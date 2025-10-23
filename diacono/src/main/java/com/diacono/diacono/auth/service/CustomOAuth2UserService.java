package com.diacono.diacono.auth.service;

import com.diacono.diacono.auth.model.CustomMembroOAuth2User;
import com.diacono.diacono.membro.model.entity.Membro;
import com.diacono.diacono.membro.service.MembroService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


import java.util.Map;


@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MembroService membroService;
    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

    public CustomOAuth2UserService(MembroService membroService) {
        this.membroService = membroService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oauth2User = delegate.loadUser(userRequest);

        Map<String, Object> attributes = oauth2User.getAttributes();
        String email = (String) attributes.get("email");
        String nomeCompleto = (String) attributes.get("name");

        Membro membro = membroService.buscarPorEmail(email);


        if (membro == null) {
            membro = new Membro();
            membro.setEmail(email);
            membro.setNome(nomeCompleto);


            membroService.salvarMembro(membro);
        }

        return new CustomMembroOAuth2User(membro, attributes);
    }
}