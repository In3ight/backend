package kr.co.suitcarrier.web.repository.post;

import kr.co.suitcarrier.web.entity.post.PostState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostStateRepository extends JpaRepository<PostState, Long> {


}
