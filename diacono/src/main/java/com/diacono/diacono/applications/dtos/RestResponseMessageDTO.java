package com.diacono.diacono.applications.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
public class RestResponseMessageDTO {
    private HttpStatus status;
    private String message;

}
