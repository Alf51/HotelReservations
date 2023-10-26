package org.goldenalf.privatepr.utils.erorsHandler;

import org.springframework.validation.BindingResult;

public class ErrorHandler {

    public static String getErrorMessage(BindingResult bindingResult) {
        StringBuilder errMessage = new StringBuilder();
        bindingResult.getFieldErrors().forEach(fieldError -> {
            errMessage.append(fieldError.getField()).append(" - ").append(fieldError.getDefaultMessage()).append("; ");
        });
        return errMessage.toString();
    }
}