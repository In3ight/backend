package kr.co.suitcarrier.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.suitcarrier.web.entity.RefreshToken;
import kr.co.suitcarrier.web.repository.RefreshTokenRepository;
import kr.co.suitcarrier.web.repository.UserRepository;

@Service
public class RefreshTokenService {
    

    @Autowired
    UserRepository userRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    public RefreshToken saveRefreshToken(String refreshToken, String userName) {
        try {
            RefreshToken refreshTokenEntity = new RefreshToken();
            refreshTokenEntity.setUser(userRepository.findByEmail(userName).get());
            refreshTokenEntity.setRefreshJwt(refreshToken);
            refreshTokenRepository.save(refreshTokenEntity);
            return refreshTokenEntity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean doesRefreshTokenExists(String refreshJwt) {
        try {
            return refreshJwt.equals(refreshTokenRepository.findByRefreshJwt(refreshJwt).get().getRefreshJwt());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
