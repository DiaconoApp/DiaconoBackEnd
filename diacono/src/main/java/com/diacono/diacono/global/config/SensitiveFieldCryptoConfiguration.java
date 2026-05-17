package com.diacono.diacono.global.config;

import com.diacono.diacono.global.util.SensitiveFieldCryptoUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SensitiveFieldCryptoConfiguration {
    private final String encryptionKey;

    public SensitiveFieldCryptoConfiguration(@Value("${app.field.encryption.key}") String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    @PostConstruct
    void configure() {
        SensitiveFieldCryptoUtils.configure(encryptionKey);
    }
}
