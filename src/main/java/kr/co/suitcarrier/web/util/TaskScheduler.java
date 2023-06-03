package kr.co.suitcarrier.web.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import kr.co.suitcarrier.web.repository.RefreshTokenRepository;

@Component
public class TaskScheduler {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    // scheduled every 1 hour
    @Scheduled(cron = "0 0 0/1 * * *")
    public void deleteExpiredRefreshToken() {
        refreshTokenRepository.deleteByRefreshJwtAndCreatedAt();
    }
}
