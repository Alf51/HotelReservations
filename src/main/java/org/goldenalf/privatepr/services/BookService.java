package org.goldenalf.privatepr.services;

import org.goldenalf.privatepr.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    void save(Book book);

    void delete(int id);

    void update(int id, Book bookByUpdate);

    List<Book> findAllBookInHotel(int hotelId);

    Optional<Book> getBook(int id);

    List<Book> getAllBook();

    List<Book> findAllByRoomId(long roomId);

    List<Book> findAllByClientId(String login);
}
