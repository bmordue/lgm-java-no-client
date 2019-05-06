package me.bmordue.lgm.web.api;

import me.bmordue.lgm.web.api.exceptions.GameNotFoundException;
import me.bmordue.lgm.web.api.exceptions.LgmServiceException;
import me.bmordue.lgm.web.api.exceptions.UserLoginNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GamesServiceErrorAdvice {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({LgmServiceException.class})
    public void handle() {}

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({UserLoginNotFoundException.class})
    public void handle (UserLoginNotFoundException e) {}

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({GameNotFoundException.class})
    public void handle(GameNotFoundException e) {}
}
