package org.goldenalf.privatepr.controllers;

import lombok.RequiredArgsConstructor;
import org.goldenalf.privatepr.utils.erorsHandler.ErrorHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final ErrorHandler errorHandler;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("failed")
    public String failed(Model model) {
        String errorMessage = errorHandler.getErrorMessage("validation.hotelBook.security.bad-credential");
        model.addAttribute("errorMessage", errorMessage);
        return "auth/login";
    }

    @GetMapping("accessDenied")
    public String accessDenied(Model model) {
        String errorMessage = errorHandler.getErrorMessage("validation.hotelBook.security.permissions.access-denied");
        model.addAttribute("errorMessage", errorMessage);
        return "auth/error-page";
    }
}
