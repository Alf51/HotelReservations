package org.goldenalf.privatepr.services;

import org.goldenalf.privatepr.dto.BookDateDto;
import org.goldenalf.privatepr.dto.RoomDto;
import org.goldenalf.privatepr.models.Room;

import java.util.List;
import java.util.Optional;

public interface RoomService {
    void save(Room room);

    void delete(int id);

    void update(int id, Room roomByUpdate);

    Optional<Room> getRoom(long id);

    List<Room> findAllRoomsByHotelId(int hotelId);

    List<RoomDto> findAllRoomsInHotelForGivenDate(BookDateDto bookDateDto, boolean isAvailableRoom);
}
