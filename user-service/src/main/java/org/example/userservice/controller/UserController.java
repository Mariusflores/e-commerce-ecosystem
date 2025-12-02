package org.example.userservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.userservice.dto.AddressRequest;
import org.example.userservice.dto.UserProfileResponse;
import org.example.userservice.dto.UserRequest;
import org.example.userservice.models.Address;
import org.example.userservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    public UserProfileResponse getUserProfile(Authentication authentication) {
        String email = authentication.getName();
        return userService.getUserProfileByEmail(email);
    }

    @PutMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    public void updateUserProfile(Authentication authentication, @RequestBody UserRequest userRequest){
        String email = authentication.getName();

        userService.updateUserProfile(email, userRequest);
    }

    @DeleteMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserProfile(Authentication authentication){
        String email = authentication.getName();
        userService.deleteUserProfile(email);

    }

    @GetMapping("/profile/addresses")
    @ResponseStatus(HttpStatus.OK)
    public List<Address> getAddresses(Authentication authentication){
        String email = authentication.getName();

        return userService.getAddresses(email);
    }

    @PostMapping("/profile/addresses")
    @ResponseStatus(HttpStatus.OK)
    public void addAddress(Authentication authentication, @RequestBody AddressRequest addressRequest){
        String email = authentication.getName();

        userService.addAddress(email, addressRequest);
    }

}
