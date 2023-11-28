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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;


@Controller
@RequestMapping("/hotel")
@RequiredArgsConstructor
public class HotelController {
    private final HotelServiceImpl hotelService;
    private final ModelMapper modelMapper;
    private final ErrorHandler errorHandler;
    private final HotelValidator hotelValidator;

    @GetMapping("/{id_hotel}")
    public String getHotel(@PathVariable("id_hotel") int id, Model model) {
        HotelDto hotelDto = convertToHotelDto(hotelService.getHotel(id).orElseThrow(() -> new HotelErrorException(errorHandler
                .getErrorMessage("validation.hotelBook.hotel.exception.hotel-not-found"))));
        model.addAttribute("hotel", hotelDto);
        return "/hotel/hotel";
    }

    @GetMapping("/all")
    public String getAllHotel(Model model) {
        List<HotelDto> hotelDtoList = convertToHotelDtoList(hotelService.getAllHotels());
        model.addAttribute("hotelList", hotelDtoList);

        return "/hotel/hotel-all";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        HotelDto hotelDto = convertToHotelDto(hotelService.getHotel(id).orElseThrow(() -> new HotelErrorException(errorHandler
                .getErrorMessage("validation.hotelBook.hotel.exception.hotel-not-found"))));

        model.addAttribute("hotel", hotelDto);
        return "hotel/edit";
    }

    @GetMapping("/new")
    public String newPersonCreate(@ModelAttribute("hotel") HotelDto hotelDto) {
        return "hotel/new";
    }


    @PostMapping("/new")
    public String saveHotel(@ModelAttribute("hotel") @Valid HotelDto hotelDto,
                                                BindingResult bindingResult) {
        Hotel hotel = convertToHotel(hotelDto);
        hotelValidator.validate(hotel, bindingResult);

        if (bindingResult.hasErrors()) {
            return "hotel/new";
        }
        hotelService.save(hotel);
        return "redirect:/hotel/all";
    }

    @PatchMapping("/{id_hotel}")
    public String updateHotel(@PathVariable("id_hotel") int id,
                              @ModelAttribute("hotel") @Valid HotelDto hotelDto,
                              BindingResult bindingResult) {

        Hotel hotel = convertToHotel(hotelDto);
        hotel.setId(id);
        hotelValidator.validate(hotel, bindingResult);

        if (bindingResult.hasErrors()) {
            hotelDto.setId(id);
            return "/hotel/edit";
        }

        hotelService.update(id, hotel);
        return "redirect:/hotel/all";
    }

    @DeleteMapping("/{id_hotel}")
    public String deleteHotel(@PathVariable("id_hotel") int id) {
        hotelService.delete(id);
        return "redirect:/hotel/all";
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
