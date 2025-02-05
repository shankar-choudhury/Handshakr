package com.SWE2025.project.prototype.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
    Optional<User> findByUsername(String userName);
    boolean existsByUsername(String name);
    boolean existsByEmail(String email);
}
