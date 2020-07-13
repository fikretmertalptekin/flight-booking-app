package com.travel.flightbookingapp.service;

import com.travel.flightbookingapp.entity.Airport;
import com.travel.flightbookingapp.entity.Route;
import com.travel.flightbookingapp.exception.ResourceAlreadyExistsException;
import com.travel.flightbookingapp.exception.ResourceNotFoundException;
import com.travel.flightbookingapp.repository.AirportRepository;
import com.travel.flightbookingapp.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class RouteService {

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private AirportRepository airportRepository;

    public List<Route> findAll() {
        return routeRepository.findAll();
    }

    @Transactional
    public Route addRoute(Route route) {

        Airport fromAirport = airportRepository.findByName(route.getFrom().getName()).orElseThrow(
                () -> new ResourceNotFoundException("airport", route.getFrom().getName()));
        Airport toAirport = airportRepository.findByName(route.getTo().getName()).orElseThrow(
                () -> new ResourceNotFoundException("airport", route.getTo().getName()));
        route.setFrom(fromAirport);
        route.setTo(toAirport);
        if (routeRepository.existsByFromAndTo(route.getFrom(), route.getTo())) {
            throw new ResourceAlreadyExistsException("Route from " + route.getFrom().getName() + " to " + route.getTo().getName());
        }

        return routeRepository.save(route);
    }

    public Route getRoute(Long id) {
        return routeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("route with id", id.toString()));
    }

    @Transactional
    public Route updateRoute(Route newRoute, Long id) {

        return routeRepository.findById(id)
                .map(route -> {
                    route.setFrom(newRoute.getFrom());
                    route.setTo(newRoute.getTo());
                    return routeRepository.save(route);
                })
                .orElseGet(() -> {
                    newRoute.setId(id);
                    return routeRepository.save(newRoute);
                });
    }

    public void deleteRoute(Long id) {
        routeRepository.deleteById(id);
    }
}
