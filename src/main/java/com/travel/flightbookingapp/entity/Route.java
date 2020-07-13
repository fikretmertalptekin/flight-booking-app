package com.travel.flightbookingapp.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.hateoas.server.core.Relation;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "routes", uniqueConstraints={
        @UniqueConstraint(columnNames = {"from_airport", "to_airport"})
})
@Relation(collectionRelation = "routes")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_airport", referencedColumnName = "name")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Airport from;

    @ManyToOne
    @JoinColumn(name = "to_airport", referencedColumnName = "name")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Airport to;

    protected Route() {
    }

    public Route(Airport from, Airport to) {
        this.from = from;
        this.to = to;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Airport getFrom() {
        return from;
    }

    public void setFrom(Airport from) {
        this.from = from;
    }

    public Airport getTo() {
        return to;
    }

    public void setTo(Airport to) {
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return from.equals(route.from) &&
                to.equals(route.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}
