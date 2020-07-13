package com.travel.flightbookingapp.controller;

import com.travel.flightbookingapp.entity.Route;
import com.travel.flightbookingapp.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class RouteController {


    @Autowired
    private RouteService routeService;

    @GetMapping("/routes")
    CollectionModel<EntityModel<Route>> findAll() {
        List<EntityModel<Route>> routes = routeService.findAll().stream()
                .map(route -> EntityModel.of(route,
                        linkTo(methodOn(RouteController.class).getRoute(route.getId())).withSelfRel(),
                        linkTo(methodOn(RouteController.class).findAll()).withRel("routes")))
                .collect(Collectors.toList());

        return CollectionModel.of(routes,
                linkTo(methodOn(FlightController.class).findAll()).withSelfRel());
    }

    @PostMapping("/routes")
    @ResponseStatus(HttpStatus.CREATED)
    EntityModel<Route> addRoute(@RequestBody Route route) {
        Route createdRoute = routeService.addRoute(route);

        return EntityModel.of(createdRoute,
                linkTo(methodOn(RouteController.class).getRoute(route.getId())).withSelfRel(),
                linkTo(methodOn(RouteController.class).findAll()).withRel("routes"));
    }

    @GetMapping("/routes/{id}")
    EntityModel<Route>  getRoute(@PathVariable Long id) {

        Route route = routeService.getRoute(id);

        return EntityModel.of(route,
                linkTo(methodOn(RouteController.class).getRoute(route.getId())).withSelfRel(),
                linkTo(methodOn(RouteController.class).findAll()).withRel("routes"));
    }

    @PutMapping("/routes/{id}")
    EntityModel<Route>  updateRoute(@RequestBody Route newRoute, @PathVariable Long id) {
        Route route = routeService.updateRoute(newRoute, id);

        return EntityModel.of(route,
                linkTo(methodOn(RouteController.class).getRoute(route.getId())).withSelfRel(),
                linkTo(methodOn(RouteController.class).findAll()).withRel("routes"));
    }

    @DeleteMapping("/routes/{id}")
    void deleteRoute(@PathVariable Long id) {
        routeService.deleteRoute(id);
    }
}
