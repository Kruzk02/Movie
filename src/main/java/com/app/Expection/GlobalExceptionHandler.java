package com.app.Expection;

import com.app.Service.Impl.ActorServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DirectorNotFound.class)
    public ResponseEntity<ErrorResponse> handleDirectorNotFound(DirectorNotFound ex){
        ErrorResponse response = new ErrorResponse("Director Not Found");
        log.error(ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ActorNotFound.class)
    public ResponseEntity<ErrorResponse> handleActorNotFound(ActorNotFound ex){
        ErrorResponse response = new ErrorResponse("Actor Not Found");
        log.error(ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MovieNotFound.class)
    public ResponseEntity<ErrorResponse> handleMovieNotFound(MovieNotFound ex){
        ErrorResponse response = new ErrorResponse("Movie Not Found");
        log.error(ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex){
        ErrorResponse response = new ErrorResponse(ex.getMessage());
        log.error(ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PhoneNumberValidationException.class)
    public ResponseEntity<ErrorResponse> handlePhoneNumberValidationException(PhoneNumberValidationException ex){
        ErrorResponse response = new ErrorResponse(ex.getMessage());
        log.error(ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailValidationException.class)
    public ResponseEntity<ErrorResponse> handleEmailValidationException(EmailValidationException ex){
        ErrorResponse response = new ErrorResponse(ex.getMessage());
        log.error(ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordValidationException.class)
    public ResponseEntity<ErrorResponse> handlePasswordValidationException(PasswordValidationException ex){
        ErrorResponse response = new ErrorResponse(ex.getMessage());
        log.error(ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
}
