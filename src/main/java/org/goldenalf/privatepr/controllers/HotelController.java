package org.goldenalf.privatepr.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.goldenalf.privatepr.dto.HotelDto;
import org.goldenalf.privatepr.models.Hotel;
import org.goldenalf.privatepr.services.HotelService;
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

    @GetMapping("/{id}")
    public HotelDto getHotel(@PathVariable("id") int id) {
        return convertToHotelDto(hotelService.getHotel(id).get());
    }

    @PostMapping("/new")
    //TODO обработать ошибку
    public ResponseEntity<HttpStatus> saveHotel(@RequestBody @Valid HotelDto hotelDto,
                                                BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new RuntimeException();
        }

        hotelService.save(convertToHotel(hotelDto));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    //TODO обработать ошибку
    public ResponseEntity<HttpStatus> updateHotel(@PathVariable("id") int id,
                                                  @RequestBody @Valid HotelDto hotelDto,
                                                  BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new RuntimeException();
        }

        hotelService.update(id, convertToHotel(hotelDto));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    //TODO обработать ошибку, если пользователь не найден ... хотя мб лишнее
    public ResponseEntity<HttpStatus> deleteHotel(@PathVariable("id") int id) {
        hotelService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private HotelDto convertToHotelDto(Hotel hotel) {
        return modelMapper.map(hotel, HotelDto.class);
    }

    private Hotel convertToHotel(HotelDto hotel) {
        return modelMapper.map(hotel, Hotel.class);
    }
}
