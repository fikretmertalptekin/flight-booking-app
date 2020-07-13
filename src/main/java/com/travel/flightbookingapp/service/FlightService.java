package com.travel.flightbookingapp.service;

import com.travel.flightbookingapp.entity.Airline;
import com.travel.flightbookingapp.entity.Flight;
import com.travel.flightbookingapp.entity.Route;
import com.travel.flightbookingapp.exception.ResourceNotFoundException;
import com.travel.flightbookingapp.repository.AirlineRepository;
import com.travel.flightbookingapp.repository.FlightRepository;
import com.travel.flightbookingapp.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private AirlineRepository airlineRepository;
    @Autowired
    private RouteRepository routeRepository;

    public List<Flight> findAll() {
        return flightRepository.findAll();
    }

    @Transactional
    public Flight addFlight(Flight flight) {
        Airline airline = airlineRepository.findByName(flight.getAirline().getName()).orElseThrow(
                () -> new ResourceNotFoundException("airline", flight.getAirline().getName()));
        Route route = routeRepository.findByFromAndTo(flight.getRoute().getFrom(), flight.getRoute().getTo()).orElseThrow(
                () -> new ResourceNotFoundException("route from-to", flight.getRoute().getFrom().getName() + " " + flight.getRoute().getTo().getName()));
        flight.setAirline(airline);
        flight.setRoute(route);
        if (flight.getRemainingSeats() == 0)
            flight.setRemainingSeats(flight.getInitialCapacity());


        return flightRepository.save(flight);
    }

    public Flight getFlight(Long id) {
        return flightRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("flight with id", id.toString()));
    }

    public Flight updateFlight(Flight newFlight, Long id) {

        return flightRepository.findById(id)
                .map(flight -> {
                    flight.setAirline(newFlight.getAirline());
                    flight.setTicketPrice(newFlight.getTicketPrice());
                    return flightRepository.save(flight);
                })
                .orElseGet(() -> {
                    newFlight.setId(id);
                    return flightRepository.save(newFlight);
                });
    }

    public void deleteFlight(Long id) {
        flightRepository.deleteById(id);
    }
}
