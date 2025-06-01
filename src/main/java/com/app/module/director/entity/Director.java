package com.app.module.director.entity;

import com.app.type.Nationality;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table("director")
public class Director {
    @Id
    @Column("id")
    private Long id;
    @Column("first_name")
    private String firstName;
    @Column("last_name")
    private String lastName;
    @Column("birth_date")
    private LocalDate birthDate;
    @Column("nationality")
    private Nationality nationality;
    @Column("photo")
    private String photo;
}
