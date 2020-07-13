package com.travel.flightbookingapp.controller;

import com.travel.flightbookingapp.entity.Airport;
import com.travel.flightbookingapp.service.AirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class AirportController {

    @Autowired
    private AirportService airportService;


    @GetMapping("/airports")
    CollectionModel<EntityModel<Airport>> findAll() {
        List<EntityModel<Airport>> airports = airportService.findAll().stream()
                .map(airport -> EntityModel.of(airport,
                        linkTo(methodOn(AirportController.class).getAirport(airport.getId())).withSelfRel(),
                        linkTo(methodOn(AirportController.class).findAll()).withRel("airports")))
                .collect(Collectors.toList());

        return CollectionModel.of(airports,
                linkTo(methodOn(AirportController.class).findAll()).withSelfRel());

    }

    @PostMapping("/airports")
    @ResponseStatus(HttpStatus.CREATED)
    EntityModel<Airport> addAirport(@RequestBody Airport airport) {
        Airport createdAirport = airportService.addAirport(airport);

        return EntityModel.of(createdAirport,
                linkTo(methodOn(AirportController.class).getAirport(createdAirport.getId())).withSelfRel(),
                linkTo(methodOn(AirportController.class).findAll()).withRel("airports"));
    }


    @GetMapping("/airports/{id}")
    EntityModel<Airport> getAirport(@PathVariable Long id) {
        Airport airport = airportService.getAirport(id);

        return EntityModel.of(airport,
                linkTo(methodOn(AirportController.class).getAirport(airport.getId())).withSelfRel(),
                linkTo(methodOn(AirportController.class).findAll()).withRel("airports"));
    }

    @PutMapping("/airports/{id}")
    EntityModel<Airport> updateAirport(@RequestBody Airport newAirport, @PathVariable Long id) {
        Airport airport = airportService.updateAirport(newAirport, id);

        return EntityModel.of(airport,
                linkTo(methodOn(AirportController.class).getAirport(airport.getId())).withSelfRel(),
                linkTo(methodOn(AirportController.class).findAll()).withRel("airports"));
    }

    @DeleteMapping("/airports/{id}")
    void deleteAirport(@PathVariable Long id) {
        airportService.deleteAirport(id);
    }
}
