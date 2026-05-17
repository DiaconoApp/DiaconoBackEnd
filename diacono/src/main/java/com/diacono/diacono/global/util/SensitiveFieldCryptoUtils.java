package com.diacono.diacono.global.util;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public final class SensitiveFieldCryptoUtils {
    private static final String ENCRYPTED_PREFIX = "enc:v1:";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final int GCM_TAG_BITS = 128;
    private static final int IV_LENGTH = 12;
    private static volatile SecretKeySpec secretKey;

    private SensitiveFieldCryptoUtils() {
    }

    public static String encrypt(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        if (value.startsWith(ENCRYPTED_PREFIX)) {
            return value;
        }

        try {
            byte[] iv = new byte[IV_LENGTH];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, requireConfiguredKey(), new GCMParameterSpec(GCM_TAG_BITS, iv));
            byte[] encrypted = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));

            byte[] payload = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, payload, 0, iv.length);
            System.arraycopy(encrypted, 0, payload, iv.length, encrypted.length);
            return ENCRYPTED_PREFIX + Base64.getEncoder().encodeToString(payload);
        } catch (Exception ex) {
            throw new IllegalStateException("Falha ao criptografar campo sensivel", ex);
        }
    }

    public static String decrypt(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        if (!value.startsWith(ENCRYPTED_PREFIX)) {
            return value;
        }

        try {
            byte[] payload = Base64.getDecoder().decode(value.substring(ENCRYPTED_PREFIX.length()));
            byte[] iv = Arrays.copyOfRange(payload, 0, IV_LENGTH);
            byte[] encrypted = Arrays.copyOfRange(payload, IV_LENGTH, payload.length);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, requireConfiguredKey(), new GCMParameterSpec(GCM_TAG_BITS, iv));
            return new String(cipher.doFinal(encrypted), StandardCharsets.UTF_8);
        } catch (Exception ex) {
            throw new IllegalStateException("Falha ao descriptografar campo sensivel", ex);
        }
    }

    public static String hmacSha256(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(requireConfiguredKey().getEncoded(), HMAC_ALGORITHM));
            byte[] digest = mac.doFinal(value.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(Arrays.copyOf(digest, 16));
        } catch (Exception ex) {
            throw new IllegalStateException("Falha ao gerar indice de busca de campo sensivel", ex);
        }
    }

    public static void validateConfigured() {
        requireConfiguredKey();
    }

    public static void configure(String keyBase64) {
        if (keyBase64 == null || keyBase64.isBlank()) {
            throw new IllegalStateException("APP_FIELD_ENCRYPTION_KEY e obrigatoria para criptografia de campos sensiveis");
        }

        try {
            byte[] keyBytes = Base64.getDecoder().decode(keyBase64);
            if (keyBytes.length != 16 && keyBytes.length != 24 && keyBytes.length != 32) {
                throw new IllegalStateException("APP_FIELD_ENCRYPTION_KEY deve ter 16, 24 ou 32 bytes em Base64");
            }
            secretKey = new SecretKeySpec(keyBytes, "AES");
        } catch (IllegalArgumentException ex) {
            throw new IllegalStateException("APP_FIELD_ENCRYPTION_KEY deve estar em Base64 valido", ex);
        }
    }

    private static SecretKeySpec requireConfiguredKey() {
        SecretKeySpec configuredKey = secretKey;
        if (configuredKey == null) {
            throw new IllegalStateException("APP_FIELD_ENCRYPTION_KEY e obrigatoria para criptografia de campos sensiveis");
        }
        return configuredKey;
    }
}
