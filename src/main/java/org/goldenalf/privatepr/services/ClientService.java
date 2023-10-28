package org.goldenalf.privatepr.services;

import lombok.RequiredArgsConstructor;
import org.goldenalf.privatepr.models.Client;
import org.goldenalf.privatepr.models.Hotel;
import org.goldenalf.privatepr.repositories.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    @Transactional
    public void save(Client client) {
        clientRepository.save(client);
    }

    @Transactional
    public void delete(int id) {
        clientRepository.deleteById(id);
    }

    @Transactional
    public void update(int id, Client client) {
        client.setId(id);
        clientRepository.save(client);
    }


    @Transactional(readOnly = true)
    public Optional<Client> getClient(int id) {
        return clientRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Client> getAllClient() {
        return clientRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Hotel> findByName(String name) {
        return clientRepository.findByName(name);
    }
}
