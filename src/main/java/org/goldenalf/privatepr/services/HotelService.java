package org.goldenalf.privatepr.services;

import lombok.RequiredArgsConstructor;
import org.goldenalf.privatepr.models.Hotel;
import org.goldenalf.privatepr.repositories.HotelRepositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HotelService {
    private final HotelRepositories hotelRepositories;

    @Transactional
    public void save(Hotel hotel) {
        hotel.getRoomList().forEach(room -> room.setHotel(hotel));
        hotelRepositories.save(hotel);
    }

    @Transactional
    public void delete(int id) {
        hotelRepositories.deleteById(id);
    }

    @Transactional
    public void update(int id, Hotel hotelByUpdate) {
        hotelByUpdate.setId(id);
        hotelRepositories.save(hotelByUpdate);
    }


    @Transactional(readOnly = true)
    public Optional<Hotel> getHotel(int id) {
        return hotelRepositories.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Hotel> getAllHotels() {
        return hotelRepositories.findAll();
    }

    @Transactional(readOnly = true)
    public List<Hotel> findByName(String name) {
        return hotelRepositories.findByName(name);
    }
}
