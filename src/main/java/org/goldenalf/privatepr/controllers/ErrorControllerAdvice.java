package org.goldenalf.privatepr.controllers;

import lombok.RequiredArgsConstructor;
import org.goldenalf.privatepr.utils.erorsHandler.ErrorHandler;
import org.goldenalf.privatepr.utils.exeptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@ControllerAdvice
@RequiredArgsConstructor
public class ErrorControllerAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ErrorControllerAdvice.class);

    private final ErrorHandler errorHandler;

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception e, Model model) {
        logger.error(e.getMessage());
        String errorMessage = errorHandler.getErrorMessage("validation.hotelBook.server.error");
        model.addAttribute("time", LocalDateTime.now());
        model.addAttribute("errorMessage", errorMessage);
        return "error/simple-error";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleException(MethodArgumentNotValidException e, Model model) {
        String errorMessage = errorHandler.getErrorMessage(e.getBindingResult());
        model.addAttribute("time", LocalDateTime.now());
        model.addAttribute("errorMessage", errorMessage);
        return "error/simple-error";
    }

    @ExceptionHandler(DateTimeParseException.class)
    public String handleException(DateTimeParseException e, Model model) {
        String errorMessage = errorHandler
                .getErrorMessage("validation.hotelBook.date.exception.message-date") + e.getParsedString();

        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("time", LocalDateTime.now());
        return "error/simple-error";
    }

    @ExceptionHandler({HotelErrorException.class,
            ClientErrorException.class,
            RoomErrorException.class,
            BookErrorException.class,
            InsufficientAccessException.class,
            ReviewErrorException.class})
    public String handleException(Exception e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        model.addAttribute("time", LocalDateTime.now());
        return "error/simple-error";
    }
}
