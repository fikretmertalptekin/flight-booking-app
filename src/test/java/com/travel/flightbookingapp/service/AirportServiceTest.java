package com.travel.flightbookingapp.service;

import com.travel.flightbookingapp.entity.Airport;
import com.travel.flightbookingapp.exception.ResourceAlreadyExistsException;
import com.travel.flightbookingapp.exception.ResourceNotFoundException;
import com.travel.flightbookingapp.repository.AirportRepository;
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

public class AirportServiceTest {

    @InjectMocks
    private AirportService service;

    @Mock
    private AirportRepository airportRepository;

    private Airport istanbul;

    @BeforeEach
    public void setup() {
        istanbul = new Airport("Istanbul");
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenFindAllReturnAllAirports() {
        Airport istanbul = new Airport("Istanbul");
        Airport ankara = new Airport("Ankara");
        List<Airport> airports = new ArrayList<>();
        airports.add(istanbul);
        airports.add(ankara);
        Mockito.when(airportRepository.findAll())
                .thenReturn(airports);

        assertThat(service.findAll())
                .isEqualTo(airports);
    }

    @Test
    public void whenGetExistingAirportThenReturnAirport() {
        Mockito.when(airportRepository.findById(1L))
                .thenReturn(Optional.of(istanbul));

        assertThat(service.getAirport(1L))
                .isEqualTo(istanbul);
    }

    @Test
    public void whenGetNonExistingAirportThenThrowResourceNotFoundException() {
        Mockito.when(airportRepository.findById(1L))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.getAirport(1L);
        });

        String expectedMessage = "Could not find airport with id: 1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void whenAddNotExistingAirportThenReturnAddedAirport() {
        Mockito.when(airportRepository.findByName("Istanbul"))
                .thenReturn(Optional.empty());
        Mockito.when(airportRepository.save(istanbul))
                .thenReturn(istanbul);

        assertThat(service.addAirport(istanbul))
                .isEqualTo(istanbul);
    }

    @Test
    public void whenAddExistingAirportThrowResourceAlreadyExistsException() {
        Mockito.when(airportRepository.findByName("Istanbul"))
                .thenReturn(Optional.of(istanbul));

        Exception exception = assertThrows(ResourceAlreadyExistsException.class, () -> {
            service.addAirport(istanbul);
        });

        String expectedMessage = "Airport Istanbul already exists";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
