package org.example.userservice.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.userservice.dto.address.AddressRequest;
import org.example.userservice.dto.address.AddressResponse;
import org.example.userservice.dto.user.UserProfileResponse;
import org.example.userservice.dto.user.UserRequest;
import org.example.userservice.error.AddressNotFoundException;
import org.example.userservice.error.UnauthorizedAccessException;
import org.example.userservice.models.Address;
import org.example.userservice.models.User;
import org.example.userservice.repository.AddressRepository;
import org.example.userservice.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public UserProfileResponse getUserProfileByEmail(String email) {

        User user = getUser(email);
        return mapToUserResponse(user);

    }

    @Transactional
    public void updateUserProfile(String email, UserRequest userRequest) {
        User user = getUser(email);
        boolean hasChanged = false;

        if(userRequest.getFirstName() != null && !userRequest.getFirstName().isBlank()) {
            user.setFirstName(userRequest.getFirstName());
            hasChanged = true;
            log.info("Changed First Name of User: {}", userRequest.getFirstName());
        }
        if(userRequest.getLastName() != null && !userRequest.getLastName().isBlank()) {
            user.setLastName(userRequest.getLastName());
            hasChanged = true;
            log.info("Changed Last Name of User: {}", userRequest.getLastName());
        }
        if(userRequest.getPhoneNumber() != null && !userRequest.getPhoneNumber().isBlank()) {
            user.setPhoneNumber(userRequest.getPhoneNumber());
            hasChanged = true;
            log.info("Changed Phone Number of User: {}", userRequest.getPhoneNumber());
        }

        if(hasChanged) {
            userRepository.save(user);
            log.info("User with email: {} has been updated", email);
        }
    }
    @Transactional
    public void deleteUserProfile(String email) {

        User user = getUser(email);
        userRepository.delete(user);
        log.info("User with email: {} has been deleted", email);
    }

    public List<AddressResponse> getAddresses(String email) {
        User user = getUser(email);
        List<Address> addresses = user.getAddresses();
        return addresses.stream().map(this::mapToAddressResponse).toList();
    }

    private AddressResponse mapToAddressResponse(Address address) {
        return AddressResponse.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .zipCode(address.getZipCode())
                .country(address.getCountry())
                .build();
    }

    @Transactional
    public void addAddress(String email, AddressRequest addressRequest) {
        User user = getUser(email);
        List<Address> addresses = user.getAddresses();

        Address newAddress = Address.builder()
                .street(addressRequest.getStreet())
                .city(addressRequest.getCity())
                .zipCode(addressRequest.getZipCode())
                .country(addressRequest.getCountry())
                .user(user)
                .build();

        addresses.add(newAddress);

        user.setAddresses(addresses);
        userRepository.save(user);
        addressRepository.save(newAddress);

    }
    @Transactional
    public void updateAddress(Long id, String email, AddressRequest addressRequest) {
        User user = getUser(email);
        Address address = addressRepository.findById(id).orElse(null);
        boolean hasChanged = false;

        if(address == null) {
            throw new AddressNotFoundException("Address not found");
        }
        if(!address.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("Address not tied to user");
        }

        if (addressRequest.getStreet() != null && !addressRequest.getStreet().isBlank()) {
            address.setStreet(addressRequest.getStreet());
            hasChanged = true;
        }
        if (addressRequest.getCity() != null && !addressRequest.getCity().isBlank()) {
            address.setCity(addressRequest.getCity());
            hasChanged = true;
        }
        if (addressRequest.getZipCode() != null && !addressRequest.getZipCode().isBlank()) {
            address.setZipCode(addressRequest.getZipCode());
            hasChanged = true;
        }
        if (addressRequest.getCountry() != null && !addressRequest.getCountry().isBlank()) {
            address.setCountry(addressRequest.getCountry());
            hasChanged = true;
        }

        if(hasChanged) {
            addressRepository.save(address);
            log.info("Address with email: {} has been updated", email);
        }

    }

    @Transactional
    public void deleteAddress(Long id, String email) {
        User user = getUser(email);
        Address address = addressRepository.findById(id).orElse(null);

        if(address == null) {
            throw new AddressNotFoundException("Address not found");
        }
        if(!address.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("Address not tied to user");
        }

        addressRepository.delete(address);
        log.info("Address with email: {} has been deleted", email);

    }

    /**
     * Helper Methods
     * */
    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User: " + email + " not found."));
    }

    private UserProfileResponse mapToUserResponse(User user) {
        return UserProfileResponse.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}
