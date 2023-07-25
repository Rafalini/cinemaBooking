package com.cinema.integrationTests;

import com.cinema.model.TemporalScreeningTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.annotation.Order;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ScreeningControllerTest {
    @Value(value="${local.server.port}")
    private int port;

    private LocalDateTime time;
    DateTimeFormatter timeFormatter;
    DateTimeFormatter dateFormatter;
    DateTimeFormatter dateTimeFormatter;
    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    public void setTime(){
        this.time = LocalDateTime.now();
        this.timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    }

    @Test
    @Order(1)
    public void addNew() throws Exception {
        TemporalScreeningTime newScreening = new TemporalScreeningTime(1L,1L, this.time);
        assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/screening/add", newScreening, String.class))
                .contains(this.time.format(this.timeFormatter));
    }

    @Test
    @Order(2)
    public void getAll() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/screening/all?date="+this.time.format(this.dateFormatter),
                String.class)).contains(this.time.format(this.dateTimeFormatter));
    }

    @Test
    @Order(3)
    public void getById() throws Exception {
        Integer screeningId = (Integer)(( (LinkedHashMap<String,Object>)(this.restTemplate.getForObject("http://localhost:" + port + "/screening/all?date="+this.time.format(this.dateFormatter),List.class).get(0))
                    ).get("id"));
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/screening/"+screeningId,
                String.class)).contains(screeningId.toString());
    }
}
