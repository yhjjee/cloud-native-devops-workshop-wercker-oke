package com.example.moviepeople;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import com.example.moviepeople.jpa.MoviePeopleRepository;
import com.example.moviepeople.repository.entity.Movie_People;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Autowired
    MoviePeopleRepository moviePeopleRepository;

    @Test
    public void testMoviePeopleRepository(){

        Optional<Movie_People> moviePeopleById = moviePeopleRepository.findById(10016538);

        Optional<Movie_People> moviePeopleByName = moviePeopleRepository.findByName("로버트 다우니 주니어");

        assertThat(moviePeopleById.get().getName(), is("로버트 다우니 주니어"));
        assertThat(moviePeopleByName.get().getId(), is(10016538));
    }
}
