package kr.co.suitcarrier.web.repository;

import kr.co.suitcarrier.web.entity.PostState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostStateRepository extends JpaRepository<PostState, Long> {


}
