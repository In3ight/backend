package kr.co.suitcarrier.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.suitcarrier.web.dto.LoginRequestDto;
import kr.co.suitcarrier.web.repository.UserRepository;
import kr.co.suitcarrier.web.service.CustomUserDetailsService;
import kr.co.suitcarrier.web.service.RefreshTokenService;
import kr.co.suitcarrier.web.util.JwtTokenUtil;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping(path="/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        
        UserDetails userDetails = null;
        
        try {
            // email로 user 찾기
            userDetails = customUserDetailsService.loadUserByUsername(loginRequestDto.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (passwordEncoder.matches(loginRequestDto.getPassword(), userDetails.getPassword())) {
            // user가 있고 비밀번호가 맞으면 access token, refresh token 반환
            // return new LoginResponseDto(jwtTokenUtil.generateAccessToken(user), jwtTokenUtil.generateRefreshToken(user));

            // Set JWTs as HTTP-only cookie
            Cookie cookieAccess = new Cookie("SC_access_token", jwtTokenUtil.generateAccessToken(userDetails));
            cookieAccess.setHttpOnly(true);
            cookieAccess.setMaxAge((int)jwtTokenUtil.getJwtAccessExpirationTime());
            cookieAccess.setPath("/");
            // cookieAccess.setSecure(true);
            response.addCookie(cookieAccess);

            String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);
            refreshTokenService.saveRefreshToken(refreshToken, loginRequestDto.getEmail());
            Cookie cookieRefresh = new Cookie("SC_refresh_token", refreshToken);
            cookieRefresh.setHttpOnly(true);
            cookieRefresh.setMaxAge((int)jwtTokenUtil.getJwtRefreshExpirationTime());
            cookieRefresh.setPath("/refreshAccessToken");
            // cookieRefresh.setSecure(true);
            response.addCookie(cookieRefresh);
            
            return ResponseEntity.ok().build();
        } else {
            // user가 없거나 비밀번호가 틀리면 null 반환
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/refreshAccessToken")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        // cookie 확인 (http-only refresh token)
        Cookie[] cookies = request.getCookies();
        // refresh token으로 access token 재발급
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals("SC_refresh_token")) {
                    try {
                        // refresh token으로 user 찾기
                        UserDetails userDetails = customUserDetailsService.loadUserByUsername(jwtTokenUtil.getUsernameFromRefreshToken(cookie.getValue()));
                        // refresh token이 유효한지 확인
                        if(jwtTokenUtil.validateRefreshToken(cookie.getValue())) {
                            // refresh token으로 access token 재발급
                            // Set Access Token as HTTP-only cookie
                            Cookie cookieAccess = new Cookie("SC_access_token", jwtTokenUtil.generateAccessToken(userDetails));
                            cookieAccess.setHttpOnly(true);
                            cookieAccess.setMaxAge((int)jwtTokenUtil.getJwtAccessExpirationTime());
                            cookieAccess.setPath("/");
                            // cookieAccess.setSecure(true);
                            response.addCookie(cookieAccess);
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
    public String logout() {
        // access token redis에 등록
        // refresh token DB에서 삭제
        return "true";
    }
    
}
