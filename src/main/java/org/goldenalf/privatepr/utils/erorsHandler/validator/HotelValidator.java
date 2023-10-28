package org.goldenalf.privatepr.utils.erorsHandler.validator;

import lombok.AllArgsConstructor;
import org.goldenalf.privatepr.models.Hotel;
import org.goldenalf.privatepr.services.HotelService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
@AllArgsConstructor
public class HotelValidator implements Validator {
    private final HotelService hotelService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Hotel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Hotel hotel = (Hotel) target;
        List<Hotel> listHotelWithSameName = hotelService.findByName(hotel.getName());

        if (!listHotelWithSameName.isEmpty()) {
            for (Hotel hotelWithSameName : listHotelWithSameName) {
                if (hotelWithSameName.equals(hotel)) {
                    //Значит объект уже существует и мы производим обновление
                    break;
                } else if (hotelWithSameName.getAddress().equals(hotel.getAddress())) {
                    errors.rejectValue("address", "409", "отель с данным именем и по данному адресу уже зарегистрирован");
                    break;
                }
            }
        }
    }
}
