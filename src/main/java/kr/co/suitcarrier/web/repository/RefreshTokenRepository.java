package kr.co.suitcarrier.web.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import kr.co.suitcarrier.web.entity.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>{
    
    Optional<RefreshToken> findByRefreshJwt(String refreshJwt);

    @Transactional
    void deleteByRefreshJwt(String refreshJwt);

}