package com.diacono.diacono.use_cases;

import com.diacono.diacono.applications.dtos.login.GoogleAuthRequestDTO;
import com.diacono.diacono.applications.dtos.login.LoginResponseDTO;
import com.diacono.diacono.domain.entity.Membro;
import com.diacono.diacono.domain.enums.EnumCargoMembro;
import com.diacono.diacono.domain.enums.EnumStatusMembro;
import com.diacono.diacono.global.error.exceptions.BadCredentialsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoogleAuthService {

	private final MembroService membroService;
	private final TokenService tokenService;
	private final JwtDecoder googleJwtDecoder;

	@Value("${google.oauth.client-id:}")
	private String googleClientId;

	public GoogleAuthService(MembroService membroService, TokenService tokenService) {
		this.membroService = membroService;
		this.tokenService = tokenService;
		this.googleJwtDecoder = JwtDecoders.fromOidcIssuerLocation("https://accounts.google.com");
	}

	public LoginResponseDTO autenticar(GoogleAuthRequestDTO googleAuthRequestDTO) {
		Jwt googleJwt = validarGoogleJwt(googleAuthRequestDTO.idToken());

		validarAudience(googleJwt);
		String email = buscarEmailValido(googleJwt);

		Membro membro = membroService.buscarPorEmail(email);
		if (membro == null) {
			throw new BadCredentialsException("Usuario nao cadastrado");
		//	membro = criarMembroPeloGoogle(googleJwt, email);
		}

		String jwtValue = tokenService.generateToken(membro);
		long expiresIn = tokenService.getExpiresIn();

		return new LoginResponseDTO(jwtValue, expiresIn);
	}

	private Jwt validarGoogleJwt(String idToken) {
		try {
			return googleJwtDecoder.decode(idToken);
		} catch (JwtException exception) {
			throw new BadCredentialsException("Token do Google invalido");
		}
	}

	private void validarAudience(Jwt googleJwt) {
		List<String> audience = googleJwt.getAudience();

		if (googleClientId == null || googleClientId.isBlank()) {
			throw new BadCredentialsException("Configuracao do Google OAuth ausente na aplicacao");
		}

		if (audience == null || !audience.contains(googleClientId)) {
			throw new BadCredentialsException("Token do Google nao pertence a aplicacao");
		}
	}

	private String buscarEmailValido(Jwt googleJwt) {
		Boolean emailVerificado = googleJwt.getClaim("email_verified");
		String email = googleJwt.getClaimAsString("email");

		if (email == null || email.isBlank() || !Boolean.TRUE.equals(emailVerificado)) {
			throw new BadCredentialsException("Email do Google nao verificado");
		}

		return email;
	}

	private Membro criarMembroPeloGoogle(Jwt googleJwt, String email) {
		Membro membro = new Membro();
		membro.setEmail(email);
		membro.setNome(googleJwt.getClaimAsString("name"));
		membro.setCargoMembro(EnumCargoMembro.MEMBRO);
		membro.setStatus(EnumStatusMembro.ATIVO);

		return membroService.salvarMembro(membro);
	}
}

