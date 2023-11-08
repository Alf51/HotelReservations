package org.goldenalf.privatepr.utils;

import org.goldenalf.privatepr.utils.exeptions.InsufficientAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class VerifyingAccess {
    public static void checkPossibilityAction(String login) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = (authentication.getAuthorities().stream()
                .anyMatch(role -> role.toString().equals("ROLE_ADMIN")));

        if (!isAdmin) {
            boolean isValidLogin = authentication.getName().equals(login);
            if (!isValidLogin) {
                throw new InsufficientAccessException("Недостаточно прав для действия: неверно указанный логин или запись не может принадлежать текущему пользователю");
            }
        }
    }

    public static void checkPossibilityAction(String loginPerformingUpdate, String loginInDB) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = (authentication.getAuthorities().stream()
                .anyMatch(role -> role.toString().equals("ROLE_ADMIN")));

        if (!isAdmin) {
            boolean isValidLogin = authentication.getName().equals(loginPerformingUpdate)
                    && authentication.getName().equals(loginInDB);

            if (!isValidLogin) {
                throw new InsufficientAccessException("Недостаточно прав для действия. Запись не принадлежит текущему пользователю");
            }
        }
    }
}
