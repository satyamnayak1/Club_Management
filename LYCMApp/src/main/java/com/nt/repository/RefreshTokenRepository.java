package com.nt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

}
