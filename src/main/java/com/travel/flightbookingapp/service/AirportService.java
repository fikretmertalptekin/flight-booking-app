package com.travel.flightbookingapp.service;

import com.travel.flightbookingapp.entity.Airport;
import com.travel.flightbookingapp.exception.ResourceAlreadyExistsException;
import com.travel.flightbookingapp.exception.ResourceNotFoundException;
import com.travel.flightbookingapp.repository.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class AirportService {

    @Autowired
    private AirportRepository repository;

    public List<Airport> findAll() {
        return repository.findAll();
    }

    @Transactional
    public Airport addAirport(Airport airport) {
        if (!repository.findByName(airport.getName()).isPresent())
            return repository.save(airport);
        else
            throw new ResourceAlreadyExistsException("Airport " + airport.getName());
    }

    public Airport getAirport(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("airport with id", id.toString()));
    }

    @Transactional
    public Airport updateAirport(Airport newAirport, Long id) {

        return repository.findById(id)
                .map(airport -> {
                    airport.setName(newAirport.getName());
                    return repository.save(airport);
                })
                .orElseGet(() -> {
                    newAirport.setId(id);
                    return repository.save(newAirport);
                });
    }

    public void deleteAirport(Long id) {
        repository.deleteById(id);
    }
}
