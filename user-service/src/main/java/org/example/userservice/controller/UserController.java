package org.example.userservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.userservice.dto.AddressRequest;
import org.example.userservice.dto.UserProfileResponse;
import org.example.userservice.dto.UserRequest;
import org.example.userservice.models.Address;
import org.example.userservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserProfileResponse getUserProfileById(@PathVariable Long id){
        return null;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateUserProfile(@PathVariable Long id, @RequestBody UserRequest userRequest){

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserProfile(@PathVariable Long id){

    }

    @GetMapping("/{id}/addresses")
    @ResponseStatus(HttpStatus.OK)
    public List<Address> getAddresses(@PathVariable Long id){
        return null;
    }

    @PostMapping("/{id}/addresses")
    @ResponseStatus(HttpStatus.OK)
    public void addAddress(@PathVariable Long id, @RequestBody AddressRequest addressRequest){

    }

}
