package com.travel.flightbookingapp.service;

import com.travel.flightbookingapp.entity.Airline;
import com.travel.flightbookingapp.exception.ResourceAlreadyExistsException;
import com.travel.flightbookingapp.exception.ResourceNotFoundException;
import com.travel.flightbookingapp.repository.AirlineRepository;
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

public class AirlineServiceTest {

    @InjectMocks
    private AirlineService service;

    @Mock
    private AirlineRepository repository;

    private Airline thy;

    @BeforeEach
    public void setup() {
        thy = new Airline("Turkish Airlines");
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenFindAllReturnAllAirlines() {
        Airline thy = new Airline("Turkish Airlines");
        Airline pegasus = new Airline("Pegasus");
        List<Airline> airlines = new ArrayList<>();
        airlines.add(thy);
        airlines.add(pegasus);
        Mockito.when(repository.findAll())
                .thenReturn(airlines);

        assertThat(service.findAll())
                .isEqualTo(airlines);
    }

    @Test
    public void whenGetExistingAirlineThenReturnAirline() {
        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(thy));

        assertThat(service.getAirline(1L))
                .isEqualTo(thy);
    }

    @Test
    public void whenGetNonExistingAirlineThenThrowResourceNotFoundException() {
        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.getAirline(1L);
        });

        String expectedMessage = "Could not find airline with id: 1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void whenAddNotExistingAirlineThenReturnAddedAirline() {
        Mockito.when(repository.findByName("Turkish Airlines"))
                .thenReturn(Optional.empty());
        Mockito.when(repository.save(thy))
                .thenReturn(thy);

        assertThat(service.addAirline(thy))
                .isEqualTo(thy);
    }

    @Test
    public void whenAddExistingAirlineThrowResourceAlreadyExistsException() {
        Mockito.when(repository.findByName("Turkish Airlines"))
                .thenReturn(Optional.of(thy));

        Exception exception = assertThrows(ResourceAlreadyExistsException.class, () -> {
            service.addAirline(thy);
        });

        String expectedMessage = "Airline Turkish Airlines already exists";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
