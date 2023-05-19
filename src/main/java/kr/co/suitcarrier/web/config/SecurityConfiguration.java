package kr.co.suitcarrier.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    
    @Value("${security.enable-csrf:true}")
    private boolean csrfEnabled;

    @Autowired
    private JwtFilter jwtFilter;

    // Password encoder, used by Spring to encode and verify user passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 개발용으로 csrf를 비활성화한 경우, csrf 설정 비활성화
        if(!csrfEnabled) {
            // Disable CSRF (Cross Site Request Forgery)
            http.csrf().disable();
        }

        // JWT 사용 위해 세션 비활성화
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // XSS Protection, Content Security Policy 설정
        http.headers()
            .xssProtection()
            .and()
            // every contents source must be derived from the server
            .contentSecurityPolicy("default-src 'self'");

        // Request PATH에 따른 요구 권한 설정
        http
            .authorizeHttpRequests((authorize) -> authorize
                // .requestMatchers("/**").permitAll()
                .requestMatchers("/api/hello").permitAll()
                .requestMatchers("/auth/logout").authenticated()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                .requestMatchers("/product/**").hasAuthority("USER")
                .requestMatchers("/search/**").permitAll()
                .requestMatchers("/error").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/api-docs/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .anyRequest().denyAll()
            );

        // Add our custom JWT security filter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
