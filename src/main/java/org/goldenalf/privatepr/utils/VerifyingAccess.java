package org.goldenalf.privatepr.utils;

import lombok.RequiredArgsConstructor;
import org.goldenalf.privatepr.utils.exeptions.InsufficientAccessException;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class VerifyingAccess {
    private final MessageSource messageSource;

    public void checkPossibilityAction(String login) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = (authentication.getAuthorities().stream()
                .anyMatch(role -> role.toString().equals("ROLE_ADMIN")));

        if (!isAdmin) {
            boolean isValidLogin = authentication.getName().equals(login);
            if (!isValidLogin) {
                throw new InsufficientAccessException(messageSource
                        .getMessage("validation.hotelBook.security.permissions.bad-owner", null, Locale.getDefault()));
            }
        }
    }

    public void checkPossibilityAction(String loginPerformingUpdate, String loginInDB) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = (authentication.getAuthorities().stream()
                .anyMatch(role -> role.toString().equals("ROLE_ADMIN")));

        if (!isAdmin) {
            boolean isValidLogin = authentication.getName().equals(loginPerformingUpdate)
                    && authentication.getName().equals(loginInDB);

            if (!isValidLogin) {
                throw new InsufficientAccessException(messageSource
                        .getMessage("validation.hotelBook.security.permissions.non-owner", null, Locale.getDefault()));
            }
        }
    }
}
