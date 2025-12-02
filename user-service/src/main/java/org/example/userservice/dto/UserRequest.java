package org.example.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.userservice.models.Address;
import org.example.userservice.models.Role;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
