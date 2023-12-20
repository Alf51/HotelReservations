package org.goldenalf.privatepr.services.impl;

import org.goldenalf.privatepr.models.Hotel;
import org.goldenalf.privatepr.models.Room;
import org.goldenalf.privatepr.repositories.HotelRepository;
import org.goldenalf.privatepr.utils.erorsHandler.ErrorHandler;
import org.goldenalf.privatepr.utils.exeptions.HotelErrorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {
    @Mock
    private HotelRepository hotelRepository;
    @Mock
    private ErrorHandler errorHandler;
    @InjectMocks
    private HotelServiceImpl hotelService;

    @Test
    void save_saveHotelSuccess() {
        Hotel hotel = getHotel();

        hotelService.save(hotel);

        // Проверяем, что для каждого номера устанавливается отель
        hotel.getRoomList().forEach(room -> assertEquals(hotel, room.getHotel()));

        // Проверяем, что hotelRepository.save был вызван с правильным аргументом
        verify(hotelRepository).save(hotel);
    }

    @Test
    void delete_hotelExist_shouldDeleteHotel() {
        Hotel hotelInDB = getHotel();
        int hotelId = hotelInDB.getId();

        when(hotelService.getHotel(hotelId)).thenReturn(Optional.of(hotelInDB));
        doNothing().when(hotelRepository).deleteById(hotelId);

        hotelService.delete(hotelId);

        // deleteById вызывается ровно один раз с правильным id
        verify(hotelRepository, times(1)).deleteById(hotelId);
    }

    @Test
    void delete_HotelNotFound_shouldThrowException() {
        int hotelId = 1;

        when(hotelService.getHotel(hotelId)).thenReturn(Optional.empty());
        when(errorHandler.getErrorMessage(anyString())).thenReturn("Error message");

        assertThrows(HotelErrorException.class, () -> hotelService.delete(hotelId));

        //deleteById не должен вызываться
        verify(hotelRepository, never()).deleteById(anyInt());
    }

    @Test
    void update_forExistHotel_shouldUpdateHotel() {
        Hotel hotelByUpdate = getHotel();
        int hotelId = hotelByUpdate.getId();

        hotelService.update(hotelId, hotelByUpdate);

        // Проверяем, что id задан правильно
        assertEquals(hotelId, hotelByUpdate.getId());

        // Метод save должен вызываться ровно один раз при обновлении отеля
        verify(hotelRepository, times(1)).save(hotelByUpdate);
    }


    private Hotel getHotel() {
        Hotel hotel = new Hotel("Makrag", "Sistem Ulitima", 5, "Glory place");

        Room room1 = new Room(512, 7.62, true);
        room1.setId(1);
        room1.setHotel(hotel);

        Room room2 = new Room(256, 5.56, true);
        room2.setId(2);

        List<Room> roomList = List.of(room1, room2);
        hotel.setRoomList(roomList);
        room2.setHotel(hotel);
        return hotel;
    }
}
