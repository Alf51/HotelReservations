package org.goldenalf.privatepr.services.impl;

import lombok.RequiredArgsConstructor;
import org.goldenalf.privatepr.models.Hotel;
import org.goldenalf.privatepr.repositories.HotelRepository;
import org.goldenalf.privatepr.services.HotelService;
import org.goldenalf.privatepr.utils.erorsHandler.ErrorHandler;
import org.goldenalf.privatepr.utils.exeptions.HotelErrorException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;
    private final ErrorHandler errorHandler;

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
            throw new HotelErrorException(errorHandler
                    .getErrorMessage("validation.hotelBook.hotel.exception.hotel-not-found"));
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
