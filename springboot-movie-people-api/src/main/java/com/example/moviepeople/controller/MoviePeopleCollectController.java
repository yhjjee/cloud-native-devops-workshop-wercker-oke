package com.example.moviepeople.controller;

import java.util.Optional;

import com.example.moviepeople.jpa.MoviePeopleRepository;
import com.example.moviepeople.repository.entity.Movie_People;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
@CrossOrigin("*")
public class MoviePeopleCollectController {

    @Autowired
    MoviePeopleRepository moviePeopleRepository;

    Logger logger = LoggerFactory.getLogger(MoviePeopleCollectController.class);

    @RequestMapping(value = "/moviepeople", method = RequestMethod.GET)
    public Page<Movie_People> findMoviePeople(Pageable pageable) throws Exception {
        Page<Movie_People> moviepeople = moviePeopleRepository.findAll(pageable);
        return moviepeople;
    }

    @RequestMapping(value = "/moviepeople/{id}", method = RequestMethod.GET)
    public Optional<Movie_People> findMoviePeopleById(@PathVariable(value = "id") int id) throws Exception {
        Optional<Movie_People> moviepeople = moviePeopleRepository.findById(id);
        return moviepeople;
    }

    @RequestMapping(value = "/moviepeople/name/{name}", method = RequestMethod.GET)
    public Optional<Movie_People> findMoviePeopleByName(@PathVariable(value = "name") String name) throws Exception {
        Optional<Movie_People> moviepeople = moviePeopleRepository.findByName(name);
        return moviepeople;
    }

    @RequestMapping(value = "/moviepeople/filmography/{filmography}", method = RequestMethod.GET)
    public Page<Movie_People> findMoviePeopleByFilmographyLike(Pageable pageable, @PathVariable(value = "filmography") String filmography) throws Exception {
        Page<Movie_People> moviepeople = moviePeopleRepository.findByFilmographyLike(pageable, filmography);
        return moviepeople;
    }

    
    public static void main(String[] args) {
        SpringApplication.run(MoviePeopleCollectController.class, args);
    }
}