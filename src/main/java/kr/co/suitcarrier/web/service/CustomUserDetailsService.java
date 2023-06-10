package kr.co.suitcarrier.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kr.co.suitcarrier.web.config.CustomUserDetails;
import kr.co.suitcarrier.web.dto.SignupRequestDto;
import kr.co.suitcarrier.web.entity.User;
import kr.co.suitcarrier.web.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = null;
        CustomUserDetails customUserDetails = new CustomUserDetails();

        try {
            // If a value is present, returns the value, otherwise throws an exception produced by the exception supplying function.
            user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
            
            // Set the user details
            customUserDetails.setUuid(user.getUuid().toString());
            customUserDetails.setEmail(user.getEmail());
            customUserDetails.setPassword(user.getPassword());
            customUserDetails.setName(user.getName());
            customUserDetails.setRole(user.getRole().name());
            customUserDetails.setEnabled(user.isEnabled());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(username + " is not found");
        }

        return customUserDetails;
    }

    // 이메일로 검색(회원가입 시 중복 가입 확인 목적으로 사용)
    public int countByEmail(String email) {
        return userRepository.countByEmail(email);
    }

    // 전화번호로 검색(회원가입 시 중복 전화 확인 목적으로 사용)
    public int countByContact(String contact) {
        return userRepository.countByContact(contact);
    }

    // 회원가입
    public void signup(SignupRequestDto dto) {
        userRepository.save(dto.toEntity());
    }
    
}
