package org.example.userservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.userservice.dto.AddressRequest;
import org.example.userservice.dto.UserProfileResponse;
import org.example.userservice.dto.UserRequest;
import org.example.userservice.models.Address;
import org.example.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public UserProfileResponse getUserProfileByEmail(String email) {
        //TODO Implement logic, checks for user, if match, return
        return null;

    }

    public void updateUserProfile(String email, UserRequest userRequest) {
        // TODO Implement update logic, checks for user, if match update

    }

    public void deleteUserProfile(String email) {
        // TODO implement logic, Check for user, if match delete
    }

    public List<Address> getAddresses(String email) {
        //TODO Check for user, if match, list addresses
        return null;
    }

    public void addAddress(String email, AddressRequest addressRequest) {
        //TODO check for user, if match add address to List, remember bi-directional relationship
    }

    public void updateAddress(Long id, String email, AddressRequest addressRequest) {
        //TODO check if address if matches user email, if yes, update
    }

    public void deleteAddress(Long id, String email) {
        //TODO check if address matches user email, if yes, delete
    }
}
