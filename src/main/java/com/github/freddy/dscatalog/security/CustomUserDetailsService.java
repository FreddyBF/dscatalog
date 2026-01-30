package com.github.freddy.dscatalog.security;


import com.github.freddy.dscatalog.security.UserDetailsImpl;
import com.github.freddy.dscatalog.exception.ResourceNotFoundException;
import com.github.freddy.dscatalog.model.User;
import com.github.freddy.dscatalog.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow();

        var authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority().name()))
                .toList();

        return new UserDetailsImpl(
                user.getId().toString(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}

