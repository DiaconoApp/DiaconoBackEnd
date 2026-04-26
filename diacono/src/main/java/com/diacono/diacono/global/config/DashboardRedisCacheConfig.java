package com.diacono.diacono.global.config;

import com.diacono.diacono.global.util.JwtUtils;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
@EnableCaching
public class DashboardRedisCacheConfig {

    private static final String MEMBERS_KPIS_CACHE = "dashboard:membros:kpis";
    private static final String MEMBERS_EVOLUTION_CACHE = "dashboard:membros:evolucao";
    private static final String MEMBERS_AGE_RANGE_CACHE = "dashboard:membros:faixa-etaria";
    private static final String MEMBERS_GENDER_CACHE = "dashboard:membros:genero";
    private static final String MINISTRIES_KPIS_CACHE = "dashboard:ministerios:kpis";
    private static final String MINISTRIES_EVOLUTION_CACHE = "dashboard:ministerios:evolucao";
    private static final String MINISTRIES_MEMBERS_COUNT_CACHE = "dashboard:ministerios:quantidade-membros";
    private static final String MINISTRIES_EVENTS_COUNT_CACHE = "dashboard:ministerios:quantidade-eventos";

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(3))
                .disableCachingNullValues()
                .computePrefixWith(cacheName -> "diacono:" + cacheName + "::")
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        cacheConfigurations.put(MEMBERS_KPIS_CACHE, defaultConfig.entryTtl(Duration.ofMinutes(3)));
        cacheConfigurations.put(MEMBERS_EVOLUTION_CACHE, defaultConfig.entryTtl(Duration.ofMinutes(3)));
        cacheConfigurations.put(MEMBERS_AGE_RANGE_CACHE, defaultConfig.entryTtl(Duration.ofMinutes(3)));
        cacheConfigurations.put(MEMBERS_GENDER_CACHE, defaultConfig.entryTtl(Duration.ofMinutes(3)));
        cacheConfigurations.put(MINISTRIES_KPIS_CACHE, defaultConfig.entryTtl(Duration.ofMinutes(3)));
        cacheConfigurations.put(MINISTRIES_EVOLUTION_CACHE, defaultConfig.entryTtl(Duration.ofMinutes(3)));
        cacheConfigurations.put(MINISTRIES_MEMBERS_COUNT_CACHE, defaultConfig.entryTtl(Duration.ofMinutes(3)));
        cacheConfigurations.put(MINISTRIES_EVENTS_COUNT_CACHE, defaultConfig.entryTtl(Duration.ofMinutes(3)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }

    @Bean("dashboardCacheKey")
    public KeyGenerator dashboardCacheKeyGenerator(JwtUtils jwtUtils) {
        return (target, method, params) -> {
            String igrejaId = getIgrejaIdSafely(jwtUtils);
            StringBuilder keyBuilder = new StringBuilder("igreja:")
                    .append(igrejaId)
                    .append(":metodo:")
                    .append(method.getName());

            for (Object param : params) {
                keyBuilder.append(':').append(param == null ? "null" : param.toString());
            }

            return keyBuilder.toString();
        };
    }

    private String getIgrejaIdSafely(JwtUtils jwtUtils) {
        try {
            UUID igrejaId = jwtUtils.getIgrejaId();
            return igrejaId == null ? "sem-igreja" : igrejaId.toString();
        } catch (Exception exception) {
            return "sem-igreja";
        }
    }
}

