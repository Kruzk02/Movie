package com.app.module.user.entity;

import com.app.type.Nationality;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("users")
public class User {
    @Id
    @Column("id")
    private Long id;
    @Column("username")
    private String username;
    @Column("email")
    private String email;
    @Column("password")
    private String password;
    @Column("phone_number")
    private String phoneNumber;
    @Column("nationality")
    private Nationality nationality;
    @Column("birth_date")
    private LocalDate birthDate;
    @Column("enabled")
    private boolean enabled;
}
