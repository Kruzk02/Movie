package com.app.module.user.dto;

import com.app.type.Nationality;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private Nationality nationality;
    private LocalDate birthDate;
}
