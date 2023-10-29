package org.goldenalf.privatepr.services;

import lombok.RequiredArgsConstructor;
import org.goldenalf.privatepr.models.Room;
import org.goldenalf.privatepr.repositories.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;

    @Transactional
    public void save(Room room) {
        roomRepository.save(room);
    }


    @Transactional
    public void delete(int id) {
        roomRepository.deleteById(id);
    }

    @Transactional
    //TODO добавить валидацию
    public void update(int id, Room roomByUpdate) {
        Optional<Room> room = roomRepository.findById(id);

        if (room.isPresent()) {
            roomByUpdate.setHotel(room.get().getHotel());
            roomByUpdate.setId(id);
            roomRepository.save(roomByUpdate);
        }
    }

    @Transactional(readOnly = true)
    public Optional<Room> getRoom(int id) {
        return roomRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Room> findAllRoomsByHotelId(int hotelId) {
        return roomRepository.findAllByHotelId(hotelId);
    }

    @Transactional(readOnly = true)
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
}
