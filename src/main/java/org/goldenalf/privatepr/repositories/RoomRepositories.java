package org.goldenalf.privatepr.repositories;

import org.goldenalf.privatepr.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RoomRepositories extends JpaRepository<Room, Integer> {
    List<Room> findAllByHotelId (int hotelId);
}
