package com.travel.flightbookingapp.repository;

import com.travel.flightbookingapp.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AirportRepository  extends JpaRepository<Airport, Long> {

    Optional<Airport> findByName(String name);

}
