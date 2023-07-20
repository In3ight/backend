package kr.co.suitcarrier.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
            customUserDetails.setId(user.getId());
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

    // 회원탈퇴 (uer 테이블에서 enabled 컬럼을 0으로 업데이트)
    public void deleteById() {
        userRepository.updateEnabledZeroById(((CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
    }

    // 회원탈퇴 취소 (uer 테이블에서 enabled 컬럼을 1로 업데이트)
    public void cancelDeleteById() {
        userRepository.updateEnabledOneById(((CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
    }

    // 회원가입
    public void signup(SignupRequestDto dto) {
        userRepository.save(dto.toEntity());
    }
    
}
