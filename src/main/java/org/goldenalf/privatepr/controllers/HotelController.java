package org.goldenalf.privatepr.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.goldenalf.privatepr.dto.HotelDto;
import org.goldenalf.privatepr.models.Hotel;
import org.goldenalf.privatepr.services.impl.HotelServiceImpl;
import org.goldenalf.privatepr.utils.erorsHandler.validator.HotelValidator;
import org.goldenalf.privatepr.utils.erorsHandler.ErrorHandler;
import org.goldenalf.privatepr.utils.exeptions.HotelErrorException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;


@RestController
@RequestMapping("/hotel")
@RequiredArgsConstructor
public class HotelController {
    private final HotelServiceImpl hotelService;
    private final ModelMapper modelMapper;
    private final ErrorHandler errorHandler;
    private final HotelValidator hotelValidator;

    @GetMapping("/{id_hotel}")
    public HotelDto getHotel(@PathVariable("id_hotel") int id) {
        return convertToHotelDto(hotelService.getHotel(id).orElseThrow(() -> new HotelErrorException(errorHandler
                .getErrorMessage("validation.hotelBook.hotel.exception.hotel-not-found"))));
    }

    @GetMapping("/all")
    public List<HotelDto> getAllHotel() {
        return convertToHotelDtoList(hotelService.getAllHotels());
    }

    @PostMapping("/new")
    public ResponseEntity<HttpStatus> saveHotel(@RequestBody @Valid HotelDto hotelDto,
                                                BindingResult bindingResult) {
        Hotel hotel = convertToHotel(hotelDto);
        hotelValidator.validate(hotel, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new HotelErrorException(errorHandler.getErrorMessage(bindingResult));
        }
        hotelService.save(hotel);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id_hotel}")
    public ResponseEntity<HttpStatus> updateHotel(@PathVariable("id_hotel") int id,
                                                  @RequestBody @Valid HotelDto hotelDto,
                                                  BindingResult bindingResult) {
        Hotel hotel = convertToHotel(hotelDto);
        hotel.setId(id);
        hotelValidator.validate(hotel, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new HotelErrorException(errorHandler.getErrorMessage(bindingResult));
        }

        hotelService.update(id, hotel);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id_hotel}")
    public ResponseEntity<HttpStatus> deleteHotel(@PathVariable("id_hotel") int id) {
        hotelService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private HotelDto convertToHotelDto(Hotel hotel) {
        return modelMapper.map(hotel, HotelDto.class);
    }

    private Hotel convertToHotel(HotelDto hotel) {
        return modelMapper.map(hotel, Hotel.class);
    }

    private List<HotelDto> convertToHotelDtoList(List<Hotel> hotels) {
        Type listType = new TypeToken<List<HotelDto>>() {
        }.getType();
        return modelMapper.map(hotels, listType);
    }
}
