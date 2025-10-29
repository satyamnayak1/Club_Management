package com.nt.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.nt.enums.TransactionType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name = "FUND_TXN_INFO")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FundTransaction {
	
	@Id
	@Column(name="TRANSACTION_ID",length=100)   
	private String transactionId;
	
	@Column(name="BALANCE")   
	private BigDecimal amount;
	
	@Column(name="TYPE")   
	@Enumerated(EnumType.STRING)
	private TransactionType type; // ADD or WITHDRAW
	    
	//link to admin
	@ManyToOne(targetEntity = User.class,fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
	private User admin;
	
	 // Link to fund
    @ManyToOne(fetch = FetchType.LAZY,targetEntity = Fund.class)
    @JoinColumn(name = "FUND_ID", referencedColumnName = "FUND_ID")
    private Fund fund;
	    
	@Column(name="REASON",length=100)   
	private String reason;
	
	@CreationTimestamp
	@Column(name="CREATED_AT")
	private LocalDateTime createdAt;
	    
	@PrePersist
    public void onPrePersist() {
        // Set the creation date to the current time, automatically
        this.createdAt = LocalDateTime.now();
        
        // Generate a custom ID if it's null
        if (this.transactionId == null) {
            this.transactionId = "T" + UUID.randomUUID().toString()
                                           .replace("-", "")
                                           .substring(0, 8)
                                           .toUpperCase();
        }
    }

	@Override
	public String toString() {
		return "FundTransaction [transactionId=" + transactionId + ", amount=" + amount + ", type=" + type + ", reason="
				+ reason + ", createdAt=" + createdAt + "]";
	}
	

}
