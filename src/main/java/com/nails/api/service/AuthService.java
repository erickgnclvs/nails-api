package com.nails.api.service;

import com.nails.api.dto.auth.AuthResponse;
import com.nails.api.dto.auth.LoginRequest;
import com.nails.api.dto.auth.RegisterRequest;
import com.nails.api.entity.User;
import com.nails.api.entity.enums.UserRole;
import com.nails.api.exception.NailsException;
import com.nails.api.entity.Profile;
import com.nails.api.entity.enums.ProfileType;
import com.nails.api.repository.ProfileRepository;
import com.nails.api.repository.UserRepository;
import com.nails.api.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw NailsException.emailAlreadyExists(request.getEmail());
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user = userRepository.save(user);

        Profile profile = new Profile();
        profile.setUser(user);
        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setPhoneNumber(request.getPhoneNumber());
        profile.setBusinessName(request.getBusinessName());
        profile.setDescription(request.getDescription());
        profile.setProfileType(request.getRole() == UserRole.PROVIDER ? ProfileType.PROVIDER : ProfileType.CUSTOMER);
        if (request.getRole() == UserRole.PROVIDER) {
            profile.setIsVerified(false);
        }
        profileRepository.save(profile);

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities("ROLE_" + user.getRole().name())
                .build();

        String token = jwtTokenUtil.generateToken(userDetails);
        return new AuthResponse(token, user.getId(), user.getRole(), user.getEmail());
    }

    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> NailsException.invalidCredentials());

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtTokenUtil.generateToken(userDetails);
            
            return new AuthResponse(token, user.getId(), user.getRole(), user.getEmail());
        } catch (Exception e) {
            throw NailsException.invalidCredentials();
        }
    }
}
