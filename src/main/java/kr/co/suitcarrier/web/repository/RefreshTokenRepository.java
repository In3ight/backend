package kr.co.suitcarrier.web.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import kr.co.suitcarrier.web.entity.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>{
    
    Optional<RefreshToken> findByRefreshJwt(String refreshJwt);

    @Query(value = "SELECT * FROM refresh_token WHERE refresh_jwt = :refreshJwt AND created_at > DATE_ADD(NOW(), INTERVAL 21 DAY)", nativeQuery = true)
    Optional<RefreshToken> findByRefreshJwtAndCreatedAt(String refreshJwt);

    @Transactional
    void deleteByRefreshJwt(String refreshJwt);

    @Transactional
    @Query(value = "DELETE FROM refresh_token WHERE created_at < DATE_SUB(NOW(), INTERVAL 21 DAY)", nativeQuery = true)
    void deleteByRefreshJwtAndCreatedAt();

}
