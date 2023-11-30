package org.goldenalf.privatepr.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.goldenalf.privatepr.dto.BookDateDto;
import org.goldenalf.privatepr.dto.BookDto;
import org.goldenalf.privatepr.dto.RoomDto;
import org.goldenalf.privatepr.models.Hotel;
import org.goldenalf.privatepr.models.Room;
import org.goldenalf.privatepr.services.impl.HotelServiceImpl;
import org.goldenalf.privatepr.services.impl.RoomServiceImpl;
import org.goldenalf.privatepr.utils.erorsHandler.ErrorHandler;
import org.goldenalf.privatepr.utils.exeptions.HotelErrorException;
import org.goldenalf.privatepr.utils.exeptions.RoomErrorException;
import org.goldenalf.privatepr.utils.erorsHandler.validator.RoomValidator;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;


@Controller
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomServiceImpl roomService;
    private final HotelServiceImpl hotelService;
    private final RoomValidator roomValidator;
    private final ErrorHandler errorHandler;
    private final ModelMapper modelMapper;

    @GetMapping("/{id_room}")
    public String getRoom(@PathVariable("id_room") int id, Model model) {
        Room room = roomService.getRoom(id).orElseThrow(() -> new RoomErrorException(errorHandler
                .getErrorMessage("validation.hotelBook.room.exception.room-not-found")));
        RoomDto roomDto = convertToRoomDto(room);
        model.addAttribute("room", roomDto);
        model.addAttribute("book", new BookDto());
        return "room/room";
    }

    @GetMapping("/{id_hotel}/allRooms")
    public String getAllRoomsInHotel(@PathVariable("id_hotel") int hotelId, Model model) {
        List<RoomDto> roomDtoList = convertToRoomDtoList(roomService.findAllRoomsByHotelId(hotelId));
        model.addAttribute("roomList", roomDtoList);
        return "room/room-all";
    }

    @GetMapping("/{id_hotel}/search-rooms")
    public String getSearchRoomPage(@PathVariable("id_hotel") int hotelId, Model model) {
        BookDateDto bookDateDto = new BookDateDto();
        bookDateDto.setHotelId(hotelId);
        model.addAttribute("bookDate", bookDateDto);
        return "room/search-rooms";
    }

    @PutMapping(value = "/roomsForGivenDate", params = "action=freeRoom")
    public String getAllAvailableRoomsInHotelForGivenDate(@ModelAttribute("bookDate") @Valid BookDateDto bookDateDto, Model model) {
        List<RoomDto> roomDtoList = roomService.findAllRoomsInHotelForGivenDate(bookDateDto, true);
        model.addAttribute("roomList", roomDtoList);
        return "room/room-by-date";
    }

    @PutMapping(value = "/roomsForGivenDate", params = "action=bookRoom")
    public String getAllBookedRoomsInHotelForGivenDate(@ModelAttribute("bookDate") @Valid BookDateDto bookDateDto, Model model) {
        List<RoomDto> roomDtoList = roomService.findAllRoomsInHotelForGivenDate(bookDateDto, false);
        model.addAttribute("roomList", roomDtoList);
        return "room/room-by-date";
    }

    @PostMapping("/{id_hotel}/new")
    public ResponseEntity<HttpStatus> saveRoom(@RequestBody @Valid RoomDto roomDto,
                                               @PathVariable("id_hotel") int hotelId,
                                               BindingResult bindingResult) {

        Hotel hotel = hotelService.getHotel(hotelId).orElseThrow(() -> new HotelErrorException(errorHandler.
                getErrorMessage("validation.hotelBook.hotel.exception.hotel-not-found")));
        Room room = convertToRoom(roomDto);
        room.setHotel(hotel);

        roomValidator.validate(room, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new RoomErrorException(errorHandler.getErrorMessage(bindingResult));
        }

        roomService.save(room);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id_room}")
    public ResponseEntity<HttpStatus> updateRoom(@PathVariable("id_room") int id,
                                                 @RequestBody @Valid RoomDto roomDto,
                                                 BindingResult bindingResult) {
        Room updatedRoom = convertToRoom(roomDto);
        Room room = roomService.getRoom(id).orElseThrow(() -> new RoomErrorException(errorHandler
                .getErrorMessage("validation.hotelBook.room.exception.room-not-found")));

        updatedRoom.setHotel(room.getHotel());
        updatedRoom.setId(room.getId());
        roomValidator.validate(updatedRoom, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new RoomErrorException(errorHandler.getErrorMessage(bindingResult));
        }

        roomService.update(id, updatedRoom);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id_room}")
    public String deleteRoom(@PathVariable("id_room") int id) {
        roomService.delete(id);
        return "hotel/hotel-all";
    }


    private RoomDto convertToRoomDto(Room room) {
        return modelMapper.map(room, RoomDto.class);
    }

    private List<RoomDto> convertToRoomDtoList(List<Room> room) {
        Type listType = new TypeToken<List<RoomDto>>() {
        }.getType();
        return modelMapper.map(room, listType);
    }

    private Room convertToRoom(RoomDto room) {
        return modelMapper.map(room, Room.class);
    }
}
