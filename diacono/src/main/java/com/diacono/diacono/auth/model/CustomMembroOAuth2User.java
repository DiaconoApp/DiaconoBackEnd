package com.diacono.diacono.auth.model;

import com.diacono.diacono.membro.model.entity.Membro; // Assumindo que sua entidade está aqui
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public class CustomMembroOAuth2User implements OAuth2User {

    private final Membro membro;
    private final Map<String, Object> attributes;

    public CustomMembroOAuth2User(Membro membro, Map<String, Object> attributes) {
        this.membro = membro;
        this.attributes = attributes;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_MEMBRO"));
    }

    @Override
    public String getName() {
        return membro.getIdInterno().toString();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }


    public Membro getMembro() {
        return membro;
    }

    public UUID getMembroId() {
        return membro.getIdExterno();
    }

    public String getEmail() {
        return membro.getEmail();
    }
}
