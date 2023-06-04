package kr.co.suitcarrier.web.repository;

import kr.co.suitcarrier.web.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {


}
