package com.app.module.actor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActorDTO {
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String nationality;
    private String photo;
}
