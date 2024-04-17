package com.app.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Director {

    @Id
    private Long id;
    private String first_name;
    private String last_name;
    private Nationality nationality;
    private Date birthDate;
}
