package com.cinema.unitTests;

import com.cinema.exceptions.ReservedSeatNotFoundException;
import com.cinema.exceptions.RoomNotFoundException;
import com.cinema.model.Room;
import com.cinema.repository.RoomRepo;
import com.cinema.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RoomServiceUnitTest {
    @Mock
    private RoomRepo roomRepo;
    @InjectMocks
    private RoomService roomService;

    @BeforeEach
    void setup() {
        roomRepo = Mockito.mock(RoomRepo.class);
        roomService = new RoomService(roomRepo);
    }

    @Test
    void addTest() {
        Room room = new Room();
        room.setSeatRows(5);
        room.setSeatsInRow(6);

        when(roomRepo.save(room)).thenReturn(room);
        Room returnedRoom = this.roomService.addRoom(room);

        assertEquals(room, returnedRoom);
        verify(this.roomRepo).save(room);
    }

    @Test
    void findAllTest() {
        Room room = new Room();
        room.setSeatRows(5);
        room.setSeatsInRow(6);

        when(roomRepo.findAll()).thenReturn(List.of(room));
        List<Room> rooms = this.roomService.findAllRooms();

        assertEquals(List.of(room), rooms);
        verify(this.roomRepo).findAll();
    }

    @Test
    void findByIdTest() {
        Room room = new Room();
        room.setId(2L);
        room.setSeatRows(5);
        room.setSeatsInRow(6);

        when(roomRepo.findRoomById(anyLong())).thenReturn(Optional.of(room));
        when(roomRepo.findRoomById(404L)).thenThrow(new RoomNotFoundException("Message"));
        Room returnedRoom = this.roomService.findRoomById(2L);

        assertEquals(returnedRoom, room);
        assertThrows(RoomNotFoundException.class , () -> {this.roomService.findRoomById(404L);});

        verify(this.roomRepo).findRoomById(2L);
        verify(this.roomRepo).findRoomById(404L);
    }
}
