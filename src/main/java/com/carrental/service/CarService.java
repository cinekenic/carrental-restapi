package com.carrental.service;

import com.carrental.exception.ResourceNotFoundException;
import com.carrental.model.CarEntity;
import com.carrental.model.CarStatus;
import com.carrental.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;

    public List<CarEntity> getAllCars() {
        return carRepository.findAll();
    }

    public CarEntity addCar(CarEntity car) {
        car.setStatus(CarStatus.DOSTEPNY);
        return carRepository.save(car);
    }

    public CarEntity changeStatus(Long id, CarStatus status) {
        CarEntity car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id " + id));
        car.setStatus(status);
        return carRepository.save(car);
    }
}