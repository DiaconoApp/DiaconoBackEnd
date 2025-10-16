package com.diacono.diacono.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
public class RestErrorMessage {

    private HttpStatus status;
    private String message;
    private final List<String> details;

    public RestErrorMessage(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.details = null;
    }

    public RestErrorMessage(List<String> details, String message, HttpStatus status) {
        this.details = details;
        this.message = message;
        this.status = status;
    }
}
