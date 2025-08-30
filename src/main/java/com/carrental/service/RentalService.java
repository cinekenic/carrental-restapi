package com.carrental.service;

import com.carrental.dto.RentalRequestDTO;
import com.carrental.dto.RentalResponseDTO;
import com.carrental.exception.ResourceNotFoundException;
import com.carrental.model.CarEntity;
import com.carrental.model.CarStatus;
import com.carrental.model.ClientEntity;
import com.carrental.model.RentalEntity;
import com.carrental.repository.CarRepository;
import com.carrental.repository.ClientRepository;
import com.carrental.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;
    private final CarRepository carRepository;
    private final ClientRepository clientRepository;


    public RentalResponseDTO rentCar(RentalRequestDTO request) {
        CarEntity car = carRepository.findById(request.carId())
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));
        if (car.getStatus() == CarStatus.WYPOZYCZONY) {
            throw new IllegalStateException("Car is already rented");
        }

        ClientEntity client = clientRepository.findById(request.clientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        car.setStatus(CarStatus.WYPOZYCZONY);
        carRepository.save(car);

        RentalEntity rental = RentalEntity.builder()
                .car(car)
                .client(client)
                .rentalDate(LocalDateTime.now())
                .build();

        RentalEntity saved = rentalRepository.save(rental);

        return new RentalResponseDTO(saved.getId(), car.getId(), client.getId(),
                saved.getRentalDate(), saved.getReturnDate());
    }

    public RentalResponseDTO returnCar(Long rentalId) {
        RentalEntity rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ResourceNotFoundException("Rental not found"));
        rental.setReturnDate(LocalDateTime.now());

        CarEntity car = rental.getCar();
        car.setStatus(CarStatus.DOSTEPNY);
        carRepository.save(car);

        RentalEntity saved = rentalRepository.save(rental);
        return new RentalResponseDTO(saved.getId(), car.getId(), rental.getClient().getId(),
                saved.getRentalDate(), saved.getReturnDate());
    }
}
