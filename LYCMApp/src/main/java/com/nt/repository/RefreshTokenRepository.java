package com.nt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByJti(String jti);
}
