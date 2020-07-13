package com.travel.flightbookingapp.controller;

import com.travel.flightbookingapp.entity.Ticket;
import com.travel.flightbookingapp.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping("/tickets/{id}")
    EntityModel<Ticket> getTicket(@PathVariable Long id) {
        Ticket ticket = ticketService.getTicket(id);

        return EntityModel.of(ticket,
                linkTo(methodOn(TicketController.class).getTicket(ticket.getId())).withSelfRel());
    }

    @PostMapping("/tickets")
    @ResponseStatus(HttpStatus.CREATED)
    EntityModel<Ticket> purchaseTicket(@RequestBody Ticket ticket) {

        Ticket createdTicket = ticketService.purchaseTicket(ticket);

        return EntityModel.of(createdTicket,
                linkTo(methodOn(TicketController.class).getTicket(createdTicket.getId())).withSelfRel());
    }

    @DeleteMapping("/tickets/{id}")
    void cancelTicket(@PathVariable Long id) {
        ticketService.cancelTicket(id);

    }
}
