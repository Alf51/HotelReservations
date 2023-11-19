package org.goldenalf.privatepr.utils.erorsHandler.validator;

import lombok.AllArgsConstructor;
import org.goldenalf.privatepr.models.Hotel;
import org.goldenalf.privatepr.models.Room;
import org.goldenalf.privatepr.services.impl.RoomServiceImpl;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Component
@AllArgsConstructor
public class RoomValidator implements Validator {
    private final RoomServiceImpl roomService;
    private final MessageSource messageSource;

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
                return;
            } else if (Objects.equals(curentRoom.getRoomNumber(), room.getRoomNumber())) {
                errors.rejectValue("roomNumber", "410",  messageSource
                        .getMessage("validation.hotelBook.room.exception.room-exist", null, Locale.getDefault()));
            }
        }
    }
}
