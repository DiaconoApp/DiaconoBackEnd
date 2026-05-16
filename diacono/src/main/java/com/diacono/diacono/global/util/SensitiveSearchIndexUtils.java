package com.diacono.diacono.global.util;

import java.text.Normalizer;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public final class SensitiveSearchIndexUtils {
    private static final String TOKEN_DELIMITER = "|";

    private SensitiveSearchIndexUtils() {
    }

    public static String exactHash(String value) {
        return normalizeExact(value)
                .map(SensitiveFieldCryptoUtils::hmacSha256)
                .orElse(null);
    }

    public static String searchToken(String value) {
        return normalize(stripLikeWildcards(value))
                .map(SensitiveFieldCryptoUtils::hmacSha256)
                .orElse(null);
    }

    public static String searchTokens(String... values) {
        Set<String> tokens = new LinkedHashSet<>();

        for (String value : values) {
            normalize(value).ifPresent(normalized -> collectTokens(normalized, tokens));
        }

        if (tokens.isEmpty()) {
            return null;
        }

        return tokens.stream()
                .map(SensitiveFieldCryptoUtils::hmacSha256)
                .map(hash -> TOKEN_DELIMITER + hash + TOKEN_DELIMITER)
                .collect(Collectors.joining());
    }

    public static String likeToken(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }
        return TOKEN_DELIMITER + token + TOKEN_DELIMITER;
    }

    private static void collectTokens(String normalized, Set<String> tokens) {
        String compact = normalized.replace(" ", "");
        collectSubstrings(compact, tokens);

        for (String part : normalized.split(" ")) {
            collectSubstrings(part, tokens);
        }
    }

    private static void collectSubstrings(String value, Set<String> tokens) {
        if (value == null || value.isBlank()) {
            return;
        }

        int length = value.length();
        for (int start = 0; start < length; start++) {
            for (int end = start + 1; end <= length; end++) {
                tokens.add(value.substring(start, end));
            }
        }
    }

    private static Optional<String> normalize(String value) {
        if (value == null || value.isBlank()) {
            return Optional.empty();
        }

        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", " ")
                .trim()
                .replaceAll("\\s+", " ");

        return normalized.isBlank() ? Optional.empty() : Optional.of(normalized.replace(" ", ""));
    }

    private static Optional<String> normalizeExact(String value) {
        if (value == null || value.isBlank()) {
            return Optional.empty();
        }

        String normalized = value.trim().toLowerCase().replaceAll("\\s+", "");
        return normalized.isBlank() ? Optional.empty() : Optional.of(normalized);
    }

    private static String stripLikeWildcards(String value) {
        return value == null ? null : value.replace("%", "").trim();
    }
}
