package com.diacono.diacono.global.error;

import com.diacono.diacono.evento.exceptions.DateInvalidException;
import com.diacono.diacono.evento.exceptions.TimeInvalidException;
import com.diacono.diacono.global.error.exceptions.FieldInvalidException;
import com.diacono.diacono.global.error.exceptions.ObjectNotFoundException;
import com.diacono.diacono.global.error.exceptions.ObjectSaveErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    private ResponseEntity<RestErrorMessage> objectNotFoundHandler(ObjectNotFoundException exception){
        RestErrorMessage message = new RestErrorMessage(HttpStatus.NOT_FOUND,exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler(FieldInvalidException.class)
    private ResponseEntity<RestErrorMessage> fieldInvalidHandler(FieldInvalidException exception){
        RestErrorMessage message = new RestErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(DateInvalidException.class)
    private ResponseEntity<RestErrorMessage> dateInvalidHandler(DateInvalidException exception){
        RestErrorMessage message = new RestErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(TimeInvalidException.class)
    private ResponseEntity<RestErrorMessage> timeInvalidHandler(TimeInvalidException exception){
        RestErrorMessage message = new RestErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(ObjectSaveErrorException.class)
    private ResponseEntity<RestErrorMessage> objectSaveErrorHandler(ObjectSaveErrorException exception){
        RestErrorMessage message = new RestErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

}
