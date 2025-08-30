package com.carrental.controller;

import com.carrental.dto.CarDTO;
import com.carrental.mapper.CarMapper;
import com.carrental.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @GetMapping
    public List<CarDTO> getAllCars() {
        return carService.getAllCars().stream()
                .map(CarMapper::toDto)
                .toList();
    }

    @PostMapping
    public CarDTO addCar(@RequestBody CarDTO carDto) {
        return CarMapper.toDto(carService.addCar(CarMapper.toEntity(carDto)));
    }

    @PutMapping("/{id}/status")
    public CarDTO changeStatus(@PathVariable Long id, @RequestParam String status) {
        return CarMapper.toDto(carService.changeStatus(id, status));
    }
}
