package com.travel.flightbookingapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name= "flights")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flight_number")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "airline", referencedColumnName = "name")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnoreProperties("flights")
    private Airline airline;

    @ManyToOne
    @JoinColumn(name = "route_fk", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Route route;

    private BigDecimal ticketPrice;

    private int initialCapacity;

    private int remainingSeats;

    protected Flight() {
    }

    public Flight(Long id, Airline airline, Route route, BigDecimal ticketPrice, int initialCapacity, int remainingSeats) {
        this.id = id;
        this.airline = airline;
        this.route = route;
        this.ticketPrice = ticketPrice;
        this.initialCapacity = initialCapacity;
        this.remainingSeats = remainingSeats;
    }

    public Flight(Airline airline, Route route, BigDecimal ticketPrice, int initialCapacity, int remainingSeats) {
        this.airline = airline;
        this.route = route;
        this.ticketPrice = ticketPrice;
        this.initialCapacity = initialCapacity;
        this.remainingSeats = remainingSeats;
    }

    public int getRemainingSeats() {
        return remainingSeats;
    }

    public void setRemainingSeats(int remainingSeats) {
        this.remainingSeats = remainingSeats;
    }

    public int getInitialCapacity() {
        return initialCapacity;
    }

    public void setInitialCapacity(int capacity) {
        this.initialCapacity = capacity;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Airline getAirline() {
        return airline;
    }

    public void setAirline(Airline airline) {
        this.airline = airline;
    }

    public BigDecimal getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(BigDecimal ticketPrice) {
        this.ticketPrice = ticketPrice;
    }
}
