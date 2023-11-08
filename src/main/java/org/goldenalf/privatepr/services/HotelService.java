package org.goldenalf.privatepr.services;

import lombok.RequiredArgsConstructor;
import org.goldenalf.privatepr.models.Hotel;
import org.goldenalf.privatepr.repositories.HotelRepository;
import org.goldenalf.privatepr.utils.exeptions.HotelErrorException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HotelService {
    private final HotelRepository hotelRepository;

    @Transactional
    public void save(Hotel hotel) {
        hotel.getRoomList().forEach(room -> room.setHotel(hotel));
        hotelRepository.save(hotel);
    }

    @Transactional
    public void delete(int id) {
        if (getHotel(id).isPresent()) {
            hotelRepository.deleteById(id);
        } else {
            throw new HotelErrorException("Отель не найден");
        }
    }

    @Transactional
    public void update(int id, Hotel hotelByUpdate) {
        hotelByUpdate.setId(id);
        hotelRepository.save(hotelByUpdate);
    }


    @Transactional(readOnly = true)
    public Optional<Hotel> getHotel(int id) {
        return hotelRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Hotel> findByName(String name) {
        return hotelRepository.findByName(name);
    }
}
