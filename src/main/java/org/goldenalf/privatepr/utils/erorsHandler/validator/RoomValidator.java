package org.goldenalf.privatepr.utils.erorsHandler.validator;

import lombok.AllArgsConstructor;
import org.goldenalf.privatepr.models.Hotel;
import org.goldenalf.privatepr.models.Room;
import org.goldenalf.privatepr.services.RoomService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
@AllArgsConstructor
public class RoomValidator implements Validator {
    private final RoomService roomService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Room.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Room room = (Room) target;
        Hotel hotel = room.getHotel();

        List<Room> allRooms = roomService.findAllRoomsByHotelId(hotel.getId());

        for (Room curentRoom : allRooms) {
            if (curentRoom.equals(room)) {
                //Значит комната уже существует и мы производим обновление
                break;
            } else if (curentRoom.getRoomNumber() == room.getRoomNumber()) {
                errors.rejectValue("roomNumber", "410", "комната с данным номером уже существует");
            }
        }
    }
}
