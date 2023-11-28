package org.goldenalf.privatepr.controllers;

import lombok.RequiredArgsConstructor;
import org.goldenalf.privatepr.utils.erorsHandler.ErrorHandler;
import org.goldenalf.privatepr.utils.erorsHandler.ErrorResponse;
import org.goldenalf.privatepr.utils.exeptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;

@RestControllerAdvice
@RequiredArgsConstructor
public class ErrorControllerAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ErrorControllerAdvice.class);

    private final ErrorHandler errorHandler;

    @ExceptionHandler({HotelErrorException.class,
            ClientErrorException.class,
            RoomErrorException.class,
            BookErrorException.class,
            InsufficientAccessException.class,
            ReviewErrorException.class})
    public ResponseEntity<ErrorResponse> handleHotelErrorExceptionException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidExceptionException(MethodArgumentNotValidException e) {
        String errorMessage = errorHandler.getErrorMessage(e.getBindingResult());
        ErrorResponse errorResponse = new ErrorResponse(errorMessage, System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        logger.error(e.getMessage());
        String errorMessage = errorHandler.getErrorMessage("validation.hotelBook.server.error");
        ErrorResponse errorResponse = new ErrorResponse(errorMessage, System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ErrorResponse> handleDateTimeParseExceptionException(DateTimeParseException e) {
        String errorMessage = errorHandler
                .getErrorMessage("validation.hotelBook.date.exception.message-date") + e.getParsedString();

        ErrorResponse errorResponse = new ErrorResponse(errorMessage, System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}