package com.app.Expection;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException.InternalServerError;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DirectorNotFound.class)
    public ResponseEntity<ErrorResponse> handleDirectorNotFound(DirectorNotFound ex){
        ErrorResponse response = new ErrorResponse("Director Not Found");
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex){
        ErrorResponse response = new ErrorResponse(ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
