package com.nt.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.nt.enums.EventStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="EVENT_REGISTRATION")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventRegistration {
	
	@Id
	@Column(name="REGISTRATION_ID")
	private String registrationId;
	
	private String name;
	
	private String mobileNo;
	
	private String email;
	
	@CreationTimestamp
	private LocalDateTime registerAt;
	
	private EventStatus status;
	
	@ManyToOne(targetEntity = User.class,fetch = FetchType.EAGER)
	@JoinColumn(name = "USER_ID",referencedColumnName = "USER_ID")
	private User user;
	
	@ManyToOne(targetEntity = Event.class,fetch = FetchType.EAGER)
	@JoinColumn(name = "EVENT_ID",referencedColumnName = "EVENT_ID")
	private Event event;
	
}
