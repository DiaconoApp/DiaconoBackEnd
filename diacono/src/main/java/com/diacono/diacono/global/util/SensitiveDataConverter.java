package com.diacono.diacono.global.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Converter
public class SensitiveDataConverter implements AttributeConverter<String, String> {

    private static final String AES_TRANSFORMATION = "AES/GCM/NoPadding";
    private static final String AES_ALGORITHM = "AES";
    private static final int GCM_TAG_BITS = 128;
    private static final int IV_LENGTH_BYTES = 12;

    // Prefixo de versao para permitir evolucao do formato futuramente.
    private static final String ENCRYPTED_PREFIX = "enc:v1:";

    // Chave em Base64 (16/24/32 bytes apos decode).
    private static final String KEY_ENV_NAME = "APP_AES_KEY_B64";

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null || attribute.isBlank()) {
            return attribute;
        }

        if (attribute.startsWith(ENCRYPTED_PREFIX)) {
            return attribute;
        }

        try {
            byte[] iv = new byte[IV_LENGTH_BYTES];
            SECURE_RANDOM.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, buildKey(), new GCMParameterSpec(GCM_TAG_BITS, iv));

            byte[] cipherText = cipher.doFinal(attribute.getBytes(StandardCharsets.UTF_8));
            byte[] payload = new byte[iv.length + cipherText.length];

            System.arraycopy(iv, 0, payload, 0, iv.length);
            System.arraycopy(cipherText, 0, payload, iv.length, cipherText.length);

            return ENCRYPTED_PREFIX + Base64.getEncoder().encodeToString(payload);
        } catch (Exception ex) {
            throw new IllegalStateException("Erro ao criptografar campo sensivel", ex);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return dbData;
        }

        // Retrocompatibilidade: linhas antigas sem criptografia continuam legiveis.
        if (!dbData.startsWith(ENCRYPTED_PREFIX)) {
            return dbData;
        }

        try {
            byte[] payload = Base64.getDecoder().decode(dbData.substring(ENCRYPTED_PREFIX.length()));
            if (payload.length <= IV_LENGTH_BYTES) {
                throw new IllegalStateException("Payload de criptografia invalido");
            }

            byte[] iv = new byte[IV_LENGTH_BYTES];
            byte[] cipherText = new byte[payload.length - IV_LENGTH_BYTES];

            System.arraycopy(payload, 0, iv, 0, IV_LENGTH_BYTES);
            System.arraycopy(payload, IV_LENGTH_BYTES, cipherText, 0, cipherText.length);

            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, buildKey(), new GCMParameterSpec(GCM_TAG_BITS, iv));

            byte[] plainText = cipher.doFinal(cipherText);
            return new String(plainText, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            throw new IllegalStateException("Erro ao descriptografar campo sensivel", ex);
        }
    }

    private SecretKeySpec buildKey() {
        String base64Key = System.getenv(KEY_ENV_NAME);
        if (base64Key == null || base64Key.isBlank()) {
            throw new IllegalStateException("Variavel de ambiente " + KEY_ENV_NAME + " nao configurada");
        }

        byte[] decodedKey;
        try {
            decodedKey = Base64.getDecoder().decode(base64Key);
        } catch (IllegalArgumentException ex) {
            throw new IllegalStateException("Valor da chave em " + KEY_ENV_NAME + " nao esta em Base64 valido", ex);
        }

        int keyLength = decodedKey.length;
        if (keyLength != 16 && keyLength != 24 && keyLength != 32) {
            throw new IllegalStateException("Chave AES invalida. Use 16, 24 ou 32 bytes apos Base64 decode");
        }

        return new SecretKeySpec(decodedKey, AES_ALGORITHM);
    }
}

