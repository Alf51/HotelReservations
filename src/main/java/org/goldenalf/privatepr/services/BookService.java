package org.goldenalf.privatepr.services;

import lombok.RequiredArgsConstructor;
import org.goldenalf.privatepr.models.Book;
import org.goldenalf.privatepr.models.Client;
import org.goldenalf.privatepr.models.Room;
import org.goldenalf.privatepr.repositories.BookRepository;
import org.goldenalf.privatepr.utils.VerifyingAccess;
import org.goldenalf.privatepr.utils.erorsHandler.ErrorHandler;
import org.goldenalf.privatepr.utils.exeptions.BookErrorException;
import org.goldenalf.privatepr.utils.exeptions.ClientErrorException;
import org.goldenalf.privatepr.utils.exeptions.RoomErrorException;
import org.goldenalf.privatepr.utils.erorsHandler.validator.BookValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final RoomService roomService;
    private final ClientService clientService;
    private final BookValidator bookValidator;

    @Transactional
    public void save(Book book) {
        getValidBook(book);
        VerifyingAccess.checkPossibilityAction(book.getClient().getLogin());

        bookRepository.save(book);
    }

    @Transactional
    public void delete(int id) {
        Book bookByDeleted = getBook(id).orElseThrow(() -> new BookErrorException("Бронь не найдена"));
        String login = bookByDeleted.getClient().getLogin();
        VerifyingAccess.checkPossibilityAction(login);

        bookRepository.deleteById(id);
    }

    @Transactional
    public void update(int id, Book bookByUpdate) {
        Book bookInDB = getBook(id).orElseThrow(() -> new BookErrorException("Бронь не найдена"));
        getValidBook(bookByUpdate);
        VerifyingAccess.checkPossibilityAction(bookByUpdate.getClient().getLogin(), bookInDB.getClient().getLogin());
        bookByUpdate.setId(id);
        bookRepository.save(bookByUpdate);
    }

    @Transactional(readOnly = true)
    public Optional<Book> getBook(int id) {
        return bookRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Book> getAllBook() {
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Book> findAllByRoomId(long roomId) {
        return bookRepository.findAllByRoomId(roomId);
    }

    @Transactional(readOnly = true)
    public List<Book> findAllByClientId(String login) {
        VerifyingAccess.checkPossibilityAction(login);
        return bookRepository.findAllByClientLogin(login);
    }

    private Book getValidBook(Book book) {
        long roomId = book.getRoom().getId();
        Room room = roomService.getRoom(roomId).orElseThrow(() -> new RoomErrorException("Комната не найдена по id=" + roomId));

        String login = book.getClient().getLogin();
        Client client = clientService.findByLogin(login).orElseThrow(() -> new ClientErrorException("Клиент с логином '" + login + "' не найден"));

        book.setClient(client);
        book.setRoom(room);

        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(book, Book.class.getName());
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new BookErrorException(ErrorHandler.getErrorMessage(bindingResult));
        }
        return book;
    }
}
