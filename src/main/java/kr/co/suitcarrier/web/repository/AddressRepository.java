package kr.co.suitcarrier.web.repository;

import kr.co.suitcarrier.web.entity.Address;
import kr.co.suitcarrier.web.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {


}