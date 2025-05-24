package com.example.bookreviewapp.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory("localhost", 6379);
    }

    @Primary
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory cf)
    {
        RedisTemplate<String, String> tpl = new RedisTemplate<>();

        tpl.setConnectionFactory(cf);
        // 키와 값에 대해 String 직렬화기 설정
        tpl.setKeySerializer(new StringRedisSerializer());
        tpl.setValueSerializer(new StringRedisSerializer());
        tpl.setHashKeySerializer(new StringRedisSerializer());
        tpl.setHashValueSerializer(new StringRedisSerializer());
        tpl.afterPropertiesSet();
        return tpl;
    }
    @Bean
    public RedisTemplate<String, Object> redisTemplateForObject(RedisConnectionFactory cf) {
        RedisTemplate<String, Object> tpl = new RedisTemplate<>();
        tpl.setConnectionFactory(cf);

        // 키는 String, 값은 JSON 직렬화
        tpl.setKeySerializer(new StringRedisSerializer());
        tpl.setValueSerializer(new org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer());

        tpl.setHashKeySerializer(new StringRedisSerializer());
        tpl.setHashValueSerializer(new org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer());

        tpl.afterPropertiesSet();
        return tpl;
    }

}