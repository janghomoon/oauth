package com.oauth2.domain.repository;

import com.oauth2.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  User findById(String userName);
}
