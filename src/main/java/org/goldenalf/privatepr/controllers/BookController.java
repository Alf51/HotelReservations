package org.goldenalf.privatepr.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.goldenalf.privatepr.dto.BookDto;
import org.goldenalf.privatepr.models.Book;
import org.goldenalf.privatepr.services.BookService;
import org.goldenalf.privatepr.utils.erorsHandler.ErrorHandler;
import org.goldenalf.privatepr.utils.erorsHandler.ErrorResponse;
import org.goldenalf.privatepr.utils.erorsHandler.bookError.BookErrorException;
import org.goldenalf.privatepr.utils.erorsHandler.clientError.ClientErrorException;
import org.goldenalf.privatepr.utils.erorsHandler.hotelError.HotelErrorException;
import org.goldenalf.privatepr.utils.erorsHandler.roomError.RoomErrorException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final ModelMapper modelMapper;

    @GetMapping("/{id_book}")
    public BookDto getBook(@PathVariable("id_book") int id) {
        return convertToBookDto(bookService.getBook(id).orElseThrow(() -> new BookErrorException("Бронь не найдена")));
    }

    @GetMapping("/{id_hotel}/allHotelBooks")
    public List<BookDto> getAllHotelBooks(@PathVariable("id_hotel") int hotelId) {
        return convertToBookDtoList(bookService.findAllByHotelId(hotelId));
    }

    @GetMapping("/{id_client}/allClientBooks")
    public List<BookDto> getAllBookByClient(@PathVariable("id_client") int clientId) {
        return convertToBookDtoList(bookService.findAllByClientId(clientId));
    }

    @PostMapping("/new")
    //TODO Валидация - чтобы бронь была свободна на заданные даты
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
    //TODO Валидация - чтобы бронь была свободна на заданные даты
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
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable("id") int id) {
        bookService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(DateTimeParseException e) {
        String errorMessage = "Некорректный формат даты. Введена '" + e.getParsedString() + "'. Введите дату в формате dd-MM-yyyy";
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
