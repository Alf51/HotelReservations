package org.goldenalf.privatepr.services;

import org.goldenalf.privatepr.dto.ClientRoleDto;
import org.goldenalf.privatepr.models.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    void save(Client client);

    void delete(int id);

    void update(int id, Client client);

    void addRoleForClient(ClientRoleDto clientRoleDto);

    void removeRoleForClient(ClientRoleDto clientRoleDto);

    Optional<Client> getClient(int id);

    List<Client> getAllClient();

    Optional<Client> findByLogin(String login);
}
