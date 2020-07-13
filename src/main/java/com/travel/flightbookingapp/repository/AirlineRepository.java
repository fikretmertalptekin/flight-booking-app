package com.travel.flightbookingapp.repository;

import com.travel.flightbookingapp.entity.Airline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AirlineRepository extends JpaRepository<Airline, Long> {

    Optional<Airline> findByName(String name);

}
