package com.app.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DirectorDTO {

    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String nationality;
    private String photo;
}
