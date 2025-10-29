package com.nt.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.nt.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="MEMBER_INFO")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
	
	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + ", name=" + name + ", fatherName=" + fatherName
				+ ", password=" + password + ", mobileNo=" + mobileNo + ", dob=" + dob + ", email=" + email
				+ ", address=" + address + ", roles=" + roles + "]";
	}


	@Id
	@Column(name="USER_ID",length=60)
	private String userId;
	
	@Column(name="USER_NAME",length=60,unique = true)
	private String userName;
	
	@Column(name="NAME",length=60)
	private String name;
	
	@Column(name="FATHER_NAME",length=60)
	private String fatherName;
	
	@Column(name="PASSWORD",length=60)
	private String password;
	
	@Column(name="MOBILE_NO")
	private String mobileNo;
	
	@Column(name="DOB",length=60)
	private LocalDate dob;
	
	@Column(name="EMAIL",length=60)
	private String email;
	
	@Column(name="ADDRESS",length=60)
	private String address;
	
	@Column(name="VERSION")
	@Version
	private Long version;
	
	@ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();
	
	
	@OneToMany(mappedBy = "admin")
	private List<FundTransaction> transaction=new ArrayList<>();
	
//	@OneToMany(mappedBy = "user")
//	private List<FundTransaction> event=new ArrayList<>();
	
	 @PrePersist
	    public void onPrePersist() {
	        // Set the creation date to the current time, automatically
	        
	        
	        // Generate a custom ID if it's null
	        if (this.userId == null) {
	            this.userId = "F" + UUID.randomUUID().toString()
	                                           .replace("-", "")
	                                           .substring(0, 8)
	                                           .toUpperCase();
	        }
	    }

}
