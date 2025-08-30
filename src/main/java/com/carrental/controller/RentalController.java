package com.carrental.controller;

import com.carrental.dto.RentalRequestDTO;
import com.carrental.dto.RentalResponseDTO;
import com.carrental.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rentals")
@RequiredArgsConstructor
public class RentalController {
    private final RentalService rentalService;

    @PostMapping
    public RentalResponseDTO rentCar(@RequestBody RentalRequestDTO request) {
        return rentalService.rentCar(request);
    }

    @PutMapping("/{rentalId}/return")
    public RentalResponseDTO returnCar(@PathVariable Long rentalId) {
        return rentalService.returnCar(rentalId);
    }
}