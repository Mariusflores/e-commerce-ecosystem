package org.example.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.userservice.dto.AddressRequest;
import org.example.userservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/addresses")
public class AddressController {

    private final UserService userService;

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateAddress(@PathVariable Long id, @RequestBody AddressRequest addressRequest){

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAddress(@PathVariable Long id){

    }
}
