package com.nt.repository;



import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import com.nt.entity.Fund;

import jakarta.persistence.LockModeType;

public interface IFundRepository extends JpaRepository<Fund, String> {

	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("Select f from Fund f")
	Optional<Fund> loadTheFund();
	
	
	
	

//    @Modifying
//    @Query("UPDATE Fund f SET f.amount = f.amount + :amount WHERE f.fundId = :fundId")
//    int deposit(@Param("fundId") String fundId, @Param("amount") BigDecimal amount);
//
//    @Modifying
//    @Query("UPDATE Fund f SET f.amount = f.amount - :amount WHERE f.fundId = :fundId AND f.amount >= :amount")
//    int withdraw(@Param("fundId") String fundId, @Param("amount") BigDecimal amount);
//
//    @Modifying
//    @Query("UPDATE Fund f SET f.amount = f.amount - :amount WHERE f.fundId = :fundId AND f.amount >= :amount")
//    int reverse(@Param("fundId") String fundId, @Param("amount") BigDecimal amount);
}
