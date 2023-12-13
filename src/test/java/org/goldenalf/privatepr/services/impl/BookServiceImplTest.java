package org.goldenalf.privatepr.services.impl;

import org.goldenalf.privatepr.models.Book;
import org.goldenalf.privatepr.models.Client;
import org.goldenalf.privatepr.models.Room;
import org.goldenalf.privatepr.repositories.BookRepository;
import org.goldenalf.privatepr.utils.VerifyingAccess;
import org.goldenalf.privatepr.utils.erorsHandler.ErrorHandler;
import org.goldenalf.privatepr.utils.erorsHandler.validator.BookValidator;
import org.goldenalf.privatepr.utils.exeptions.ClientErrorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private RoomServiceImpl roomService;
    @Mock
    private ClientServiceImpl clientService;
    @Mock
    private BookValidator bookValidator;
    @Mock
    private VerifyingAccess verifyingAccess;
    @Mock
    private ErrorHandler errorHandler;

    @InjectMocks
    private BookServiceImpl bookService;


    @Test
    void update_updateBookForExistingClient_updateSuccess() {
        Book bookInDB = getBook();

        Book bookByUpdate = new Book();
        bookByUpdate.setId(1);
        Room room = new Room();
        room.setId(2);
        bookByUpdate.setRoom(room);
        Client client = new Client();
        client.setLogin("Bee");
        bookByUpdate.setClient(client);

        when(bookService.getBook(bookByUpdate.getId())).thenReturn(Optional.of(bookInDB));
        when(roomService.getRoom(room.getId())).thenReturn(Optional.of(room));
        when(clientService.findByLogin(client.getLogin())).thenReturn(Optional.of(client));

        // Вызываем метод update
        bookService.update(bookByUpdate.getId(), bookByUpdate);

        // Проверяем, что clientService был вызван с правильным аргументом
        verify(clientService).findByLogin(client.getLogin());

        // Проверяем, что verifyingAccess.checkPossibilityAction был вызван с правильными логинами
        verify(verifyingAccess).checkPossibilityAction(
                bookByUpdate.getClient().getLogin(),
                bookInDB.getClient().getLogin()
        );

        // Проверяем, что bookRepository.save был вызван с правильной книгой
        verify(bookRepository).save(bookByUpdate);
    }


    @Test
    void save_saveBookForExistingClient_saveSuccess() {
        Book book = getBook();
        Room room = book.getRoom();
        long roomId = room.getId();
        Client client = book.getClient();
        String login = client.getLogin();

        when(roomService.getRoom(roomId)).thenReturn(Optional.of(room));
        when(clientService.findByLogin(login)).thenReturn(Optional.of(client));

        bookService.save(book);

        // Проверяем, что bookRepository.save был вызван с правильной книгой
        verify(bookRepository).save(book);
    }

    @Test
    void save_saveBookForNotExistingClient_saveFailed() {
        Book book = getBook();
        Room room = book.getRoom();
        long roomId = room.getId();
        Client client = book.getClient();
        String login = client.getLogin();

        when(roomService.getRoom(roomId)).thenReturn(Optional.of(room));
        when(clientService.findByLogin(login)).thenReturn(Optional.empty());
        when(errorHandler.getErrorMessage(anyString())).thenReturn("Error message");

        ClientErrorException exception = assertThrows(ClientErrorException.class, () ->
                bookService.save(book));

        // Проверяем, что исключение содержит правильное сообщение
        assertEquals("Error message", exception.getMessage());

        // Проверяем, что bookRepository.save не был вызван
        verify(bookRepository, never()).save(book);
    }

    private Book getBook() {
        Client client = new Client();
        client.setName("Monarch");
        client.setLogin("Bee");

        Room room = new Room(512, 7.62, true);
        room.setId(1);

        Book book = new Book();
        book.setCheckIn(LocalDate.of(2023, 12, 15));
        book.setCheckOut(LocalDate.of(2023, 12, 25));
        book.setClient(client);
        book.setRoom(room);
        return book;
    }
}