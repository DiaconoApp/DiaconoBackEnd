package com.diacono.diacono.global.error;

import com.diacono.diacono.infrastructure.exceptions.DateInvalidException;
import com.diacono.diacono.infrastructure.exceptions.TimeInvalidException;
import com.diacono.diacono.global.error.exceptions.*;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDate;
import java.util.List;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

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

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        String messageDetail = "Corpo da requisição inválido. Verifique o formato JSON e os tipos de dados.";

        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException invalidFormatException) {
            String fieldName = "";
            if (!invalidFormatException.getPath().isEmpty()) {
                fieldName = invalidFormatException.getPath().get(0).getFieldName();
            }

            if (invalidFormatException.getTargetType().isEnum()) {
                messageDetail = String.format("O campo '%s' possui um valor inválido. Verifique os valores permitidos para o Enum.", fieldName);
            } else if (invalidFormatException.getTargetType().equals(LocalDate.class)) {
                messageDetail = String.format("O campo '%s' possui uma data inválida. Use o formato yyyy-MM-dd.", fieldName);
            } else {
                messageDetail = String.format("O campo '%s' possui um valor incompatível com o tipo esperado.", fieldName);
            }
        }

        RestErrorMessage error = new RestErrorMessage(HttpStatus.BAD_REQUEST, messageDetail);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()))
                .toList();

        RestErrorMessage message = new RestErrorMessage(
                errors,
                "Erros de validação encontrados.",
                HttpStatus.BAD_REQUEST
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<RestErrorMessage> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String msg = String.format("Parâmetro '%s' inválido. Valor recebido: '%s'. Esperado tipo: %s",
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RestErrorMessage(HttpStatus.BAD_REQUEST, msg));
    }

    @ExceptionHandler(OAuth2AuthenticationException.class)
    private ResponseEntity<RestErrorMessage> oauth2AuthenticationHandler(OAuth2AuthenticationException exception) {

        String detailedMessage = "Falha na autenticação via provedor externo. Tente novamente.";

        logger.error("OAuth2 Login Error: {}", exception.getMessage(), exception);

        RestErrorMessage message = new RestErrorMessage(HttpStatus.UNAUTHORIZED, detailedMessage);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }

    @ExceptionHandler(BadCredentialsException.class)
    private ResponseEntity<RestErrorMessage> badCredentialsHandler(BadCredentialsException exception){
        RestErrorMessage message = new RestErrorMessage(HttpStatus.UNAUTHORIZED,exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }

    @ExceptionHandler(ObjectExistsException.class)
    private ResponseEntity<RestErrorMessage> objectExistsHandler(ObjectExistsException exception){
        RestErrorMessage message = new RestErrorMessage(HttpStatus.CONFLICT,exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
    }

}


