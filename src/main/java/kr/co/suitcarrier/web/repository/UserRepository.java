package kr.co.suitcarrier.web.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kr.co.suitcarrier.web.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUuid(String uuid);
    Optional<User> findByEmail(String email);

    // 이메일로 검색(회원가입 시 중복 가입 확인 목적으로 사용)
    int countByEmail(String email);
    // 전화번호로 검색(회원가입 시 중복 가입 번호 목적으로 사용)
    int countByContact(String contact);

    // uer 테이블에서 enabled 컬럼을 0으로 업데이트
    @Query(value = "UPDATE user SET enabled = 0 WHERE id = :id", nativeQuery = true)
    void updateEnabledZeroById(Long id);
    // uer 테이블에서 enabled 컬럼을 1로 업데이트
    @Query(value = "UPDATE user SET enabled = 1 WHERE id = :id", nativeQuery = true)
    void updateEnabledOneById(Long id);
    
}
