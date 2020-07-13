package com.travel.flightbookingapp.repository;

import com.travel.flightbookingapp.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
