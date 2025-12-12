package com.diacono.diacono.global.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
public class RestResponseMessage {
    private HttpStatus status;
    private String message;

}
