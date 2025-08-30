package com.carrental.controller;

import com.carrental.dto.ClientDTO;
import com.carrental.mapper.ClientMapper;
import com.carrental.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @GetMapping
    public List<ClientDTO> getAllClients() {
        return clientService.getAllClients().stream()
                .map(ClientMapper::toDto)
                .toList();
    }

    @PostMapping
    public ClientDTO addClient(@RequestBody ClientDTO clientDto) {
        return ClientMapper.toDto(clientService.addClient(ClientMapper.toEntity(clientDto)));
    }
}
