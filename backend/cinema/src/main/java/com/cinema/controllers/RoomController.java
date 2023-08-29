package com.cinema.controllers;

import com.cinema.model.Room;
import com.cinema.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/all")
    public List<Room> getAllReservations(){
        return roomService.findAllRooms();
    }

    @PostMapping("/add")
    public Room addRoom(@RequestBody Room room){
        return roomService.addRoom(room);
    }
}
