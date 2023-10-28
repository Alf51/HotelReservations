package org.goldenalf.privatepr.controllers;


import lombok.RequiredArgsConstructor;
import org.goldenalf.privatepr.dto.RoomDto;

import org.goldenalf.privatepr.models.Room;
import org.goldenalf.privatepr.services.RoomService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;


@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    public RoomDto getRoom(@PathVariable("id") int id) {
        //TODO выбросить исключение
        Room room = roomService.getRoom(id).get();
        return convertToRoomDto(room);
    }

    @GetMapping("/{hotelId}/allRooms")
    public List<RoomDto> getAllRoomsInHotel(@PathVariable("hotelId") int hotelId) {
        //TODO выбросить исключение
        return convertToRoomDtoList(roomService.findAllByHotelId(hotelId));
    }

    @PostMapping("/{hotelId}/new")
    //TODO добавить валидацию
    public ResponseEntity<HttpStatus> saveRoom(@RequestBody RoomDto roomDto,
                                               @PathVariable("hotelId") int hotelId) {
        Room room = convertToRoom(roomDto);

        roomService.save(room, hotelId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    //TODO добавить валидацию (мб в сервис)
    public ResponseEntity<HttpStatus> updateRoom(@PathVariable("id") int id,
                                                  @RequestBody RoomDto roomDto) {
        Room room = convertToRoom(roomDto);

        roomService.update(id, room);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteRoom(@PathVariable("id") int id) {
        roomService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private RoomDto convertToRoomDto(Room room) {
        return  modelMapper.map(room, RoomDto.class);
    }

    private List<RoomDto> convertToRoomDtoList(List<Room> room) {
        Type listType = new TypeToken<List<RoomDto>>(){}.getType();
        return  modelMapper.map(room, listType);
    }

    private Room convertToRoom(RoomDto room) {
        return modelMapper.map(room, Room.class);
    }
}
