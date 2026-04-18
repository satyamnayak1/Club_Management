package com.nt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.entity.RoleEntity;

import com.nt.enums.Role;

public interface IRoleRepository extends JpaRepository<RoleEntity, Long> {
	
	public Optional<RoleEntity> findByName(Role name);

}
