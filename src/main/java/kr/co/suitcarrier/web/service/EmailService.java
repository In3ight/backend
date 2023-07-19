package kr.co.suitcarrier.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.suitcarrier.web.config.SesConfig;

@Service
public class EmailService {

    @Autowired
    private SesConfig sesConfig;
    
    public boolean sendSignUpVerificationEmail(String email, String name, String authCode) {
        return sesConfig.sendSignUpTemplateEmail(email, name, authCode);
    }

    public boolean sendSignUpCompleteEmail(String email, String name) {
        return sesConfig.sendSignUpCompleteTemplateEmail(email, name);
    }

}
