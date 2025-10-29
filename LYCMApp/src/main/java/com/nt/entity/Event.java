package com.nt.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="EVENT_INFO")
public class Event {
	
	@Id
	@Column(name="EVENT_ID")
	private String eventId;
	
	@Column(name="EVENT_NAME",length=30)
	private String eventName;
	
	@Column(name="START_DATE")
	private LocalDate evenStartDate;
	
	@Column(name="END_DATE")
	private LocalDate eventEndDate;
	
	
	@Column(name="EVENT_TIME")
	private LocalDateTime eventTime;
	
	@Column(name="DESCRIPTION",length=100)
	private String description;
	
	@Column(name="LOCATION")
	private String location;
	
	@Version
	@Column(name="VERSION")
	private Long version;
	
	@CreationTimestamp
	@Column(name="CREATED_AT")
	private LocalDateTime createdAt;
	
	@ManyToOne(targetEntity = User.class,fetch = FetchType.LAZY)
	@JoinColumn(name="USER_ID",referencedColumnName = "USER_ID")
	private User user;
	
}
