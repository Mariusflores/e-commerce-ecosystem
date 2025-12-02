package org.example.userservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.userservice.dto.LoginRequest;
import org.example.userservice.dto.UserRequest;
import org.example.userservice.models.Role;
import org.example.userservice.models.User;
import org.example.userservice.repository.UserRepository;
import org.example.userservice.util.JwtUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    public void registerUser(UserRequest userRequest){

        User user = User.builder()
                .email(userRequest.getEmail())
                .password(hashPassword(userRequest.getPassword()))
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .phoneNumber(userRequest.getPhoneNumber())
                .role(Role.toRole(userRequest.getRole()))
                .addresses(userRequest.getAddresses())
                .build();
        log.info("registering user...");
        userRepository.save(user);
        log.info("User with email {} has been registered successfully", user.getEmail());
    }

    public String login(LoginRequest loginRequest){
        UserDetails userDetails = userDetailsService.loadUserByEmail(loginRequest.getEmail());

        if(userDetails == null){return null;}

        if(authenticate(loginRequest.getPassword(), userDetails.getPassword())){
            return jwtUtil.generateToken(userDetails.getUsername());

        }
        return null;
    }

    public boolean authenticate(String rawPassword, String storedHash){
        return passwordEncoder.matches(rawPassword, storedHash);
    }

    private String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
