package kr.co.suitcarrier.web.repository;

import kr.co.suitcarrier.web.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {


}
