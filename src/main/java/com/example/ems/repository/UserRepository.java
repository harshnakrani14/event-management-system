package com.example.ems.repository;

import com.example.ems.model.User;
import com.example.ems.exception.NotFoundException;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByName(String username);

    default User findByNameOrThrow(String name) {
        return findByName(name)
                .orElseThrow(() -> new NotFoundException(name, " not found"));
    }

    User findByEmail(String email);

    default User findUserByIdOrThrow(String userId) {
        return findById(userId)
                .orElseThrow(() -> new NotFoundException("User",userId));
    }

}