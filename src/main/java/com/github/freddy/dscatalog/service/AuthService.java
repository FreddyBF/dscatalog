package com.github.freddy.dscatalog.service;


import com.github.freddy.dscatalog.auth.JwtService;
import com.github.freddy.dscatalog.security.UserDetailsImpl;
import com.github.freddy.dscatalog.security.CustomUserDetailsService;
import com.github.freddy.dscatalog.dto.user.LoginRequest;
import com.github.freddy.dscatalog.dto.user.LoginResponse;
import com.github.freddy.dscatalog.dto.user.UserResponse;
import com.github.freddy.dscatalog.exception.ConflictException;
import com.github.freddy.dscatalog.exception.ResourceNotFoundException;
import com.github.freddy.dscatalog.exception.UnauthorizedException;
import com.github.freddy.dscatalog.model.Role;
import com.github.freddy.dscatalog.model.RoleAuthority;
import com.github.freddy.dscatalog.model.User;
import com.github.freddy.dscatalog.repository.RoleRepository;
import com.github.freddy.dscatalog.repository.UserRepository;
import com.github.freddy.dscatalog.dto.user.UserRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final String TOKEN_TYPE = "Bearer";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Transactional
    public UserResponse register(UserRequest user) {

        if (userRepository.existsByEmail(user.email())) {
            throw new ConflictException("E-mail já cadastrado");
        }
        User userEntity = new User();
        userEntity.setEmail(user.email());
        userEntity.setFirstName(user.firstName());
        userEntity.setLastName(user.lastName());
        userEntity.setPassword(passwordEncoder.encode(user.password()));

        Role role = roleRepository.findByAuthority(RoleAuthority.ROLE_OPERATOR)
                .orElseThrow(() -> new ResourceNotFoundException("Role não encontrada"));
        userEntity.getRoles().add(role);
        return new UserResponse(userRepository.save(userEntity));
    }


    public LoginResponse login(LoginRequest dto) {
        // 1. Autenticação via Spring Security
        var authenticationToken = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        //Vai ai banco verificar user e senha
        var authentication = authenticationManager.authenticate(authenticationToken);
        // 2. Recuperar o usuário autenticado
        UserDetails user = (UserDetails) authentication.getPrincipal();
        System.out.println(user.getAuthorities());
        // 3. Gerar ambos os tokens usando o seu JwtService
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return new LoginResponse(accessToken, refreshToken, TOKEN_TYPE);

    }


    public LoginResponse refreshToken(String refreshToken) {
        // 1. Extrair e-mail do refresh token
        String email = jwtService.getUserEmailFromToken(refreshToken);

        // 2. Carregar o usuário
        var user = (UserDetailsImpl) userDetailsService.loadUserByUsername(email);

        // 3. Validar se o token não expirou
        if (!jwtService.validateToken(refreshToken)) {
            throw new UnauthorizedException("Refresh Token inválido ou expirado.");
        }

        // 4. Gerar novos tokens
        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        return new LoginResponse(newAccessToken, newRefreshToken, TOKEN_TYPE);
    }
}
