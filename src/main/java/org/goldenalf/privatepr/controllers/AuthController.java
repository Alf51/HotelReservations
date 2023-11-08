package org.goldenalf.privatepr.controllers;

import org.goldenalf.privatepr.dto.AuthStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {
  @GetMapping("success")
  public ResponseEntity<AuthStatus> success() {
    return ResponseEntity.ok(new AuthStatus("OK"));
  }

  @GetMapping("failed")
  public ResponseEntity<AuthStatus> failed() {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthStatus("Неправильный логин и/или пароль"));
  }
}
