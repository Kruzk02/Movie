package com.app.Entity;

import org.springframework.data.annotation.Id;

import java.util.Date;

public class Actor {

    @Id
    private Long id;
    private String first_name;
    private String last_name;
    private Nationality nationality;
    private Date birthDate;

    public Actor() {
    }

    public Actor(Long id, String first_name, String last_name, Nationality nationality, Date birthDate) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.nationality = nationality;
        this.birthDate = birthDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Nationality getNationality() {
        return nationality;
    }

    public void setNationality(Nationality nationality) {
        this.nationality = nationality;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}
