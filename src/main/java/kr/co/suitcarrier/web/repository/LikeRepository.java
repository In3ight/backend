package kr.co.suitcarrier.web.repository;

import kr.co.suitcarrier.web.entity.Like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    @Query(value = "SELECT * FROM like WHERE user_id = :userId)", nativeQuery = true)
    List<Like> findByUser(Integer userId);

    @Query(value = "SELECT * FROM like WHERE user_id = :userId AND post_id = :postId)", nativeQuery = true)
    Optional<Like> findByUserAndPost(Integer userId, Integer postId);

    @Query(value = "DELETE FROM like WHERE user_id = :userId AND post_id = :postId)", nativeQuery = true)
    void deleteByUserAndPost(Integer userId, Integer postId);

}
