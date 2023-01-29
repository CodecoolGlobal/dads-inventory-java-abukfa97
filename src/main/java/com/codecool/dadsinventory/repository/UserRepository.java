package com.codecool.dadsinventory.repository;

import com.codecool.dadsinventory.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Long, UserEntity> {
}

