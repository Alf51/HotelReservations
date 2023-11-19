package org.goldenalf.privatepr.controllers;

import lombok.RequiredArgsConstructor;
import org.goldenalf.privatepr.dto.AuthStatus;
import org.goldenalf.privatepr.utils.erorsHandler.ErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
  private final ErrorHandler errorHandler;

  @GetMapping("success")
  public ResponseEntity<AuthStatus> success() {
    return ResponseEntity.ok(new AuthStatus("OK"));
  }

  @GetMapping("failed")
  public ResponseEntity<AuthStatus> failed() {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthStatus(errorHandler
            .getErrorMessage("validation.hotelBook.security.bad-credential")));
  }
}
