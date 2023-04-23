package kr.co.suitcarrier.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {
    
    // Password encoder, used by Spring to encode and verify user passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF (Cross Site Request Forgery)
            .csrf().disable();

        http
            .authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/product/**").hasRole("USER")
                .requestMatchers("/auth/**").authenticated()
                .requestMatchers("/search/**","/login").permitAll()
            );

        // Add our custom JWT security filter
        http.addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
