package com.diacono.diacono.global.config;

import com.diacono.diacono.auth.handler.CustomOAuth2AuthenticationSuccessHandler;
import com.diacono.diacono.auth.service.CustomOidcUserService;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${jwt.private.key}")
    private RSAPrivateKey rsaPrivateKey;
    @Value("${jwt.public.key}")
    private RSAPublicKey rsaPublicKey;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            CustomOidcUserService customOidcUserService,
            CustomOAuth2AuthenticationSuccessHandler successHandler
    ) throws Exception{
        http
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login/**","/api/v1/auth/**",  "/oauth2/**", "/api/v1/register/**").permitAll()
                        .requestMatchers("/h2-console/**", "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                /*oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .oidcUserService(customOidcUserService)
                        )
                        .successHandler(successHandler))*/
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .logout(logout -> logout.logoutSuccessUrl("/").permitAll());


        return http.build();
    }

    @Bean
    public JwtEncoder jwtEncoder(Environment env){
        String privKeyProp = env.getProperty("jwt.private.key", "");
        if(privKeyProp.contains("classpath:teste")){
            logger.warn("A chave privada JWT está configurada a partir de um recurso de teste no classpath ({}). NÃO utilize chaves de teste em produção", privKeyProp);
        }
        JWK jwk = new RSAKey.Builder(this.rsaPublicKey).privateKey(this.rsaPrivateKey).build();
        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));

        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public JwtDecoder jwtDecoder(Environment env){
        String pubKeyProp = env.getProperty("jwt.public.key", "");
        if(pubKeyProp.contains("classpath:teste")){
            logger.warn("A chave pública JWT está configurada a partir de um recurso de teste no classpath ({}). NÃO utilize chaves de teste em produção", pubKeyProp);
        }
        return NimbusJwtDecoder.withPublicKey(rsaPublicKey).build();
    }

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    //CRIPTOGRAFIA DA SENHA

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(List.of(
            "http://localhost:5173", 
            "https://odiacono.duckdns.org" 
        ));
        
        configuration.setAllowedMethods(List.of("GET", "POST", "DELETE", "PUT", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;

    }

}
