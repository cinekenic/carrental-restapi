package com.carrental.service;

import com.carrental.dto.ClientDTO;
import com.carrental.model.ClientEntity;
import com.carrental.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public List<ClientEntity> getAllClients() {
        return clientRepository.findAll();
    }

    public ClientEntity addClient(ClientDTO clientDTO) {
        ClientEntity client = ClientEntity.builder()
                .firstName(clientDTO.firstName())
                .lastName(clientDTO.lastName())
                .email(clientDTO.email())
                .build();

        return clientRepository.save(client);
    }
}
