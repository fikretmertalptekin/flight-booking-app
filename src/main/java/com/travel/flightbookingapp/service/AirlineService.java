package com.travel.flightbookingapp.service;

import com.travel.flightbookingapp.entity.Airline;
import com.travel.flightbookingapp.entity.Flight;
import com.travel.flightbookingapp.entity.Route;
import com.travel.flightbookingapp.exception.ResourceAlreadyExistsException;
import com.travel.flightbookingapp.exception.ResourceNotFoundException;
import com.travel.flightbookingapp.repository.AirlineRepository;
import com.travel.flightbookingapp.repository.FlightRepository;
import com.travel.flightbookingapp.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AirlineService {

    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private AirlineRepository airlineRepository;
    @Autowired
    private RouteRepository routeRepository;

    public List<Airline> findAll() {
        return airlineRepository.findAll();
    }

    @Transactional
    public Airline addAirline(Airline airline) {
        if (!airlineRepository.findByName(airline.getName()).isPresent())
            return airlineRepository.save(airline);
        else
            throw new ResourceAlreadyExistsException("Airline " + airline.getName());
    }

    public Airline getAirline(Long id) {
        return airlineRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("airline with id", id.toString()));
    }

    @Transactional
    public Airline updateAirline(Airline newAirline, Long id) {

        return airlineRepository.findById(id)
                .map(airline -> {
                    airline.setName(newAirline.getName());
                    return airlineRepository.save(airline);
                })
                .orElseGet(() -> {
                    newAirline.setId(id);
                    return airlineRepository.save(newAirline);
                });
    }

    public void deleteAirline(Long id) {
        airlineRepository.deleteById(id);
    }

    @Transactional
    public Flight addFlightForAirline(Long id, Flight flight) {
        Airline airline = airlineRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("airline", flight.getAirline().getName()));
        Route route = routeRepository.findByFromAndTo(flight.getRoute().getFrom(), flight.getRoute().getTo()).orElseThrow(
                () -> new ResourceNotFoundException("route from-to", flight.getRoute().getFrom().getName() + " " + flight.getRoute().getTo().getName()));
        flight.setAirline(airline);
        flight.setRoute(route);
        if (flight.getRemainingSeats() == 0)
            flight.setRemainingSeats(flight.getInitialCapacity());


        return flightRepository.save(flight);
    }

}
