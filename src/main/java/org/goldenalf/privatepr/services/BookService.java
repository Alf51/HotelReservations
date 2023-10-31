package org.goldenalf.privatepr.services;

import lombok.RequiredArgsConstructor;
import org.goldenalf.privatepr.models.Book;
import org.goldenalf.privatepr.repositories.BookRepository;
import org.goldenalf.privatepr.utils.erorsHandler.reviewError.ReviewErrorException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void delete(int id) {
        if (getBook(id).isPresent()) {
            bookRepository.deleteById(id);
        } else {
            throw new ReviewErrorException("Бронь не найдена");
        }
    }

    @Transactional
    public void update(int id, Book bookByUpdate) {
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
}
