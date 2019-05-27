package me.bmordue.lgm.web.api;

import me.bmordue.lgm.web.api.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class TurnsServiceErrorAdvice {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({PlayerNotFoundException.class, UserLoginNotFoundException.class})
    public void handle() {}

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({LgmServiceException.class})
    public void handle() {}
}
