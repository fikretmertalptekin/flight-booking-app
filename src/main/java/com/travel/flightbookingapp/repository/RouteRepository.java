package com.travel.flightbookingapp.repository;

import com.travel.flightbookingapp.entity.Airport;
import com.travel.flightbookingapp.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RouteRepository  extends JpaRepository<Route, Long> {

    boolean existsByFromAndTo(Airport from, Airport to);

    Optional<Route> findByFromAndTo(Airport from, Airport to);
}
