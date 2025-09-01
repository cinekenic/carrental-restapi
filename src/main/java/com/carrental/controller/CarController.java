package com.carrental.controller;

import com.carrental.model.CarEntity;
import com.carrental.model.CarStatus;
import com.carrental.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @GetMapping
    public List<CarEntity> getAllCars() {
        return carService.getAllCars();
    }

    @PostMapping
    public CarEntity addCar(@RequestBody CarEntity car) {
        return carService.addCar(car);
    }

    @PutMapping("/{id}/status")
    public CarEntity changeStatus(@PathVariable Long id, @RequestParam CarStatus status) {
        return carService.changeStatus(id, status);
    }
}
