package com.booking.recruitment.hotel.service.impl;

import com.booking.recruitment.hotel.exception.BadRequestException;
import com.booking.recruitment.hotel.model.Hotel;
import com.booking.recruitment.hotel.repository.HotelRepository;
import com.booking.recruitment.hotel.service.HotelService;
import com.booking.recruitment.utils.Haversine;

import jakarta.persistence.EntityManager;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

@Service
class DefaultHotelService implements HotelService {
  private final HotelRepository hotelRepository;

  @Autowired
  DefaultHotelService(HotelRepository hotelRepository) {
    this.hotelRepository = hotelRepository;
  }

  @Autowired
  private EntityManager entityManager;

  @Override
  public List<Hotel> getAllHotels() {
    return hotelRepository.findAll();
  }

  
  @Override
  public List<Hotel> getAllHotelsNotDeleted(boolean isDeleted) {
    Session session = entityManager.unwrap(Session.class);
    Filter filter = session.enableFilter("deletedHotelsFilter");
    filter.setParameter("isDeleted", isDeleted);
    List<Hotel> hotels = hotelRepository.findAll();
    session.disableFilter("deletedHotelsFilter");
    return hotels;
  }

  @Override
  public List<Hotel> getHotelsByCity(Long cityId) {
    return hotelRepository.findAll().stream()
        .filter((hotel) -> cityId.equals(hotel.getCity().getId()))
        .collect(Collectors.toList());
  }

  @Override
  public Hotel getHotelById(Long hotelId) {
    Hotel hotel = hotelRepository.getReferenceById(hotelId);
    if(!hotel.isDeleted())
      return hotel;
    else
      return null;
  }

  @Override
  public Hotel createNewHotel(Hotel hotel) {
    if (hotel.getId() != null) {
      throw new BadRequestException("The ID must not be provided when creating a new Hotel");
    }

    return hotelRepository.save(hotel);
  }

  @Override
  public String deleteHotelById(Long hotelId)
    {
        hotelRepository.deleteById(hotelId);
        return "Hotel with id " + hotelId + " has been deleted!";
    }

  @Override
  public List<Hotel> getHotelsClosestToCenter(Long centerLat, Long centerLong)
  {
    int n = 3;
    PriorityQueue<Map.Entry<Double, Hotel>> queue = new PriorityQueue<Map.Entry<Double, Hotel>>(Map.Entry.comparingByKey(Comparator.reverseOrder()));;

      List<Hotel> hotelCandidatesList = hotelRepository.findAll();
      for(Hotel candidate : hotelCandidatesList)
      {
          Double dist = Haversine.distance(candidate.getLatitude(), candidate.getLongitude(), centerLat, centerLong);
          queue.offer(new AbstractMap.SimpleEntry<>(dist, candidate));
      }

      return new ArrayList(queue);
  }
}

class Pair implements Comparable<Pair> {
    Double key;
    Hotel value;

    public Pair(Double key, Hotel value) {
        this.key = key;
        this.value = value;
    }

    public Double getkey()
    {
      return key;
    }

    @Override
    public int compareTo(Pair arg0) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'compareTo'");
    }
}
