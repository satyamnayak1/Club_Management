package com.nt.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.entity.FundTransaction;

public interface ITransactionRepository extends JpaRepository<FundTransaction, String> {
	
	List<FundTransaction> findTop5ByOrderByCreatedAtDesc();

	Page<FundTransaction> findAllByOrderByCreatedAtDesc(Pageable pageable);

	

}
