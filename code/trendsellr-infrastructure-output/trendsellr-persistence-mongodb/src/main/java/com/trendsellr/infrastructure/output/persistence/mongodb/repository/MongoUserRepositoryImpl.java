package com.trendsellr.infrastructure.output.persistence.mongodb.repository;

import com.trendsellr.domain.model.user.User;
import com.trendsellr.domain.repository.UserRepository;
import com.trendsellr.infrastructure.output.persistence.mongodb.entity.UserEntity;
import com.trendsellr.infrastructure.output.persistence.mongodb.repository.mapper.user.UserRepositoryMapper;
import com.trendsellr.infrastructure.output.persistence.mongodb.repository.spring.SpringUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MongoUserRepositoryImpl implements UserRepository {

    private final SpringUserRepository springUserRepository;

    private final UserRepositoryMapper userRepositoryMapper;

    @Override
    public User save(final User user) {
        log.info("Saving user with email: {}", user.getEmail());
        final UserEntity entityToSave = this.userRepositoryMapper.toEntity(user);
        final UserEntity savedEntity = this.springUserRepository.save(entityToSave);

        return this.userRepositoryMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findByEmail(final String email) {
        log.info("Finding user by email: {}", email);

        return this.springUserRepository.findByEmail(email)
                .map(this.userRepositoryMapper::toDomain);
    }

    @Override
    public void deleteAll() {
        log.info("Deleting all users");
        this.springUserRepository.deleteAll();
    }
}