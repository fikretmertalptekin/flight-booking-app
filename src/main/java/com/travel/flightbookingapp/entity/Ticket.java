package com.travel.flightbookingapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TICKET_NUMBER")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "flight_fk", referencedColumnName = "flight_number")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnoreProperties({ "ticketPrice", "initialCapacity" , "remainingSeats" })
    private Flight flight;

    private String purchasedCardNumber;

    protected Ticket() {
    }

    public Ticket(Flight flight, String purchasedCardNumber) {
        this.flight = flight;
        this.purchasedCardNumber = purchasedCardNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public String getPurchasedCardNumber() {
        return purchasedCardNumber;
    }

    public void setPurchasedCardNumber(String purchasedCardNumber) {
        this.purchasedCardNumber = purchasedCardNumber;
    }
}
