package com.nt.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="FUND_INFO")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Fund {
	
	    @Id
	    @Column(name="FUND_ID")
	    private String fundId;
	    
	    @Column(name="BALANCE")
	    private BigDecimal amount;

//	    @Enumerated(EnumType.STRING)
//	    private TransactionType type; // ADD or WITHDRAW

	    @Column(name="REASON",length=100)   
	    private String reason;
	    
	    @CreationTimestamp
	    private LocalDateTime createdAt;
	    
	    @OneToMany(mappedBy = "fund", cascade = CascadeType.PERSIST, orphanRemoval = false)
	    private List<FundTransaction> transactions = new ArrayList<>();
	    
	    
	    @PrePersist
	    public void onPrePersist() {
	        // Set the creation date to the current time, automatically
	        this.createdAt = LocalDateTime.now();
	        
	        // Generate a custom ID if it's null
	        if (this.fundId == null) {
	            this.fundId = "F" + UUID.randomUUID().toString()
	                                           .replace("-", "")
	                                           .substring(0, 8)
	                                           .toUpperCase();
	        }
	    }


		@Override
		public String toString() {
			return "Fund [fundId=" + fundId + ", amount=" + amount + ", reason=" + reason + ", createdAt=" + createdAt
					+ "]";
		}
	    

}
