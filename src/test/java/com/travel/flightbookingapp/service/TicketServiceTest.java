package com.travel.flightbookingapp.service;

import com.travel.flightbookingapp.entity.*;
import com.travel.flightbookingapp.exception.ResourceNotFoundException;
import com.travel.flightbookingapp.repository.FlightRepository;
import com.travel.flightbookingapp.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

public class TicketServiceTest {

    @InjectMocks
    private TicketService service;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private FlightRepository flightRepository;

    private Route route;
    private Airport istanbul;
    private Airport ankara;
    private Airline thy;

    private Ticket ticket;

    private Flight flightFromIstanbulToAnkara;

    @BeforeEach
    public void setup() {
        istanbul = new Airport("Istanbul");
        ankara = new Airport("Ankara");

        route = new Route(istanbul, ankara);
        thy = new Airline("Turkish Airlines");

        flightFromIstanbulToAnkara = new Flight(1L, thy, route, new BigDecimal("10.00"), 100, 100);

        ticket = new Ticket(flightFromIstanbulToAnkara, "4221161122330005");

        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void whenGetExistingTicketThenReturnTicket() {
        Mockito.when(ticketRepository.findById(1L))
                .thenReturn(Optional.of(ticket));

        assertThat(service.getTicket(1L)).isEqualTo(ticket);
    }

    @Test
    public void whenGetNonExistingTicketThenThrowResourceNotFoundException() {
        Mockito.when(ticketRepository.findById(1L))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.getTicket(1L);
        });

        String expectedMessage = "Could not find ticket with id: 1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void whenPurchaseTicketThenReturnPurchasedTicket() {
        Mockito.when(flightRepository.findById(1L))
                .thenReturn(Optional.of(flightFromIstanbulToAnkara));
        Mockito.when(ticketRepository.save(ticket))
                .thenReturn(ticket);

        assertThat(service.purchaseTicket(ticket))
                .isEqualTo(ticket);
    }

    @Test
    public void whenPurchaseTicketForNonExistingFlightThrowResourceNotFoundException() {
        Mockito.when(flightRepository.findById(1L))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.purchaseTicket(ticket);
        });

        String expectedMessage = "Could not find flight with id: 1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void whenCancelTicketVerifyDelete() {
        Mockito.when(ticketRepository.findById(1L))
                .thenReturn(Optional.of(ticket));
        Mockito.when(flightRepository.findById(1L))
                .thenReturn(Optional.of(flightFromIstanbulToAnkara));
        Mockito.when(flightRepository.save(flightFromIstanbulToAnkara))
                .thenReturn(flightFromIstanbulToAnkara);

        service.cancelTicket(1L);

        Mockito.verify(ticketRepository, times(1)).deleteById(1L);


    }

    @Test
    public void whenCancelTicketThenVerifyRemainingSeats() {
        int remainingSeats = flightFromIstanbulToAnkara.getRemainingSeats();
        Mockito.when(ticketRepository.findById(1L))
                .thenReturn(Optional.of(ticket));
        Mockito.when(flightRepository.findById(1L))
                .thenReturn(Optional.of(flightFromIstanbulToAnkara));
        Mockito.when(flightRepository.save(flightFromIstanbulToAnkara))
                .thenReturn(flightFromIstanbulToAnkara);

        service.cancelTicket(1L);

        ArgumentCaptor<Flight> captor = ArgumentCaptor.forClass(Flight.class);
        Mockito.verify(flightRepository, times(1)).save(captor.capture());

        Flight actual = captor.getValue();
        assertEquals(remainingSeats + 1, actual.getRemainingSeats());
    }

    @Test
    public void whenPurchaseTicketThenVerifyRemainingSeats() {
        int remainingSeats = flightFromIstanbulToAnkara.getRemainingSeats();
        Mockito.when(flightRepository.findById(1L))
                .thenReturn(Optional.of(flightFromIstanbulToAnkara));
        Mockito.when(ticketRepository.save(ticket))
                .thenReturn(ticket);
        service.purchaseTicket(ticket);

        assertEquals(remainingSeats - 1, ticket.getFlight().getRemainingSeats());
    }

    @Test
    public void whenCancelNonExistingTicketThrowResourceNotFoundException() {
        Mockito.when(ticketRepository.findById(1L))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.cancelTicket(1L);
        });

        String expectedMessage = "Could not find ticket with id: 1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}
