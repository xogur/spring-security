package com.project.repository;

import com.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByUsername(String username);
    Optional<User> findByProviderAndProviderId(String provider, String providerId);
}
