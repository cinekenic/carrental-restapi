package com.carrental.controller;

import com.carrental.dto.ClientDTO;
import com.carrental.model.ClientEntity;
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
    public List<ClientEntity> getAllClients() {
        return clientService.getAllClients();
    }

    @PostMapping
    public ClientEntity addClient(@RequestBody ClientDTO clientDTO) {
        return clientService.addClient(clientDTO);
    }
}
