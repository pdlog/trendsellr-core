package com.trendsellr.infrastructure.output.persistence.mongodb.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@Document(collection = "users")
public class UserEntity {

    @Id
    private String id;

    @Indexed(unique = true)
    private String userId;

    @Indexed(unique = true)
    private String email;

    private String username;

    private String password;

    private Set<String> roles;

    private MetadataEntity metadata;
}