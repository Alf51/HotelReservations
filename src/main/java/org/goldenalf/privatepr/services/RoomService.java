package org.goldenalf.privatepr.services;

import lombok.RequiredArgsConstructor;
import org.goldenalf.privatepr.models.Room;
import org.goldenalf.privatepr.repositories.RoomRepositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepositories roomRepositories;
    private final HotelService hotelService;

    @Transactional
    public void save(Room room) {
        roomRepositories.save(room);
    }


    @Transactional
    public void delete(int id) {
        roomRepositories.deleteById(id);
    }

    @Transactional
    //TODO добавить валидацию
    public void update(int id, Room roomByUpdate) {
        Optional<Room> room = roomRepositories.findById(id);

        if (room.isPresent()) {
            roomByUpdate.setHotel(room.get().getHotel());
            roomByUpdate.setId(id);
            roomRepositories.save(roomByUpdate);
        }
    }

    @Transactional(readOnly = true)
    public Optional<Room> getRoom(int id) {
        return roomRepositories.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Room> findAllRoomsByHotelId(int hotelId) {
        return roomRepositories.findAllByHotelId(hotelId);
    }

    @Transactional(readOnly = true)
    public List<Room> getAllRooms() {
        return roomRepositories.findAll();
    }
}
