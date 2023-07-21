package kr.co.suitcarrier.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.suitcarrier.web.config.CustomUserDetails;
import kr.co.suitcarrier.web.dto.LoginRequestDto;
import kr.co.suitcarrier.web.dto.SignupRequestDto;
import kr.co.suitcarrier.web.repository.UserRepository;
import kr.co.suitcarrier.web.service.CustomUserDetailsService;
import kr.co.suitcarrier.web.service.EmailService;
import kr.co.suitcarrier.web.service.RedisService;
import kr.co.suitcarrier.web.service.RefreshTokenService;
import kr.co.suitcarrier.web.service.SmsService;
// import kr.co.suitcarrier.web.service.LogService;
import kr.co.suitcarrier.web.util.JwtTokenUtil;

@Tag(name = "auth", description = "인증 관련 API")
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private static final String accessTokenCookieName = "SC_access_token";
    private static final String refreshTokenCookieName = "SC_refresh_token";
    private static final String accessTokenRedisPrefix = "REDIS_JWT_";
    private static final String emailSignUpVerifyingRedisPrefix = "EMAIL_SIGN_UP_VERIFYING_";
    private static final String emailSignUpVerifiedRedisPrefix = "EMAIL_SIGN_UP_VERIFIED_";
    private static final String contactSignUpVerifyingRedisPrefix = "CONTACT_SIGN_UP_VERIFYING_";
    private static final String contactSignUpVerifiedRedisPrefix = "CONTACT_SIGN_UP_VERIFIED_";
    private static final String emailChangeVerifyingRedisPrefix = "EMAIL_CHANGE_VERIFYING_";
    private static final String emailChangeVerifiedRedisPrefix = "EMAIL_CHANGE_VERIFIED_";
    private static final String contactChangeVerifyingRedisPrefix = "CONTACT_CHANGE_VERIFYING_";
    private static final String contactChangeVerifiedRedisPrefix = "CONTACT_CHANGE_VERIFIED_";
    
    @Autowired
    UserRepository userRepository;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    RedisService redisService;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SmsService smsService;

    // @Autowired
    // LogService logService;

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
    
    @GetMapping("/emails/{email}")
    @Operation(summary = "이메일 중복 확인", description = "이메일 중복 확인을 위해 해당 이메일 Count를 반환합니다. (0이면 중복 없음)")
    public ResponseEntity<Integer> checkEmail(@PathVariable String email) {
        return ResponseEntity.ok(customUserDetailsService.countByEmail(email));
    }

    @GetMapping("/contacts/{contact}")
    @Operation(summary = "전화번호 중복 확인", description = "전화번호 중복 확인을 위해 해당 전화번호 Count를 반환합니다. (0이면 중복 없음)")
    public ResponseEntity<Integer> checkContact(@PathVariable String contact) {
        return ResponseEntity.ok(customUserDetailsService.countByContact(contact));
    }

    @PostMapping("/sign-up/emails")
    @Operation(summary = "이메일 인증번호 전송", description = "이메일 소유 여부 확인을 위해 해당 이메일로 인증번호를 발송합니다.")
    public ResponseEntity<?> sendSignUpVerificationEmail(@RequestParam(name="email") String email) {
        email = URLDecoder.decode(email, StandardCharsets.UTF_8);
        try {
            // 인증번호 생성
            String verificationCode = String.valueOf((int)(Math.floor(Math.random()*1000000)));
            // 인증번호 Redis에 저장
            redisService.setVerificationCode(emailSignUpVerifyingRedisPrefix + email, verificationCode);
            // 이메일 전송
            emailService.sendSignUpVerificationEmail(email, email, verificationCode);
            return ResponseEntity.ok().build();       
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/sign-up/emails/verification")
    @Operation(summary = "이메일 인증번호 확인", description = "이메일로 전송된 인증번호를 확인합니다. 일정 기간 내에 변경해야 합니다.")
    public ResponseEntity<?> checkSignUpVerificationEmail(@RequestParam(name="email") String email, @RequestParam(name="verificationCode") String verificationCode) {
        email = URLDecoder.decode(email, StandardCharsets.UTF_8);
        try {
            // Redis에서 인증번호 가져오기
            String redisVerificationCode = redisService.get(emailSignUpVerifyingRedisPrefix + email);
            // 인증번호 확인
            if(redisVerificationCode.equals(verificationCode)) {
                redisService.delete(emailSignUpVerifyingRedisPrefix + email);
                // 인증된 이메일을 Redis에 일정 시간 동안 저장, 시간 내에 회원가입 완료 필요
                redisService.setVerificationCode(emailSignUpVerifiedRedisPrefix + email, email);
                return ResponseEntity.ok().build();
            }
            else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/sign-up/contacts")
    @Operation(summary = "전화번호 인증번호 전송", description = "전화번호 소유 여부 확인을 위해 해당 전화번호로 인증번호를 발송합니다.")
    public ResponseEntity<?> sendSignUpVerificationContact(@RequestParam(name="contact") String contact) {
        try {
            // 인증번호 생성
            String verificationCode = String.valueOf((int)(Math.floor(Math.random()*1000000)));
            // 인증번호 Redis에 저장
            redisService.setVerificationCode(contactSignUpVerifyingRedisPrefix + contact, verificationCode);
            // 메시지 전송
            smsService.sendSignUpVerificationSms(contact, verificationCode);
            return ResponseEntity.ok().build();       
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/sign-up/contacts/verification")
    @Operation(summary = "전화번호 인증번호 확인", description = "전화번호로 전송된 인증번호를 확인합니다.")
    public ResponseEntity<?> checkSignUpVerificationContact(@RequestParam(name="contact") String contact, @RequestParam(name="verificationCode") String verificationCode) {
        try {
            // Redis에서 인증번호 가져오기
            String redisVerificationCode = redisService.get(contactSignUpVerifyingRedisPrefix + contact);
            // 인증번호 확인
            if(redisVerificationCode.equals(verificationCode)) {
                redisService.delete(contactSignUpVerifyingRedisPrefix + contact);
                // 인증된 전화번호를 Redis에 일정 시간 동안 저장, 시간 내에 회원가입 완료 필요
                redisService.setVerificationCode(contactSignUpVerifiedRedisPrefix + contact, contact);
                return ResponseEntity.ok().build();
            }
            else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
      
    @GetMapping("/accounts")
    @Operation(summary = "회원정보 조회", description = "회원정보를 조회합니다.")
    public ResponseEntity<?> getAccount(HttpServletResponse response) {
        try {
            // user 정보 반환
            CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return ResponseEntity.ok().build();       
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/accounts")
    @Operation(summary = "회원가입", description = "필요 정보들을 전달하여 회원가입을 합니다. (이름, 이베일, 닉네임, 비밀번호(SHA3-256 해싱), 전화번호)")
    public ResponseEntity<?> signupAccount(SignupRequestDto signupRequestDto) {
        try {
            // 전화번호 중복 확인
            if(customUserDetailsService.countByContact(signupRequestDto.getContact()) != 0) {
                throw new Exception("SignUp request with the duplicated contact.");
            }
            // 이메일 중복 확인
            if(customUserDetailsService.countByEmail(signupRequestDto.getEmail()) != 0) {
                throw new Exception("SignUp request with the duplicated email.");
            }
            // 인증된 전화번호 여부 확인
            if(redisService.get(contactSignUpVerifiedRedisPrefix+signupRequestDto.getContact()) == null) {
                throw new Exception("SignUp request with the unverified contact.");
            }
            // 인증된 이메일 여부 확인
            if(redisService.get(emailSignUpVerifiedRedisPrefix+signupRequestDto.getEmail()) == null) {
                throw new Exception("SignUp request with the unverified email.");
            }
            redisService.delete(contactSignUpVerifiedRedisPrefix + signupRequestDto.getContact());
            redisService.delete(emailSignUpVerifiedRedisPrefix + signupRequestDto.getEmail());
            // 비밀번호 해싱
            signupRequestDto.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));
            // 회원가입
            customUserDetailsService.signup(signupRequestDto);
            emailService.sendSignUpCompleteEmail(signupRequestDto.getEmail(), signupRequestDto.getName());
            return ResponseEntity.ok().build();       
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/accounts")
    @Operation(summary = "회원탈퇴", description = "회원탈퇴를 합니다.")
    public ResponseEntity<?> deleteAccount() {
        try {
            // user 테이블 enabled 0으로 변경, 자정에 스케줄러가 삭제
            customUserDetailsService.deleteById();
            // user의 모든 정보 삭제
            return ResponseEntity.ok().build();       
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/accounts/contact")
    @Operation(summary = "전화번호 변경 인증번호 발송", description = "전화번호를 변경하기 위해 새로운 전화번호로 인증번호를 발송합니다.")
    public ResponseEntity<?> sendChangeContactVerificationContact(@RequestParam(name="contact") String contact) {
        try {
            return ResponseEntity.ok().build();       
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/accounts/contact/verification")
    @Operation(summary = "전화번호 변경 인증번호 확인", description = "전화번호 변경을 위해 전송된 인증번호를 확인합니다. 일정 기간 내에 변경해야 합니다.")
    public ResponseEntity<?> checkChangeContactVerificationContact(@RequestParam(name="contact") String email, @RequestParam(name="verificationCode") String verificationCode) {
        try {
            String redisVerificationCode = null;
            // 인증번호 확인
            if(redisVerificationCode.equals(verificationCode)) {
                return ResponseEntity.ok().build();
            }
            else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/accounts/email")
    @Operation(summary = "전화번호로 이메일 찾기", description = "전화번호로 이메일 전송을 전송합니다.")
    public ResponseEntity<?> sendEmailToContact() {
        try {
            return ResponseEntity.ok().build();       
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/accounts/password")
    @Operation(summary = "이메일로 임시 비밀번호 발송", description = "임시 비밀번호를 생성해 이메일로 전송합니다.")
    public ResponseEntity<?> sendTemporaryPasswordToEmail() {
        try {
            return ResponseEntity.ok().build();       
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/accounts/email")
    @Operation(summary = "비밀번호를 변경합니다.", description = "현재 비밀번호와 새 비밀번호를 전송하여 비밀번호를 변경합니다.")
    public ResponseEntity<?> changePassword(@RequestParam(name="oldPassword") String oldPassword, @RequestParam(name="newPassword") String newPassword) {
        try {
            return ResponseEntity.ok().build();       
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/accounts/{nickname}")
    @Operation(summary = "닉네임 중복 확인", description = "닉네임 중복 확인을 위해 해당 닉네임 Count를 반환합니다. (0이면 중복 없음)")
    public ResponseEntity<Integer> checkNickname(@PathVariable(name="nickname") String nickname) {
        try {
            return ResponseEntity.ok().build();       
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/accounts/nickname")
    @Operation(summary = "닉네임 변경", description = "닉네임을 변경합니다.")
    public ResponseEntity<?> changeNickname(@RequestParam(name="nickname") String nickname) {
        try {
            return ResponseEntity.ok().build();       
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // @GetMapping("/log")
    // public void log() {
    //     logService.log();
    // }
}
