package kr.co.suitcarrier.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kr.co.suitcarrier.web.dto.LoginRequestDto;
import kr.co.suitcarrier.web.dto.LoginResponseDto;
import kr.co.suitcarrier.web.entity.User;
import kr.co.suitcarrier.web.repository.UserRepository;
import kr.co.suitcarrier.web.util.JwtTokenUtil;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping(path="/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto) {
        
        User user = null;
        
        try {
            // email로 user 찾기
            user = userRepository.findByEmail(loginRequestDto.getEmail()).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            // user가 있고 비밀번호가 맞으면 access token, refresh token 반환
            return new LoginResponseDto(jwtTokenUtil.generateAccessToken(user), jwtTokenUtil.generateRefreshToken(user));
        } else {
            // user가 없거나 비밀번호가 틀리면 null 반환
            return new LoginResponseDto(null, null);
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
