package com.travel.flightbookingapp.controller;

import com.travel.flightbookingapp.entity.Airline;
import com.travel.flightbookingapp.entity.Flight;
import com.travel.flightbookingapp.service.AirlineService;
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
@RequestMapping("/*")
public class AirlineController {


    @Autowired
    private AirlineService airlineService;

    @Autowired
    private FlightService flightService;

    @GetMapping("/airlines")
    CollectionModel<EntityModel<Airline>> findAll() {
        List<EntityModel<Airline>> airlines = airlineService.findAll().stream()
                .map(airline -> EntityModel.of(airline,
                        linkTo(methodOn(AirlineController.class).getAirline(airline.getId())).withSelfRel(),
                        linkTo(methodOn(AirlineController.class).findAll()).withRel("airlines")))
                .collect(Collectors.toList());

        return CollectionModel.of(airlines,
                linkTo(methodOn(AirlineController.class).findAll()).withSelfRel());

    }

    @PostMapping("/airlines")
    @ResponseStatus(HttpStatus.CREATED)
    EntityModel addAirline(@RequestBody Airline airline) {
        Airline createdAirline = airlineService.addAirline(airline);
        return EntityModel.of(createdAirline,
                linkTo(methodOn(AirlineController.class).getAirline(createdAirline.getId())).withSelfRel(),
                linkTo(methodOn(AirlineController.class).findAll()).withRel("airlines"));
    }

    @GetMapping("/airlines/{id}/flights")
    CollectionModel<EntityModel<Flight>> findFlightsForAirline(@PathVariable Long id) {
        List<EntityModel<Flight>> flights = airlineService.getAirline(id).getFlights().stream()
                .map(flight -> EntityModel.of(flight,
                        linkTo(methodOn(FlightController.class).getFlight(flight.getId())).withSelfRel(),
                        linkTo(methodOn(FlightController.class).findAll()).withRel("flights")))
                .collect(Collectors.toList());

        return CollectionModel.of(flights,
                linkTo(methodOn(FlightController.class).findAll()).withSelfRel());

    }

    @PostMapping("/airlines/{id}/flights")
    @ResponseStatus(HttpStatus.CREATED)
    EntityModel<Flight> addFlight(@PathVariable Long id, @RequestBody Flight flight) {
        Flight createdFlight = airlineService.addFlightForAirline(id, flight);

        return EntityModel.of(createdFlight,
                linkTo(methodOn(FlightController.class).getFlight(createdFlight.getId())).withSelfRel());
    }

    @GetMapping("/airlines/{id}")
    EntityModel<Airline> getAirline(@PathVariable Long id) {
        Airline airline = airlineService.getAirline(id);
        return EntityModel.of(airline,
                linkTo(methodOn(AirlineController.class).getAirline(id)).withSelfRel(),
                linkTo(methodOn(AirlineController.class).findAll()).withRel("airlines"));
    }

    @PutMapping("/airlines/{id}")
    EntityModel<Airline> updateAirline(@RequestBody Airline newAirline, @PathVariable Long id) {
        Airline airline = airlineService.updateAirline(newAirline, id);
        return EntityModel.of(airline,
                linkTo(methodOn(AirlineController.class).getAirline(id)).withSelfRel(),
                linkTo(methodOn(AirlineController.class).findAll()).withRel("airlines"));
    }

    @DeleteMapping("/airlines/{id}")
    void deleteAirline(@PathVariable Long id) {
        airlineService.deleteAirline(id);
    }
}
