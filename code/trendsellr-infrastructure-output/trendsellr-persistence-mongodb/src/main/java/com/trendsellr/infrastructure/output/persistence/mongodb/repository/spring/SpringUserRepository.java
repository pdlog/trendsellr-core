package com.trendsellr.infrastructure.output.persistence.mongodb.repository.spring;

import com.trendsellr.infrastructure.output.persistence.mongodb.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringUserRepository extends MongoRepository<UserEntity, String> {

    Optional<UserEntity> findByEmail(final String email);
}
