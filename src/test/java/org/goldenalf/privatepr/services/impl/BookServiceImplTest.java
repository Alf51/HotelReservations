package org.goldenalf.privatepr.services.impl;

import org.goldenalf.privatepr.models.Book;
import org.goldenalf.privatepr.models.Client;
import org.goldenalf.privatepr.models.Room;
import org.goldenalf.privatepr.repositories.BookRepository;
import org.goldenalf.privatepr.utils.VerifyingAccess;
import org.goldenalf.privatepr.utils.erorsHandler.ErrorHandler;
import org.goldenalf.privatepr.utils.erorsHandler.validator.BookValidator;
import org.goldenalf.privatepr.utils.exeptions.ClientErrorException;
import org.goldenalf.privatepr.utils.exeptions.InsufficientAccessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
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
    void delete_clientNotHaveAccessToDelete_deleteFailed() {
        Book bookByDeleted = getBook();
        int bookId = bookByDeleted.getId();

        when(bookService.getBook(bookId)).thenReturn(Optional.of(bookByDeleted));

        //Бросаем ошибку, недостаточно прав для действия
        doThrow(InsufficientAccessException.class).when(verifyingAccess).checkPossibilityAction(bookByDeleted.getClient().getLogin());

        // Вызываем метод delete
        InsufficientAccessException exception = assertThrows(InsufficientAccessException.class, () -> bookService.delete(bookId));

        // Проверяем, что метод deleteById не вызывался
        verify(bookRepository, never()).deleteById(bookId);
    }

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
    void findAllByRoomId_getAllRoomByRoomId() {
        long roomId = 1L;

        // Mocking bookRepository.findAllByRoomId
        List<Book> expectedBooks = getRoomList().stream().filter(b -> b.getId() == roomId)
                .flatMap(room -> room.getBookList().stream()).toList();
        when(bookRepository.findAllByRoomId(roomId)).thenReturn(expectedBooks);

        List<Book> result = bookService.findAllByRoomId(roomId);

        // Проверяем, что bookRepository.findAllByRoomId был вызван с правильным аргументом
        verify(bookRepository).findAllByRoomId(roomId);

        // Проверяем, что результат соответствует ожиданиям
        assertEquals(expectedBooks, result);
    }

    @Test
    void findAllByClientId_getListForAllBooks() {
        List<Book> bookList = getBookList();
        Client client = bookList.get(0).getClient();
        String login = client.getLogin();
        List<Book> expectedBooks = bookList.stream().filter(book -> book.getClient().getLogin().equals(login)).toList();
        when(bookRepository.findAllByClientLogin(login)).thenReturn(expectedBooks);

        doNothing().when(verifyingAccess).checkPossibilityAction(login);

        List<Book> result = bookService.findAllByClientId(login);

        // Проверяем, что verifyingAccess.checkPossibilityAction был вызван с правильным аргументом
        verify(verifyingAccess).checkPossibilityAction(login);

        // Проверяем, что bookRepository.findAllByClientLogin был вызван с правильным аргументом
        verify(bookRepository).findAllByClientLogin(login);

        // Проверяем, что результат соответствует ожиданиям
        assertEquals(expectedBooks, result);
    }

    @Test
    void findAllBookInHotel_getListForAllBooks() {
        List<Room> listRoom = getRoomList();
        long sizeBook = listRoom.stream().mapToLong(room -> room.getBookList().size()).sum();
        int hotelId = 2;

        // Mocking roomService.findAllRoomsByHotelId
        when(roomService.findAllRoomsByHotelId(hotelId)).thenReturn(listRoom);

        List<Book> result = bookService.findAllBookInHotel(hotelId);

        // Проверяем, что roomService.findAllRoomsByHotelId был вызван с правильным аргументом
        verify(roomService).findAllRoomsByHotelId(hotelId);

        assertEquals(sizeBook, result.size());
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

    @Test
    void getAllBook_findAllBooks() {
        List<Book> expectedBooks = getBookList();

        when(bookRepository.findAll()).thenReturn(expectedBooks);

        List<Book> result = bookService.getAllBook();

        assertNotNull(result);
        assertEquals(expectedBooks, result);
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

    private List<Room> getRoomList() {
        Room firstRoom = new Room(512, 7.62, true);
        firstRoom.setId(1);

        Book book = new Book();
        book.setRoom(firstRoom);
        book.setId(1);

        Room secondRoom = new Room(412, 5.56, true);
        firstRoom.setId(2);

        Book book2 = new Book();
        book2.setRoom(secondRoom);
        book2.setId(2);
        return List.of(firstRoom, secondRoom);
    }

    private List<Book> getBookList() {
        Book firstBook = getBook();

        Client client = new Client();
        client.setName("Brock");
        client.setLogin("Samson");

        Room room = new Room(12, 7.62, true);
        room.setId(1);

        Book secondBook = new Book();
        secondBook.setCheckIn(LocalDate.of(2024, 11, 10));
        secondBook.setCheckOut(LocalDate.of(2024, 11, 27));
        secondBook.setClient(client);
        secondBook.setRoom(room);

        return List.of(firstBook, secondBook);
    }
}