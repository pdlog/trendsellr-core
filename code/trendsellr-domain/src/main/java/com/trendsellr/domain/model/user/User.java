package com.trendsellr.domain.model.user;

import com.trendsellr.domain.model.Metadata;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Represents a user in the domain layer.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String id;

    private String email;

    private String username;

    private String password;

    private Set<Role> roles;

    private Metadata metadata;
}