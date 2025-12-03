package org.example.userservice.error;

public class AddressNotFoundException extends RuntimeException {
    public AddressNotFoundException(String msg)  {
        super(msg);
    }
}
