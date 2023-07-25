package com.cinema.service;

import com.cinema.exceptions.CustomNotFoundException;
import com.cinema.model.Room;
import com.cinema.repository.RoomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class RoomService {
    private final RoomRepo roomRepo;
    @Autowired
    public RoomService(RoomRepo roomRepo) {
        this.roomRepo = roomRepo;
    }

    public Room addRoom(Room room){
        return roomRepo.save(room);
    }

    public List<Room> findAllRooms(){
        return roomRepo.findAll();
    }

    public Room findRoomById(Long id){
        return roomRepo.findRoomById(id)
                .orElseThrow(() -> new CustomNotFoundException("Room "+id+" was not found"));
    }
}
