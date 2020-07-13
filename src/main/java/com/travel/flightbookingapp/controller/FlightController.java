package com.travel.flightbookingapp.controller;

import com.travel.flightbookingapp.entity.Flight;
import com.travel.flightbookingapp.service.FlightService;
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
public class FlightController {

    @Autowired
    private FlightService flightService;

    @GetMapping("/flights")
    CollectionModel<EntityModel<Flight>> findAll() {
        List<EntityModel<Flight>> flights = flightService.findAll().stream()
                .map(flight -> EntityModel.of(flight,
                        linkTo(methodOn(FlightController.class).getFlight(flight.getId())).withSelfRel(),
                        linkTo(methodOn(FlightController.class).findAll()).withRel("flights")))
                .collect(Collectors.toList());

        return CollectionModel.of(flights,
                linkTo(methodOn(FlightController.class).findAll()).withSelfRel());
    }

    @PostMapping("/flights")
    @ResponseStatus(HttpStatus.CREATED)
    EntityModel<Flight> addFlight(@RequestBody Flight flight) {
        Flight createdFlight = flightService.addFlight(flight);

        return EntityModel.of(createdFlight,
                linkTo(methodOn(FlightController.class).getFlight(createdFlight.getId())).withSelfRel(),
                linkTo(methodOn(FlightController.class).findAll()).withRel("flights"));
    }

    @GetMapping("/flights/{id}")
    EntityModel<Flight> getFlight(@PathVariable Long id) {
        Flight flight = flightService.getFlight(id);

        return EntityModel.of(flight,
                linkTo(methodOn(FlightController.class).getFlight(flight.getId())).withSelfRel(),
                linkTo(methodOn(FlightController.class).findAll()).withRel("flights"));
    }

    @PutMapping("/flights/{id}")
    EntityModel<Flight> updateFlight(@RequestBody Flight newFlight, @PathVariable Long id) {
        Flight flight = flightService.updateFlight(newFlight, id);

        return EntityModel.of(flight,
                linkTo(methodOn(FlightController.class).getFlight(flight.getId())).withSelfRel(),
                linkTo(methodOn(FlightController.class).findAll()).withRel("flights"));
    }

    @DeleteMapping("/flights/{id}")
    void deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
    }
}
