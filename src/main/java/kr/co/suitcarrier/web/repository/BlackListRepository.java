package kr.co.suitcarrier.web.repository;

import kr.co.suitcarrier.web.entity.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlackListRepository extends JpaRepository<BlackList, Long> {


}