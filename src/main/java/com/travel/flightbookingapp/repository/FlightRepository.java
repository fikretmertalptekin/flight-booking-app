package com.travel.flightbookingapp.repository;

import com.travel.flightbookingapp.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository  extends JpaRepository<Flight, Long> {
}
