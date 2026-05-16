package com.diacono.diacono.applications.dtos.googleauth;

import java.util.List;

public record GoogleIdTokenDTO(
        List<String> audience,
        String email,
        Boolean emailVerified
) {
}
