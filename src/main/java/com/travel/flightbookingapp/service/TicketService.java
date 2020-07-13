package com.travel.flightbookingapp.service;

import com.travel.flightbookingapp.entity.Flight;
import com.travel.flightbookingapp.entity.Ticket;
import com.travel.flightbookingapp.exception.ResourceNotFoundException;
import com.travel.flightbookingapp.repository.FlightRepository;
import com.travel.flightbookingapp.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private FlightRepository flightRepository;

    public Ticket getTicket(Long id) {
        return ticketRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ticket with id", id.toString()));
    }

    @Transactional
    public Ticket purchaseTicket(Ticket ticket) {
        Flight flight = flightRepository.findById(ticket.getFlight().getId()).orElseThrow(
                () -> new ResourceNotFoundException("flight with id", ticket.getFlight().getId().toString()));
        // TO-DO add remaining seat check!!
        flight.setRemainingSeats(flight.getRemainingSeats() - 1);
        flight.setTicketPrice(updateTicketPrice(flight.getInitialCapacity(), flight.getRemainingSeats(), flight.getTicketPrice()));
        ticket.setFlight(flight);
        ticket.setPurchasedCardNumber(maskCardNumber(ticket.getPurchasedCardNumber()));
        return ticketRepository.save(ticket);
    }

    private BigDecimal updateTicketPrice(int initialCapacity, int remainingSeats, BigDecimal ticketPrice) {
        int tenPercentOfInitialCapacity = initialCapacity / 10;
        int remainder = initialCapacity % tenPercentOfInitialCapacity;

        if (remainingSeats % tenPercentOfInitialCapacity == remainder) {
            return ticketPrice.add(ticketPrice.divide(BigDecimal.TEN));
        } else {
            return ticketPrice;
        }

    }


    private String maskCardNumber(String purchasedCardNumber) {
        String reformattedCardNumber = purchasedCardNumber.replaceAll("[^0-9]", "");
        return reformattedCardNumber.substring(0, 6) + "******" + reformattedCardNumber.substring(12, reformattedCardNumber.length());

    }

    @Transactional
    public void cancelTicket(Long id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if (ticket.isPresent()) {
            Flight flight = ticket.get().getFlight();
            flight.setRemainingSeats(flight.getRemainingSeats() + 1);
            flightRepository.save(flight);
            ticketRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("ticket with id", id.toString());
        }

    }
}
