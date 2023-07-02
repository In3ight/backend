package kr.co.suitcarrier.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.filter.HiddenHttpMethodFilter;

// Spring Boot에서 로그인 관련 처리가 없을 때 작동하는 기본 로그인을 비활성화, Redis 리퍼지토리 설정 비활성화
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class, RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class })
@EnableAsync
@EnableJpaAuditing
public class WebApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}

	// @DeleteMapping, @PutMapping 등의 최신 매핑기능을 이용하기 위해 추가
	@Bean
	public HiddenHttpMethodFilter hiddenHttpMethodFilter(){
		return new HiddenHttpMethodFilter();
	}	

}
