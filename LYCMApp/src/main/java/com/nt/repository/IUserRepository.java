package com.nt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import com.nt.entity.User;

public interface IUserRepository extends JpaRepository<User, String> {
	
	public Optional<User> findByUserName(String username);
	
	public boolean existsByUserName(String username);
}
