package com.nt.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.nt.dto.PageResponseDto;
import com.nt.dto.TransactionDetailsDto;
import com.nt.entity.FundTransaction;

public class PaymentImplService implements IPaymentService {
	
//	@Transactional
//    @Override
//    public FundResponseDto performTransaction(FundAddDto dto) {
//
//        if (dto.getAmount() == null || dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
//            throw new InvalidAmountException("Invalid amount");
//        }
//
//        // Load or create fund
//        Fund fund = fundRepo.loadTheFund()
//                .orElseGet(() -> {
//                    Fund f = new Fund();
//                    f.setAmount(BigDecimal.ZERO);
//                    return fundRepo.save(f);
//                });
//
//        // Update fund
//        switch (dto.getType()) {
//            case DEPOSITE -> fund.setAmount(fund.getAmount().add(dto.getAmount()));
//            case WITHDRAW, REVERSE -> {
//                if (fund.getAmount().compareTo(dto.getAmount()) < 0)
//                    throw new InsufficientFundException("Insufficient fund");
//                fund.setAmount(fund.getAmount().subtract(dto.getAmount()));
//            }
//            default -> throw new IllegalArgumentException("Invalid transaction type");
//        }
//
//        fundRepo.save(fund);
//
//        // Get current admin
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
//        User admin = userRepo.findById(userPrinciple.getUserId())
//                .orElseThrow(() -> new UserNotFoundException("User not found"));
//
//        // Create transaction
//        FundTransaction txn = new FundTransaction();
//        txn.setAmount(dto.getAmount());
//        txn.setType(dto.getType());
//        txn.setReason(dto.getReason());
//        txn.setFund(fund);
//        txn.setAdmin(admin);
//
//        transactionRepo.save(txn);
//
//        // Map last 5 transactions to DTOs
//        List<TransactionDto> lastTxns = transactionRepo.findTop5ByOrderByCreatedAtDesc()
//                .stream()
//                .map(mapper::toDto)
//                .toList();
//                 
//        return new FundResponseDto(fund.getAmount(), lastTxns);
//    }
//	
//
//
//    @Transactional(readOnly = true)
//    @Override
//    public FundResponseDto getTheFundDetail() {
//
//        Fund fund = fundRepo.loadTheFund()
//                .orElseThrow(() -> new FundIsNotAvailableException("Fund is not available"));
//
//        List<TransactionDto> lastTxns = transactionRepo.findTop5ByOrderByCreatedAtDesc()
//                .stream()
//                .map(mapper::toDto)
//                .toList();
//
//        return new FundResponseDto(fund.getAmount(), lastTxns);
//    }
	
//	@Override
//	public PageResponseDto<TransactionDetailsDto> getAllTransactions(int page, int size) {
//		
//		Pageable pageable=PageRequest.of(page, size);
//		
//		Page<FundTransaction> page1=transactionRepo.findAllByOrderByCreatedAtDesc(pageable);
//		
//		List<FundTransaction> list=page1.getContent();
//		
//		List<TransactionDetailsDto> list1=list.stream().map(transaction-> {
//			return new TransactionDetailsDto(transaction.getAdmin().getName(),transaction.getAmount(),transaction.getType(),transaction.getReason(),transaction.getCreatedAt());
//		}).collect(Collectors.toList());	
//		return new PageResponseDto<TransactionDetailsDto>(list1, page1.getNumber(), list1.size(), page1.getTotalElements(), page1.getTotalPages(), page1.isLast());
//	}

}
