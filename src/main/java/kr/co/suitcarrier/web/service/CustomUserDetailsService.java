package kr.co.suitcarrier.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kr.co.suitcarrier.web.config.CustomUserDetails;
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
            System.out.println(username + " is not found");
        }

        return customUserDetails;
    }
    
}
