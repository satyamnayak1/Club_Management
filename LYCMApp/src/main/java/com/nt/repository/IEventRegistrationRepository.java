package com.nt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.entity.EventRegistration;

public interface IEventRegistrationRepository extends JpaRepository<EventRegistration, String> {

}
