package org.goldenalf.privatepr.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.goldenalf.privatepr.dto.BookDto;
import org.goldenalf.privatepr.models.Book;
import org.goldenalf.privatepr.services.BookService;
import org.goldenalf.privatepr.utils.erorsHandler.ErrorHandler;
import org.goldenalf.privatepr.utils.erorsHandler.ErrorResponse;
import org.goldenalf.privatepr.utils.exeptions.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;

    @GetMapping("/{id_book}")
    public BookDto getBook(@PathVariable("id_book") int id) {
        return convertToBookDto(bookService.getBook(id).orElseThrow(() -> new BookErrorException(messageSource
                .getMessage("validation.hotelBook.book.exception.book-not-found", null, Locale.getDefault()))));
    }

    @GetMapping("/{id_room}/allRoomBooks")
    public List<BookDto> getAllRoomBooks(@PathVariable("id_room") long roomId) {
        return convertToBookDtoList(bookService.findAllByRoomId(roomId));
    }

    @GetMapping("/{hotel_id}/allBooksInHotel")
    public List<BookDto> getAllBookInHotel(@PathVariable("hotel_id") int hotelId) {
        return convertToBookDtoList(bookService.findAllBookInHotel(hotelId));
    }

    @GetMapping("/all")
    public List<BookDto> getAllBooks() {
        return convertToBookDtoList(bookService.getAllBook());
    }

    @GetMapping("/{client_login}/allClientBooks")
    public List<BookDto> getAllBookByClient(@PathVariable("client_login") String login) {
        return convertToBookDtoList(bookService.findAllByClientId(login));
    }

    @PostMapping("/new")
    public ResponseEntity<HttpStatus> saveBook(@RequestBody @Valid BookDto bookDto,
                                               BindingResult bindingResult) {
        Book book = convertToBook(bookDto);

        if (bindingResult.hasErrors()) {
            throw new BookErrorException(ErrorHandler.getErrorMessage(bindingResult));
        }
        bookService.save(book);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id_book}")
    public ResponseEntity<HttpStatus> updateBook(@PathVariable("id_book") int id,
                                                 @RequestBody @Valid BookDto bookDto,
                                                 BindingResult bindingResult) {
        Book updatedBook = convertToBook(bookDto);
        if (bindingResult.hasErrors()) {
            throw new BookErrorException(ErrorHandler.getErrorMessage(bindingResult));
        }
        bookService.update(id, updatedBook);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id_book}")
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable("id_book") int id) {
        bookService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(DateTimeParseException e) {
        String errorMessage = messageSource
                .getMessage("validation.hotelBook.date.exception.message-date", null, Locale.getDefault()) + e.getParsedString();
        ErrorResponse errorResponse = new ErrorResponse(errorMessage, System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(HotelErrorException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(ClientErrorException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(RoomErrorException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(BookErrorException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException e) {
        String errorMessage = ErrorHandler.getErrorMessage(e.getBindingResult());
        ErrorResponse errorResponse = new ErrorResponse(errorMessage, System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(InsufficientAccessException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private BookDto convertToBookDto(Book book) {
        return modelMapper.map(book, BookDto.class);
    }

    private List<BookDto> convertToBookDtoList(List<Book> reviews) {
        Type listType = new TypeToken<List<BookDto>>() {
        }.getType();
        return modelMapper.map(reviews, listType);
    }

    private Book convertToBook(BookDto bookDto) {
        return modelMapper.map(bookDto, Book.class);
    }
}
