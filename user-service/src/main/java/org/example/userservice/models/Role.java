package org.example.userservice.models;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum Role {
    USER, ADMIN;


    public static Role toRole(String role) throws IllegalArgumentException {
        if(role.isBlank()){
            throw new NullPointerException("Role cannot be empty");
        }
        try{
            return Role.valueOf(role.toUpperCase());

        }catch(IllegalArgumentException e){
            log.error("Invalid Role Name",e);
            return USER;
        }
    }
}
