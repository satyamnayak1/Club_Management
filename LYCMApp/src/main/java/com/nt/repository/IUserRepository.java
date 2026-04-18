package com.nt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import com.nt.entity.User;

public interface IUserRepository extends JpaRepository<User,Long> {
	
	public boolean existsByEmail(String email);
	
	public Optional<User> findByEmail(String email);
}
