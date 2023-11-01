package org.goldenalf.privatepr.services;

import lombok.RequiredArgsConstructor;
import org.goldenalf.privatepr.models.Book;
import org.goldenalf.privatepr.models.Client;
import org.goldenalf.privatepr.models.Room;
import org.goldenalf.privatepr.repositories.BookRepository;
import org.goldenalf.privatepr.utils.erorsHandler.ErrorHandler;
import org.goldenalf.privatepr.utils.erorsHandler.bookError.BookErrorException;
import org.goldenalf.privatepr.utils.erorsHandler.clientError.ClientErrorException;
import org.goldenalf.privatepr.utils.erorsHandler.roomError.RoomErrorException;
import org.goldenalf.privatepr.utils.erorsHandler.validator.BookValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

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
        bookRepository.save(getValidBook(book));
    }

    @Transactional
    public void delete(int id) {
        if (getBook(id).isPresent()) {
            bookRepository.deleteById(id);
        } else {
            throw new BookErrorException("Бронь не найдена");
        }
    }

    @Transactional
    public void update(int id, Book bookByUpdate) {
        bookByUpdate.setId(id);
        bookRepository.save(getValidBook(bookByUpdate));
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
    //TODO реализовать
    public List<Book> findAllByHotelId(int hotelId) {
        //return bookRepository.findAllByHotelId(hotelId);
        return null;
    }

    @Transactional(readOnly = true)
    //TODO реализовать
    public List<Book> findAllByClientId(int clientId) {
        return null;
        //return reviewRepository.findAllByClientId(clientId);
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
