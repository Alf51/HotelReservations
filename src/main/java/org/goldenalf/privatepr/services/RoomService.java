package org.goldenalf.privatepr.services;

import lombok.RequiredArgsConstructor;
import org.goldenalf.privatepr.dto.BookDateDto;
import org.goldenalf.privatepr.dto.RoomDto;
import org.goldenalf.privatepr.models.Book;
import org.goldenalf.privatepr.models.Room;
import org.goldenalf.privatepr.repositories.RoomRepository;
import org.goldenalf.privatepr.utils.erorsHandler.validator.BookValidator;
import org.goldenalf.privatepr.utils.exeptions.RoomErrorException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public void save(Room room) {
        roomRepository.save(room);
    }


    @Transactional
    public void delete(int id) {
        if (getRoom(id).isPresent()) {
            roomRepository.deleteById(id);
        } else {
            throw new RoomErrorException("Комната не найдена");
        }
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
    public Optional<Room> getRoom(long id) {
        return roomRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Room> findAllRoomsByHotelId(int hotelId) {
        return roomRepository.findAllByHotelId(hotelId);
    }


    @Transactional(readOnly = true)
    public List<RoomDto> findAllFreeRoomsInHotelForGivenDate(BookDateDto bookDateDto) {
        List<Room> roomList = roomRepository.findAllByHotelId(bookDateDto.getHotelId())
                .stream()
                .filter(room -> isRoomAvailableForGivenDates(room, bookDateDto.getCheckIn(), bookDateDto.getCheckOut()))
                .toList();
        return convertToRoomDtoList(roomList);
    }

    //TODO такой код есть в RoomController - сделать сделать стат метод с дженерика в util (ConvetrerDTO или тип того)
    private RoomDto convertToRoomDto(Room room) {
        return modelMapper.map(room, RoomDto.class);
    }

    private List<RoomDto> convertToRoomDtoList(List<Room> room) {
        Type listType = new TypeToken<List<RoomDto>>() {
        }.getType();
        return modelMapper.map(room, listType);
    }

    private boolean isRoomAvailableForGivenDates(Room room, LocalDate checkIn, LocalDate checkOut) {
        List<Book> bookList = room.getBookList();
        boolean isRoomAvailable = true;
        for (Book existingBook : bookList) {
            if (!BookValidator.isFreePeriodBetweenExistingDates(existingBook.getCheckIn(), existingBook.getCheckOut(), checkIn, checkOut)) {
                return false;
            }
        }
        return isRoomAvailable;
    }
}
