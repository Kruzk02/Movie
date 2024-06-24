package com.app.DTO;

import com.app.Entity.Nationality;

import java.time.LocalDate;

public class UserDTO {
    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private Nationality nationality;
    private LocalDate birthDate;

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Nationality getNationality() {
        return nationality;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }
}
