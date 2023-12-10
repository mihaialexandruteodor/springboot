package com.booking.recruitment.hotel.controller;

import com.booking.recruitment.hotel.model.Hotel;
import com.booking.recruitment.hotel.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotel")
public class HotelController {
  private final HotelService hotelService;

  @Autowired
  public HotelController(HotelService hotelService) {
    this.hotelService = hotelService;
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<Hotel> getAllHotels() {
    return hotelService.getAllHotels();
  }

  @GetMapping("/getAllHotelsNotDeleted")
  @ResponseStatus(HttpStatus.OK)
  public List<Hotel> getAllHotelsNotDeleted() {
    return hotelService.getAllHotelsNotDeleted(false);
  }

  @GetMapping("/getHotel/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Hotel getHotelById(@PathVariable Long id) {
    return hotelService.getHotelById(id);
  }

  @GetMapping("/getHotelsClosestToCenter/{centerLat}/{centerLong}")
  @ResponseStatus(HttpStatus.OK)
  public List<Hotel> getHotelsClosestToCenter(@PathVariable Long centerLat, @PathVariable Long centerLong) {
    return hotelService.getHotelsClosestToCenter(centerLat, centerLong);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Hotel createHotel(@RequestBody Hotel hotel) {
    return hotelService.createNewHotel(hotel);
  }

  @DeleteMapping("/deleteHotel/{id}")
   @ResponseStatus(HttpStatus.OK)
  public String deleteHotel(@PathVariable Long id)
  {
      return hotelService.deleteHotelById(id);
  }
}
