package com.nailservices.service;

import com.nailservices.dto.AuthResponse;
import com.nailservices.dto.LoginRequest;
import com.nailservices.dto.RegisterRequest;
import com.nailservices.entity.User;
import com.nailservices.entity.UserRole;
import com.nailservices.exception.NailServicesException;
import com.nailservices.model.Profile;
import com.nailservices.model.ProfileType;
import com.nailservices.repository.ProfileRepository;
import com.nailservices.repository.UserRepository;
import com.nailservices.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw NailServicesException.emailAlreadyExists(request.getEmail());
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
                    .orElseThrow(() -> NailServicesException.invalidCredentials());

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtTokenUtil.generateToken(userDetails);
            
            return new AuthResponse(token, user.getId(), user.getRole(), user.getEmail());
        } catch (Exception e) {
            throw NailServicesException.invalidCredentials();
        }
    }
}
