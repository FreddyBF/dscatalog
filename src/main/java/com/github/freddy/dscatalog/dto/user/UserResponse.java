package com.github.freddy.dscatalog.dto.user;

import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

import com.github.freddy.dscatalog.model.User;

public record UserResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        List<String> roles
) {
    public UserResponse(User user) {
        this(user.getId(),
                user.getFirstName(), user.getLastName(), user.getEmail(),
                user.getRoles()
                        .stream()
                        .map(role -> role.getAuthority().name())
                        .collect(Collectors.toList()));
    }
}
