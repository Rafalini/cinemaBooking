package com.cinema.integrationTests;

import com.cinema.model.Reservation;
import com.cinema.model.TemporalReservation;
import com.cinema.model.TemporalSeatList;
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
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReservationControllerTest {
    @Value(value="${local.server.port}")
    private int port;

    private LocalDateTime time;
    DateTimeFormatter dateFormatter;
    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    public void setTime(){
        this.time = LocalDateTime.now();
        this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    @Test
    @Order(1)
    public void addNew() throws Exception {
        Integer screeningId = (Integer)(( (LinkedHashMap<String,Object>)(this.restTemplate.getForObject("http://localhost:" + port + "/screening/all?date="+this.time.format(this.dateFormatter), List.class).get(0))).get("id"));
        TemporalReservation newReservation = new TemporalReservation("TestName", "TestSurname", "13", Long.valueOf(screeningId));
        assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/reservation/add", newReservation, String.class))
                .contains("TestName")
                .contains("TestSurname")
                .contains("13");
    }

    @Test
    @Order(2)
    public void getAll() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/reservation/all", String.class))
                .contains("TestName")
                .contains("TestSurname")
                .contains("13");
    }

    @Test
    @Order(3)
    public void getById() throws Exception {
        Integer reservationId = (Integer)(( (LinkedHashMap<String,Object>)(this.restTemplate.getForObject("http://localhost:" + port + "/reservation/all", List.class).get(0))).get("id"));
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/reservation/"+reservationId,
                String.class)).contains(reservationId.toString());
    }

    @Test
    @Order(4)
    public void addSeats() throws Exception {
        Integer reservationId = (Integer)(( (LinkedHashMap<String,Object>)(this.restTemplate.getForObject("http://localhost:" + port + "/reservation/all", List.class).get(0))).get("id"));
        TemporalSeatList newSeatList = new TemporalSeatList(Long.valueOf(reservationId), Arrays.asList(1,2,3));
        assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/reservation/add-seats/", newSeatList,
                String.class))
                .contains("\"seatNumber\":1")
                .contains("\"seatNumber\":2")
                .contains("\"seatNumber\":3");
    }

    @Test
    @Order(5)
    public void getSeats() throws Exception {
        String confirmationUrl = (String)(( (LinkedHashMap<String,Object>)(this.restTemplate.getForObject("http://localhost:" + port + "/reservation/all", List.class).get(0))).get("url"));
        //No data is sent back, only 200 http status code
        assertNull((this.restTemplate.getForObject("http://localhost:" + port+"/reservation"+confirmationUrl, String.class)));
    }
}
