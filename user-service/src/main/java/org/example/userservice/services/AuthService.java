package org.example.userservice.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.userservice.dto.LoginRequest;
import org.example.userservice.dto.UserRequest;
import org.example.userservice.models.Role;
import org.example.userservice.models.User;
import org.example.userservice.repository.UserRepository;
import org.example.userservice.util.JwtUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final UserDetailsServiceImpl detailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    @Transactional
    public String registerUser(UserRequest userRequest){

        if(userRequest.getEmail() == null || userRequest.getPassword() == null){
            throw new IllegalArgumentException("Email or password is null");
        }

        String hashedPassword = hashPassword(userRequest.getPassword());

        if(!userExists(userRequest.getEmail())){
            User user = User.builder()
                    .email(userRequest.getEmail())
                    .password(hashedPassword)
                    .firstName(userRequest.getFirstName())
                    .lastName(userRequest.getLastName())
                    .phoneNumber(userRequest.getPhoneNumber())
                    .role(Role.USER)
                    .build();
            log.info("registering user...");
            userRepository.save(user);
            log.info("User with email {} has been registered successfully", user.getEmail());
        }else{
            throw new IllegalArgumentException("User already exists");
        }
        return jwtUtil.generateToken(userRequest.getEmail());
    }

    private boolean userExists(String email) {


        return userRepository.existsByEmail(email);
    }

    public String login(LoginRequest loginRequest){
        UserDetails userDetails = detailsService.loadUserByUsername(loginRequest.getEmail());



        if(authenticate(loginRequest.getPassword(), userDetails.getPassword())){
            return jwtUtil.generateToken(userDetails.getUsername());

        }else {
            throw new BadCredentialsException("Wrong email or password");
        }
    }

    public boolean authenticate(String rawPassword, String storedHash){
        return passwordEncoder.matches(rawPassword, storedHash);
    }

    private String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
