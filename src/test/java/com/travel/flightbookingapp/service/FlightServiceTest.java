package com.travel.flightbookingapp.service;

import com.travel.flightbookingapp.entity.Airline;
import com.travel.flightbookingapp.entity.Airport;
import com.travel.flightbookingapp.entity.Flight;
import com.travel.flightbookingapp.entity.Route;
import com.travel.flightbookingapp.exception.ResourceNotFoundException;
import com.travel.flightbookingapp.repository.AirlineRepository;
import com.travel.flightbookingapp.repository.FlightRepository;
import com.travel.flightbookingapp.repository.RouteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FlightServiceTest {

    @InjectMocks
    private FlightService service;

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private AirlineRepository airlineRepository;

    @Mock
    private FlightRepository flightRepository;

    private Route route;
    private Airport istanbul;
    private Airport ankara;
    private Airline thy;

    private Flight flightFromIstanbulToAnkara;

    @BeforeEach
    public void setup() {
        istanbul = new Airport("Istanbul");
        ankara = new Airport("Ankara");

        route = new Route(istanbul, ankara);
        thy = new Airline("Turkish Airlines");

        flightFromIstanbulToAnkara = new Flight(thy, route, new BigDecimal("10.00"), 100, 100);

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenFindAllReturnAllFlights() {
        Airport istanbul = new Airport("Istanbul");
        Airport ankara = new Airport("Ankara");

        Route route = new Route(istanbul, ankara);
        Airline thy = new Airline("Turkish Airlines");
        Airline pegasus = new Airline("Pegasus");

        Flight flight1 = new Flight(thy, route, new BigDecimal("10.00"), 100, 100);
        Flight flight2 = new Flight(pegasus, route, new BigDecimal("20.00"), 150, 150);

        List<Flight> flights = new ArrayList<>();
        flights.add(flight1);
        flights.add(flight2);
        Mockito.when(flightRepository.findAll())
                .thenReturn(flights);

        assertThat(service.findAll())
                .isEqualTo(flights);
    }

    @Test
    public void whenGetExistingFlightThenReturnFlight() {
        Mockito.when(flightRepository.findById(1L))
                .thenReturn(Optional.of(flightFromIstanbulToAnkara));

        assertThat(service.getFlight(1L))
                .isEqualTo(flightFromIstanbulToAnkara);
    }

    @Test
    public void whenGetNonExistingFlightThenThrowResourceNotFoundException() {
        Mockito.when(flightRepository.findById(1L))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.getFlight(1L);
        });

        String expectedMessage = "Could not find flight with id: 1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void whenAddFlightThenReturnAddedFlight() {
        Mockito.when(airlineRepository.findByName("Turkish Airlines"))
                .thenReturn(Optional.of(thy));
        Mockito.when(routeRepository.findByFromAndTo(istanbul, ankara))
                .thenReturn(Optional.of(route));
        Mockito.when(flightRepository.save(flightFromIstanbulToAnkara))
                .thenReturn(flightFromIstanbulToAnkara);

        assertThat(service.addFlight(flightFromIstanbulToAnkara))
                .isEqualTo(flightFromIstanbulToAnkara);
    }

    @Test
    public void whenAddNonExistingFromAirportThrowResourceNotFoundException() {
        Mockito.when(airlineRepository.findByName("Turkish Airlines"))
                .thenReturn(Optional.of(thy));
        Mockito.when(routeRepository.findByFromAndTo(istanbul, ankara))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.addFlight(flightFromIstanbulToAnkara);
        });

        String expectedMessage = "Could not find route from: Istanbul";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void whenAddNonExistingAirlineThrowResourceNotFoundException() {
        Mockito.when(airlineRepository.findByName(thy.getName()))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.addFlight(flightFromIstanbulToAnkara);
        });

        String expectedMessage = "Could not find airline: " + thy.getName();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
