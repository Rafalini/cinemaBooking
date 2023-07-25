package com.cinema.integrationTests;

import com.cinema.model.Movie;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MovieControllerTest {
    @Value(value="${local.server.port}")
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Order(1)
    public void addNew() throws Exception {
        Movie newMovie = new Movie("Java Adventures 7", "JA");
        assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/movie/add", newMovie, String.class))
                .contains("Java Adventures 7")
                .contains("JA");
    }

    @Test
    @Order(2)
    public void getAll() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/movie/all",
                String.class))
                .contains("Java")
                .contains("Adventures")
                .contains("7");
    }
}
