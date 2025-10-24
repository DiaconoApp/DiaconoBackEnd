package com.diacono.diacono.auth.service;

import com.diacono.diacono.auth.model.CustomMembroOAuth2User;
import com.diacono.diacono.membro.model.entity.EnumCargoMembro;
import com.diacono.diacono.membro.model.entity.Membro;
import com.diacono.diacono.membro.service.MembroService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;



@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MembroService membroService;

    public CustomOAuth2UserService(MembroService membroService) {
        this.membroService = membroService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        System.out.println("teste pra ver se chega aqui");

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String nome = oAuth2User.getAttribute("name");

        Membro membro = membroService.buscarPorEmail(email);

        if (membro == null) {
            Membro novoMembro = new Membro();
            novoMembro.setEmail(email);
            novoMembro.setNome(nome);
            novoMembro.setCargoMembro(EnumCargoMembro.MEMBRO);
            membroService.salvarMembro(novoMembro);
            return new CustomMembroOAuth2User(novoMembro, oAuth2User.getAttributes());
        }

        return new CustomMembroOAuth2User(membro, oAuth2User.getAttributes());
    }
}