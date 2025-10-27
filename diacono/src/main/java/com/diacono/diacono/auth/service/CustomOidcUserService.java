package com.diacono.diacono.auth.service;

import com.diacono.diacono.auth.model.CustomMembroOAuth2User;
import com.diacono.diacono.membro.model.entity.EnumCargoMembro;
import com.diacono.diacono.membro.model.entity.EnumStatusMembro;
import com.diacono.diacono.membro.model.entity.Membro;
import com.diacono.diacono.membro.service.MembroService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class CustomOidcUserService extends OidcUserService {

    private final MembroService membroService;

    public CustomOidcUserService(MembroService membroService) {
        this.membroService = membroService;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {


        OidcUser oidcUser = super.loadUser(userRequest);

        String email = oidcUser.getAttribute("email");
        String nome = oidcUser.getAttribute("name");

        Membro membro = membroService.buscarPorEmail(email);

        if (membro == null) {
            Membro novoMembro = new Membro();
            novoMembro.setEmail(email);
            novoMembro.setNome(nome);
            novoMembro.setCargoMembro(EnumCargoMembro.MEMBRO);
            novoMembro.setStatus(EnumStatusMembro.ATIVO);

            Membro membroSalvo = membroService.salvarMembro(novoMembro);
            return new CustomMembroOAuth2User(membroSalvo, oidcUser);
        }

        return new CustomMembroOAuth2User(membro, oidcUser);
    }
}