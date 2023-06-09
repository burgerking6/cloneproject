package com.example.cloneburgerking.repository;

import com.example.cloneburgerking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByKakaoId(Long id);
    Optional<User> findByEmail(String email);

}
