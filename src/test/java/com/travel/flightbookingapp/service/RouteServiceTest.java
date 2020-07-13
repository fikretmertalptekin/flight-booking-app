package com.travel.flightbookingapp.service;

import com.travel.flightbookingapp.entity.Airport;
import com.travel.flightbookingapp.entity.Route;
import com.travel.flightbookingapp.exception.ResourceAlreadyExistsException;
import com.travel.flightbookingapp.exception.ResourceNotFoundException;
import com.travel.flightbookingapp.repository.AirportRepository;
import com.travel.flightbookingapp.repository.RouteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RouteServiceTest {

    @InjectMocks
    private RouteService service;

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private AirportRepository airportRepository;

    private Route route;
    private Airport istanbul;
    private Airport ankara;

    @BeforeEach
    public void setup() {
        istanbul = new Airport("Istanbul");
        ankara = new Airport("Ankara");

        route = new Route(istanbul, ankara);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenFindAllReturnAllRoutes() {
        Airport istanbul = new Airport("Istanbul");
        Airport ankara = new Airport("Ankara");

        Route route1 = new Route(istanbul, ankara);
        Route route2 = new Route(ankara, istanbul);
        List<Route> routes = new ArrayList<>();
        routes.add(route1);
        routes.add(route2);
        Mockito.when(routeRepository.findAll())
                .thenReturn(routes);

        assertThat(service.findAll())
                .isEqualTo(routes);
    }

    @Test
    public void whenGetExistingRouteThenReturnRoute() {
        Mockito.when(routeRepository.findById(1L))
                .thenReturn(Optional.of(route));

        assertThat(service.getRoute(1L))
                .isEqualTo(route);
    }

    @Test
    public void whenGetNonExistingRouteThenThrowResourceNotFoundException() {
        Mockito.when(routeRepository.findById(1L))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.getRoute(1L);
        });

        String expectedMessage = "Could not find route with id: 1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void whenAddNotExistingRouteThenReturnAddedRoute() {
        Mockito.when(airportRepository.findByName("Istanbul"))
                .thenReturn(Optional.of(istanbul));
        Mockito.when(airportRepository.findByName("Ankara"))
                .thenReturn(Optional.of(ankara));
        Mockito.when(routeRepository.existsByFromAndTo(route.getFrom(), route.getTo()))
                .thenReturn(false);
        Mockito.when(routeRepository.save(route))
                .thenReturn(route);

        assertThat(service.addRoute(route))
                .isEqualTo(route);
    }

    @Test
    public void whenAddExistingRouteThrowResourceAlreadyExistsException() {
        Mockito.when(airportRepository.findByName("Istanbul"))
                .thenReturn(Optional.of(istanbul));
        Mockito.when(airportRepository.findByName("Ankara"))
                .thenReturn(Optional.of(ankara));
        Mockito.when(routeRepository.existsByFromAndTo(route.getFrom(), route.getTo()))
                .thenReturn(true);

        Exception exception = assertThrows(ResourceAlreadyExistsException.class, () -> {
            service.addRoute(route);
        });

        String expectedMessage = "Route from Istanbul to Ankara already exists";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void whenAddNonExistingFromAirportThrowResourceNotFoundException() {
        Mockito.when(airportRepository.findByName("Istanbul"))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.addRoute(route);
        });

        String expectedMessage = "Could not find airport: Istanbul";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
