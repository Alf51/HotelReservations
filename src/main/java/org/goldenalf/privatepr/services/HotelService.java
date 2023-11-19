package org.goldenalf.privatepr.services;

import org.goldenalf.privatepr.models.Hotel;

import java.util.List;
import java.util.Optional;

public interface HotelService {
    void save(Hotel hotel);

    void delete(int id);

    void update(int id, Hotel hotelByUpdate);
    Optional<Hotel> getHotel(int id);

    List<Hotel> getAllHotels();

    List<Hotel> findByName(String name);
}
