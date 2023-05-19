package kr.co.suitcarrier.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.suitcarrier.web.dto.LoginRequestDto;
import kr.co.suitcarrier.web.repository.UserRepository;
import kr.co.suitcarrier.web.service.CustomUserDetailsService;
import kr.co.suitcarrier.web.service.RedisService;
import kr.co.suitcarrier.web.service.RefreshTokenService;
import kr.co.suitcarrier.web.util.JwtTokenUtil;

@Tag(name = "auth", description = "인증 관련 API")
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private String accessTokenCookieName = "SC_access_token";
    private String refreshTokenCookieName = "SC_refresh_token";
    private String accessTokenRedisPrefix = "REDIS_JWT_";

    private final CookieCsrfTokenRepository cookieCsrfTokenRepository = new CookieCsrfTokenRepository();
    
    @Autowired
    UserRepository userRepository;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    RedisService redisService;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping(path="/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "로그인", description = "email을 이용하여 사용자를 조회하고 access token, refresh token을 반환합니다.")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        
        UserDetails userDetails = null;
        
        try {
            // email로 user 찾기
            userDetails = customUserDetailsService.loadUserByUsername(loginRequestDto.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

        if (passwordEncoder.matches(loginRequestDto.getPassword(), userDetails.getPassword())) {
            // user가 있고 비밀번호가 맞으면 access token, refresh token 반환
            // return new LoginResponseDto(jwtTokenUtil.generateAccessToken(user), jwtTokenUtil.generateRefreshToken(user));

            // Set JWTs as HTTP-only cookie
            Cookie accessTokenCookie = new Cookie(accessTokenCookieName, jwtTokenUtil.generateAccessToken(userDetails));
            accessTokenCookie.setHttpOnly(true);
            accessTokenCookie.setMaxAge((int)jwtTokenUtil.getJwtAccessExpirationTime());
            accessTokenCookie.setPath("/");
            // accessTokenCookie.setSecure(true);
            response.addCookie(accessTokenCookie);

            String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);
            refreshTokenService.saveRefreshToken(refreshToken, loginRequestDto.getEmail());
            Cookie refreshTokenCookie = new Cookie(refreshTokenCookieName, refreshToken);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setMaxAge((int)jwtTokenUtil.getJwtRefreshExpirationTime());
            refreshTokenCookie.setPath("/auth");
            // refreshTokenCookie.setSecure(true);
            response.addCookie(refreshTokenCookie);
            
            return ResponseEntity.ok().build();
        } else {
            // user가 없거나 비밀번호가 틀리면 null 반환
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/refreshAccessToken")
    @Operation(summary = "access token 재발급", description = "쿠키를 확인하고 refresh token으로 access token을 재발급합니다.")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        // cookie 확인 (http-only refresh token)
        Cookie[] cookies = request.getCookies();
        // refresh token으로 access token 재발급
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals(refreshTokenCookieName)) {
                    try {
                        // refresh token으로 user 찾기
                        UserDetails userDetails = customUserDetailsService.loadUserByUsername(jwtTokenUtil.getUsernameFromRefreshToken(cookie.getValue()));
                        // refresh token이 유효한지 확인
                        if(jwtTokenUtil.validateRefreshToken(cookie.getValue())) {
                            // refresh token으로 access token 재발급
                            // Set Access Token as HTTP-only cookie
                            Cookie accessTokenCookie = new Cookie(accessTokenCookieName, jwtTokenUtil.generateAccessToken(userDetails));
                            accessTokenCookie.setHttpOnly(true);
                            accessTokenCookie.setMaxAge((int)jwtTokenUtil.getJwtAccessExpirationTime());
                            accessTokenCookie.setPath("/");
                            // accessTokenCookie.setSecure(true);
                            response.addCookie(accessTokenCookie);
                            return ResponseEntity.ok().build();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return ResponseEntity.badRequest().build();
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }

    // 보안 위해 CSRF 적용
    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "refresh token을 삭제합니다.")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        // cookie 확인 (http-only refresh token)
        Cookie[] cookies = request.getCookies();
        // refresh token으로 access token 재발급
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals(accessTokenCookieName)) {
                    try {
                        // Redis에 (key, value)로 (Prefix + access token, username(email)) 저장
                        redisService.setAccessToken(accessTokenRedisPrefix + cookie.getValue(), ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
                        // access token 수명 0으로 설정
                        Cookie accessTokenCookie = new Cookie(accessTokenCookieName, null);
                        accessTokenCookie.setHttpOnly(true);
                        accessTokenCookie.setMaxAge(0);
                        accessTokenCookie.setPath("/");
                        // accessTokenCookie.setSecure(true);
                        response.addCookie(accessTokenCookie);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return ResponseEntity.badRequest().build();
                    }
                }
                else if(cookie.getName().equals(refreshTokenCookieName)) {
                    try {
                        // refresh token DB에서 삭제
                        refreshTokenService.deleteRefreshToken(cookie.getValue());
                        // refresh token 수명 0으로 설정
                        Cookie refreshTokenCookie = new Cookie(refreshTokenCookieName, null);
                        refreshTokenCookie.setHttpOnly(true);
                        refreshTokenCookie.setMaxAge(0);
                        refreshTokenCookie.setPath("/auth");
                        // refreshTokenCookie.setSecure(true);
                        response.addCookie(refreshTokenCookie);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return ResponseEntity.badRequest().build();
                    }
                }
            }
        }
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/getCsrfToken")
    @Operation(summary = "CSRF 토큰 발급", description = "CSRF 토큰이 없을 경우에 토큰이 필요한 api를 요청할 때 사용합니다.")
    public ResponseEntity<?> getCsrfToken(HttpServletRequest request, HttpServletResponse response) {
        CsrfToken csrfToken = cookieCsrfTokenRepository.generateToken(request);
        Cookie cookie = new Cookie("XSRF-TOKEN", csrfToken.getToken());
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

}
