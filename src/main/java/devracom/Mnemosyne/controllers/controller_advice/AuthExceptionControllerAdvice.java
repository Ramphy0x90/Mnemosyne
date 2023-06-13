package devracom.Mnemosyne.controllers.controller_advice;

import devracom.Mnemosyne.exceptions.Auth.WrongCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthExceptionControllerAdvice {
    @ExceptionHandler(WrongCredentialsException.class)
    public ResponseEntity<?> handleException(WrongCredentialsException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
    }
}
