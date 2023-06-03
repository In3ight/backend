package kr.co.suitcarrier.web.repository;

import kr.co.suitcarrier.web.entity.RentalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalDateRepository extends JpaRepository<RentalDate, Long> {


}