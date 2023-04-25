package kr.co.suitcarrier.web.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
    
    private static final long JWT_ACCESS_EXPIRATION_TIME = 3600; // 1 hour

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void set(String key, String value) {
        //TODO: System.out 삭제
        System.out.println("RedisService.set() key: " + key);
        System.out.println("RedisService.set() value: " + value);
        redisTemplate.opsForValue().set(key, value);
    }

    public void setTimeout(String key, String value, long duration) {
        redisTemplate.opsForValue().set(key, value, duration, TimeUnit.SECONDS);
    }

    public void setAccessToken(String key, String value) {
        redisTemplate.opsForValue().set(key, value, JWT_ACCESS_EXPIRATION_TIME, TimeUnit.SECONDS);
    }

    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
