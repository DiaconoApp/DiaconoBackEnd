package com.diacono.diacono.auth.model;

import com.diacono.diacono.domain.entity.Membro;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public class CustomMembroOAuth2User implements OidcUser {

    private final Membro membro;
    private final OAuth2User oauth2UserDelegate;
    private final OidcUser oidcUserDelegate;

    public CustomMembroOAuth2User(Membro membro, OidcUser oidcUser) {
        this.membro = membro;
        this.oidcUserDelegate = oidcUser;
        this.oauth2UserDelegate = oidcUser;
    }


    public CustomMembroOAuth2User(Membro membro, OAuth2User oauth2User) {
        this.membro = membro;
        this.oidcUserDelegate = null;
        this.oauth2UserDelegate = oauth2User;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String cargoName = membro.getCargoMembro().name();
        return Collections.singletonList(
                new SimpleGrantedAuthority("SCOPE_" + cargoName)
        );
    }

    @Override
    public String getName() {
        return membro.getIdInterno().toString();
    }


    @Override
    public Map<String, Object> getAttributes() {
        return oauth2UserDelegate.getAttributes();
    }


    @Override
    public Map<String, Object> getClaims() {
        return (oidcUserDelegate != null) ? oidcUserDelegate.getClaims() : oauth2UserDelegate.getAttributes();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return (oidcUserDelegate != null) ? oidcUserDelegate.getUserInfo() : null;
    }

    @Override
    public OidcIdToken getIdToken() {
        return (oidcUserDelegate != null) ? oidcUserDelegate.getIdToken() : null;
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