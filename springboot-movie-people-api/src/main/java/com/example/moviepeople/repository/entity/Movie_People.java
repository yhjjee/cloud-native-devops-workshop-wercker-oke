package com.example.moviepeople.repository.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MOVIE_PEOPLE")
public class Movie_People implements Serializable {
    @Column(name = "ID")
    @Id
    private int id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ROLE")
    private String role;

    @Column(name = "FILMOGRAPHY")
    private String filmography;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Movie_People id(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Movie_People name(String name) {
        this.name = name;
        return this;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Movie_People role(String role) {
        this.role = role;
        return this;
    }

    public String getFilmography() {
        return this.filmography;
    }

    public void setFilmography(String filmography) {
        this.filmography = filmography;
    }

    public Movie_People filmography(String filmography) {
        this.filmography = filmography;
        return this;
    }
}