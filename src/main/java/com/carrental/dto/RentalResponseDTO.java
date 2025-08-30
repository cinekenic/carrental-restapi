package com.carrental.dto;

import java.time.LocalDateTime;

public record RentalResponseDTO(Long rentalId, Long carId, Long clientId, LocalDateTime rentalDate, LocalDateTime returnDate) {}