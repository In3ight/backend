package kr.co.suitcarrier.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

// Spring Boot에서 로그인 관련 처리가 없을 때 작동하는 기본 로그인을 비활성화
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class WebApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}
}
