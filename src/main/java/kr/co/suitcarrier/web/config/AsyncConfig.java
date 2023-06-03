package kr.co.suitcarrier.web.config;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig {
    @Value("${CORE_POOL_SIZE}")
    private static int CORE_POOL_SIZE;
    @Value("${MAX_POOL_SIZE}")
    private static int MAX_POOL_SIZE;
    @Value("${QUEUE_CAPACITY}")
    private static int QUEUE_CAPACITY;

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE); // 기본 스레드 수
        executor.setMaxPoolSize(MAX_POOL_SIZE); // 최대 스레드 수
        executor.setQueueCapacity(QUEUE_CAPACITY); // 대기 큐
        executor.setThreadNamePrefix("suitcarrier-async-"); // 스레드 이름 접두사
        executor.initialize();
        return executor;
    }
    
}
