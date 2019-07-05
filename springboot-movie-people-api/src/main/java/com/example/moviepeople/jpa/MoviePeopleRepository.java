package com.example.moviepeople.jpa;

import java.util.Optional;

import com.example.moviepeople.repository.entity.Movie_People;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoviePeopleRepository extends JpaRepository<Movie_People, Integer> {
    public Optional<Movie_People> findById(String id);
    public Optional<Movie_People> findByName(String name);
    public Page<Movie_People> findByFilmographyLike(Pageable pageable, String filmography);
}