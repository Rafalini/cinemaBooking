package com.cinema.integrationTests;

import com.cinema.model.Room;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.annotation.Order;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RoomControllerTest {
    @Value(value="${local.server.port}")
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Order(1)
    public void addNew() throws Exception {
        Room newRoom = new Room(1234,1234);
        assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/room/add", newRoom, String.class))
                .contains("1234");
    }

    @Test
    @Order(2)
    public void getAll() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/room/all",
                String.class)).contains("1234");
    }
}
