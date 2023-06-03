package kr.co.suitcarrier.web.repository;

import kr.co.suitcarrier.web.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {


}