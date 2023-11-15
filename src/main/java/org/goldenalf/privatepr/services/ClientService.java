package org.goldenalf.privatepr.services;

import lombok.RequiredArgsConstructor;
import org.goldenalf.privatepr.dto.ClientRoleDto;
import org.goldenalf.privatepr.models.Client;
import org.goldenalf.privatepr.models.Role;
import org.goldenalf.privatepr.repositories.ClientRepository;
import org.goldenalf.privatepr.utils.VerifyingAccess;
import org.goldenalf.privatepr.utils.exeptions.ClientErrorException;
import org.springframework.context.MessageSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService implements UserDetailsService {
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerifyingAccess verifyingAccess;
    private final MessageSource messageSource;

    @Override
    public UserDetails loadUserByUsername(String userLogin) throws UsernameNotFoundException {
        Client client = clientRepository.findByLogin(userLogin).orElseThrow(() -> new UsernameNotFoundException(messageSource.
                getMessage("validation.hotelBook.client.exception.login-not-found", null, Locale.getDefault())));
        return new User(client.getLogin(), client.getPassword(), mapRolesToAuthorities(client.getRoles()));
    }

    @Transactional
    public void save(Client client) {
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        client.setRoles(Collections.singleton(Role.USER));
        clientRepository.save(client);
    }

    @Transactional
    public void delete(int id) {
        Client clientByDeleted = getClient(id).orElseThrow(() -> new ClientErrorException(messageSource
                .getMessage("validation.hotelBook.client.exception.client-not-found", null, Locale.getDefault())));
        String login = clientByDeleted.getLogin();
        verifyingAccess.checkPossibilityAction(login);

        clientRepository.deleteById(id);
    }

    @Transactional
    public void update(int id, Client client) {
        Client clientInDB = getClient(id).orElseThrow(() -> new ClientErrorException(messageSource
                .getMessage("validation.hotelBook.client.exception.client-not-found", null, Locale.getDefault())));

        verifyingAccess.checkPossibilityAction(client.getLogin(), clientInDB.getLogin());
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        client.setRoles(clientInDB.getRoles());
        clientRepository.save(client);
    }

    @Transactional
    public void addRoleForClient(ClientRoleDto clientRoleDto) {
        Role role = getValidRole(clientRoleDto.roleName());
        Client client = findByLogin(clientRoleDto.login())
                .orElseThrow(() -> new ClientErrorException(messageSource
                        .getMessage("validation.hotelBook.client.exception.requested-login-not-found", null, Locale.getDefault())
                        .formatted(clientRoleDto.login())));

        client.getRoles().add(role);
        clientRepository.save(client);
    }

    @Transactional
    public void removeRoleForClient(ClientRoleDto clientRoleDto) {
        Role role = getValidRole(clientRoleDto.roleName());
        Client client = findByLogin(clientRoleDto.login())
                .orElseThrow(() -> new ClientErrorException(messageSource
                        .getMessage("validation.hotelBook.client.exception.requested-login-not-found", null, Locale.getDefault())
                        .formatted(clientRoleDto.login())));

        client.getRoles().remove(role);
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

    private List<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r.name())).collect(Collectors.toList());
    }

    private Role getValidRole(String roleName) {
        try {
            return Role.valueOf(roleName);
        } catch (IllegalArgumentException e) {
            String errorMessage = messageSource
                    .getMessage("validation.hotelBook.role.exception.role-not-found", null, Locale.getDefault())
                    .formatted(roleName) + Arrays.toString(Role.values());

            throw new ClientErrorException(errorMessage);
        }
    }
}
