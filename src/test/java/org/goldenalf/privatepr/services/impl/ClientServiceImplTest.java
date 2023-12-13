package org.goldenalf.privatepr.services.impl;

import org.goldenalf.privatepr.dto.ClientRoleDto;
import org.goldenalf.privatepr.models.Client;
import org.goldenalf.privatepr.models.Role;
import org.goldenalf.privatepr.repositories.ClientRepository;
import org.goldenalf.privatepr.utils.VerifyingAccess;
import org.goldenalf.privatepr.utils.erorsHandler.ErrorHandler;
import org.goldenalf.privatepr.utils.exeptions.ClientErrorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private VerifyingAccess verifyingAccess;
    @Mock
    private ErrorHandler errorHandler;
    @InjectMocks
    private ClientServiceImpl clientService;


    @Test
    void save_SaveClientFromClientService() {
        Client client = getClient();

        // Создаем ArgumentCaptor для захвата аргументов, переданных в passwordEncoder.encode
        ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass(String.class);

        clientService.save(client);

        // Проверяем, что passwordEncoder.encode был вызван с правильным паролем
        verify(passwordEncoder).encode(passwordCaptor.capture());

        // Проверяем, что у клиента установлены правильные роли
        assertEquals(Collections.singleton(Role.USER), client.getRoles());

        // Проверяем, что clientRepository.save был вызван с правильным клиентом
        verify(clientRepository).save(client);
    }

    @Test
    void delete_forExistingClient_deleteSuccess() {
        Client client = getClient();
        int clientId = client.getId();
        when(clientService.getClient(clientId)).thenReturn(Optional.of(client));

        clientService.delete(clientId);

        // Проверяем, что verifyingAccess.checkPossibilityAction был вызван с правильным логином
        verify(verifyingAccess).checkPossibilityAction(client.getLogin());

        // Проверяем, что clientRepository.deleteById был вызван с правильным id
        verify(clientRepository).deleteById(clientId);
    }

    @Test
    void delete_forNotExistingClient_deleteFailed() {
        int clientId = 22;
        when(clientService.getClient(clientId)).thenReturn(Optional.empty());
        when(errorHandler.getErrorMessage(anyString())).thenReturn("Error message");


        // Проверяем, что будет выброшено исключение ClientErrorException
        ClientErrorException exception = assertThrows(ClientErrorException.class, () -> {
            clientService.delete(clientId);
        });

        // Проверяем, что исключение содержит правильное сообщение
        assertEquals("Error message", exception.getMessage());

        // Проверяем, что verifyingAccess.checkPossibilityAction и clientRepository.deleteById НЕ были вызваны
        verify(verifyingAccess, never()).checkPossibilityAction(anyString());
        verify(clientRepository, never()).deleteById(anyInt());
    }

    @Test
    void update_forExistingClient_updateSuccess() {
        Client existingClient = getClient();
        int clientId = existingClient.getId();

        //Создаём клиента с новыми данными для обновления
        Client updatedClient = new Client();
        updatedClient.setId(clientId);
        updatedClient.setLogin("newLogin");
        updatedClient.setPassword("newPassword");
        String newPassword = updatedClient.getPassword();

        when(clientService.getClient(clientId)).thenReturn(Optional.of(existingClient));
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");

        clientService.update(clientId, updatedClient);

        // Проверяем, что verifyingAccess.checkPossibilityAction был вызван с правильными логинами
        verify(verifyingAccess).checkPossibilityAction(updatedClient.getLogin(), existingClient.getLogin());

        // Проверяем, что passwordEncoder.encode был вызван с правильным паролем
        verify(passwordEncoder).encode(newPassword);

        // Проверяем, что clientRepository.save был вызван с правильным клиентом
        verify(clientRepository).save(updatedClient);
    }

    @Test
    void update_forNotExistingClient_updateFailed() {
        Client client = getClient();
        int clientId = client.getId();
        when(clientService.getClient(clientId)).thenReturn(Optional.empty());

        // Mocking errorHandler.getErrorMessage
        when(errorHandler.getErrorMessage(anyString())).thenReturn("Error message");

        // Проверяем, что будет выброшено исключение ClientErrorException
        ClientErrorException exception = assertThrows(ClientErrorException.class, () -> {
            clientService.update(clientId, client);
        });

        // Проверяем, что исключение содержит правильное сообщение
        assertEquals("Error message", exception.getMessage());
    }

    @Test
    void addRoleForClient_forExistingClient_addRoleSuccess() {
        ClientRoleDto clientRoleDto = getClientRoleDto();
        Role role = Role.USER;
        Client client = getClient();
        client.setRoles(new HashSet<>());

        when(clientService.findByLogin(clientRoleDto.login())).thenReturn(Optional.of(client));

        // Вызываем метод addRoleForClient
        clientService.addRoleForClient(clientRoleDto);

        // Проверяем, что роль добавлена к клиенту
        assertTrue(client.getRoles().contains(role));

        // Проверяем, что clientRepository.save был вызван с правильным клиентом
        verify(clientRepository).save(client);
    }

    @Test
    void addRoleForClient_forNotExistingClient_addRoleFailed() {
        ClientRoleDto clientRoleDto = getClientRoleDto();
        when(errorHandler.getErrorMessage(anyString())).thenReturn("Error message");

        // Проверяем, что будет выброшено исключение ClientErrorException
        ClientErrorException exception = assertThrows(ClientErrorException.class, () -> {
            clientService.addRoleForClient(clientRoleDto);
        });

        // Проверяем, что исключение содержит правильное сообщение
        assertEquals("Error message", exception.getMessage());

        // Проверяем, что clientRepository.save НЕ был вызван
        clientRepository.save(mock(Client.class));
    }

    @Test
    void removeRoleForClient_forExistingClient_removeRoleSuccess() {
        ClientRoleDto clientRoleDto = getClientRoleDto();
        Role role = Role.USER;
        Client client = getClient();
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        client.setRoles(roles);

        when(clientService.findByLogin(clientRoleDto.login())).thenReturn(Optional.of(client));

        // Вызываем метод removeRoleForClient
        clientService.removeRoleForClient(clientRoleDto);

        // Проверяем, что роль удалена у клиента
        assertFalse(client.getRoles().contains(role));

        // Проверяем, что clientRepository.save был вызван с правильным клиентом
        verify(clientRepository).save(client);
    }

    @Test
    void removeRoleForClient_forNotExistingClient_removeRoleSuccess() {
        ClientRoleDto clientRoleDto = getClientRoleDto();
        when(errorHandler.getErrorMessage(anyString())).thenReturn("Error message");

        // Проверяем, что будет выброшено исключение ClientErrorException
        ClientErrorException exception = assertThrows(ClientErrorException.class, () -> {
            clientService.removeRoleForClient(clientRoleDto);
        });

        // Проверяем, что исключение содержит правильное сообщение
        assertEquals("Error message", exception.getMessage());

        // Проверяем, что clientRepository.save НЕ был вызван
        clientRepository.save(mock(Client.class));
    }

    @Test
    void getAllClient_findAllClients() {
        List<Client> clientList = getClientList();
        when(clientService.getAllClient()).thenReturn(clientList);

        List<Client> maybeClientList = clientService.getAllClient();

        assertNotNull(maybeClientList);
        assertEquals(clientList, maybeClientList);
        verify(clientRepository).findAll();
    }

    @Test
    void loadUserByUsername_forExistingClient_GettingUserDetailsSuccessfully() {
        Client client = getClient();
        String userLogin = client.getLogin();
        String password = client.getPassword();

        when(clientRepository.findByLogin(userLogin)).thenReturn(Optional.of(client));

        UserDetails userDetails = clientService.loadUserByUsername(userLogin);

        // Проверяем, что UserDetails был создан с правильными данными
        assertEquals(userLogin, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());

        // Проверяем, что clientRepository.findByLogin был вызван с правильным аргументом
        verify(clientRepository).findByLogin(userLogin);
    }

    @Test
    void loadUserByUsername_forNotExistingClient_UserNotFind() {
        String userLogin = "DummyLogin";

        when(errorHandler.getErrorMessage(anyString())).thenReturn("Error message");
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            clientService.loadUserByUsername(userLogin);
        });

        // Проверяем, что clientRepository.findByLogin был вызван с правильным аргументом
        verify(clientRepository).findByLogin(userLogin);

        // Проверяем, что исключение содержит правильное сообщение
        assertEquals("Error message", exception.getMessage());
    }


    private Client getClient() {
        Client client = new Client();
        client.setId(1);
        client.setName("Monarch");
        client.setLogin("Bee");
        client.setPassword("123456");

        return client;
    }

    private ClientRoleDto getClientRoleDto() {
        return new ClientRoleDto("Bee", "USER");
    }

    private List<Client> getClientList() {
        Client firstClient = new Client();
        firstClient.setId(1);
        firstClient.setName("Monarch");
        firstClient.setLogin("Bee");
        firstClient.setPassword("123456");

        Client secondClient = new Client();
        secondClient.setId(2);
        secondClient.setName("Brock");
        secondClient.setLogin("Samson");
        secondClient.setPassword("123456");

        return List.of(firstClient, secondClient);
    }
}