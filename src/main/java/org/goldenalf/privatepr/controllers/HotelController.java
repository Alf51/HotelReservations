package org.goldenalf.privatepr.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.goldenalf.privatepr.dto.HotelDto;
import org.goldenalf.privatepr.models.Hotel;
import org.goldenalf.privatepr.services.HotelService;
import org.goldenalf.privatepr.utils.HotelValidator;
import org.goldenalf.privatepr.utils.erorsHandler.ErrorHandler;
import org.goldenalf.privatepr.utils.erorsHandler.HotelErrorException;
import org.goldenalf.privatepr.utils.erorsHandler.HotelErrorResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/hotel")
@RequiredArgsConstructor
public class HotelController {
    private final HotelService hotelService;
    private final ModelMapper modelMapper;
    private final HotelValidator hotelValidator;

    @GetMapping("/{id}")
    public HotelDto getHotel(@PathVariable("id") int id) {
        return convertToHotelDto(hotelService.getHotel(id).orElseThrow(() -> new HotelErrorException("Отель не найден")));
    }

    @PostMapping("/new")
    public ResponseEntity<HttpStatus> saveHotel(@RequestBody @Valid HotelDto hotelDto,
                                                BindingResult bindingResult) {
        Hotel hotel = convertToHotel(hotelDto);
        hotelValidator.validate(hotel, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new HotelErrorException(ErrorHandler.getErrorMessage(bindingResult));
        }

        hotelService.save(hotel);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> updateHotel(@PathVariable("id") int id,
                                                  @RequestBody @Valid HotelDto hotelDto,
                                                  BindingResult bindingResult) {
        Hotel hotel = convertToHotel(hotelDto);
        hotel.setId(id);
        hotelValidator.validate(hotel, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new HotelErrorException(ErrorHandler.getErrorMessage(bindingResult));
        }

        hotelService.update(id, hotel);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteHotel(@PathVariable("id") int id) {
        hotelService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<HotelErrorResponse> handleException(HotelErrorException e) {
        HotelErrorResponse personErrorResponse = new HotelErrorResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(personErrorResponse, HttpStatus.BAD_REQUEST);
    }

    private HotelDto convertToHotelDto(Hotel hotel) {
        return modelMapper.map(hotel, HotelDto.class);
    }

    private Hotel convertToHotel(HotelDto hotel) {
        return modelMapper.map(hotel, Hotel.class);
    }
}
