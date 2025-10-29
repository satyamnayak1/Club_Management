package com.nt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.entity.Event;

public interface IEventRepository extends JpaRepository<Event, String> {
	
	
}
