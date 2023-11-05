package org.goldenalf.privatepr.services;

import lombok.RequiredArgsConstructor;
import org.goldenalf.privatepr.models.Client;
import org.goldenalf.privatepr.models.Role;
import org.goldenalf.privatepr.repositories.ClientRepository;
import org.goldenalf.privatepr.utils.erorsHandler.clientError.ClientErrorException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService implements UserDetailsService {
    private final ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String userLogin) throws UsernameNotFoundException {
        Client client = clientRepository.findByLogin(userLogin).orElseThrow(() -> new UsernameNotFoundException("Логин клиента не найден"));
        return new org.springframework.security.core.userdetails.User(client.getLogin(), client.getPassword(), mapRolesToAthorities(client.getRoles()));
    }


    @Transactional
    public void save(Client client) {
        clientRepository.save(client);
    }

    @Transactional
    public void delete(int id) {
        if (getClient(id).isPresent()) {
            clientRepository.deleteById(id);
        } else {
            throw new ClientErrorException("Клиент не найден");
        }
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
    public Optional<Client> findByLogin(String login) {
        return clientRepository.findByLogin(login);
    }

    private List<? extends GrantedAuthority> mapRolesToAthorities(Set<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r.name())).collect(Collectors.toList());
    }

}
