package kr.co.suitcarrier.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.suitcarrier.web.config.SmsConfig;

@Service
public class SmsService {
    
    @Autowired
    private SmsConfig smsConfig;

    public boolean sendSignUpVerificationSms(String contact, String authCode) {
        try {
            smsConfig.sendOne(contact, authCode);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    public boolean sendChangeContactSms(String contact, String authCode) {
        try {
            smsConfig.sendOne(contact, authCode);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

}
