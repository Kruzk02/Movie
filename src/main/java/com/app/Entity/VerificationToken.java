package com.app.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table("verification_token")
public class VerificationToken {

    @Id
    private Long id;
    private String token;
    private Long userId;
    private LocalDate expireDate;

    public VerificationToken() {
    }

    public VerificationToken(Long id, String token, Long userId, LocalDate expireDate) {
        this.id = id;
        this.token = token;
        this.userId = userId;
        this.expireDate = expireDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }
}
