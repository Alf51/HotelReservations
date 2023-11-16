package org.goldenalf.privatepr.utils.erorsHandler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.Locale;

@RequiredArgsConstructor
@Component
public class ErrorHandler {
    private final MessageSource messageSource;

    public String getErrorMessage(BindingResult bindingResult) {
        StringBuilder errMessage = new StringBuilder();
        bindingResult.getFieldErrors().forEach(fieldError -> {
            errMessage.append(fieldError.getField()).append(" - ").append(fieldError.getDefaultMessage()).append("; ");
        });
        return errMessage.toString();
    }

    public String getErrorMessage(String errorCode) {
        return messageSource.getMessage(errorCode, null, Locale.getDefault());
    }
}