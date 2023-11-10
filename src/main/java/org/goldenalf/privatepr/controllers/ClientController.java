package org.goldenalf.privatepr.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.goldenalf.privatepr.dto.ClientAllRoleDto;
import org.goldenalf.privatepr.dto.ClientDto;
import org.goldenalf.privatepr.dto.ClientExtendedDto;
import org.goldenalf.privatepr.dto.ClientRoleDto;
import org.goldenalf.privatepr.models.Client;
import org.goldenalf.privatepr.models.Role;
import org.goldenalf.privatepr.services.ClientService;
import org.goldenalf.privatepr.utils.erorsHandler.ErrorHandler;
import org.goldenalf.privatepr.utils.erorsHandler.ErrorResponse;
import org.goldenalf.privatepr.utils.exeptions.ClientErrorException;
import org.goldenalf.privatepr.utils.erorsHandler.validator.ClientValidator;
import org.goldenalf.privatepr.utils.exeptions.InsufficientAccessException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;
    private final ModelMapper modelMapper;
    private final ClientValidator clientValidator;

    @GetMapping("/{id_client}")
    public ClientDto getClient(@PathVariable("id_client") int id) {
        return convertToClientDto(clientService.getClient(id).orElseThrow(() -> new ClientErrorException("Клиент не найден")));
    }

    @GetMapping("/all")
    public List<ClientDto> getAllClient() {
        return convertToClientDtoList(clientService.getAllClient());
    }

    @GetMapping("/allRoles/{id_client}")
    public ClientAllRoleDto getAllRolesClient(@PathVariable("id_client") int id) {
        Client client = clientService.getClient(id).orElseThrow(() -> new ClientErrorException("Клиент не найден"));
        return convertToClientAllRoleDto(client);
    }

    @PostMapping("/new")
    public ResponseEntity<HttpStatus> saveClient(@RequestBody @Valid ClientExtendedDto clientDto,
                                                 BindingResult bindingResult) {

        Client client = convertToClient(clientDto);
        clientValidator.validate(client, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new ClientErrorException(ErrorHandler.getErrorMessage(bindingResult));
        }

        client.setRoles(Collections.singleton(Role.USER));
        clientService.save(client);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/addRole/")
    public ResponseEntity<HttpStatus> setRoleForClient(@RequestBody @Valid ClientRoleDto clientRoleDto) {

        Role role = getValidRole(clientRoleDto.roleName());
        Client client = clientService.findByLogin(clientRoleDto.login())
                .orElseThrow(() -> new ClientErrorException("Клиент c логином '" + clientRoleDto.login() + "' не найден"));

        client.getRoles().add(role);
        clientService.save(client);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/removeRole/")
    public ResponseEntity<HttpStatus> removeRoleForClient(@RequestBody @Valid ClientRoleDto clientRoleDto) {

        Role role = getValidRole(clientRoleDto.roleName());
        Client client = clientService.findByLogin(clientRoleDto.login())
                .orElseThrow(() -> new ClientErrorException("Клиент c логином '" + clientRoleDto.login() + "' не найден"));

        client.getRoles().remove(role);
        clientService.save(client);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id_client}")
    public ResponseEntity<HttpStatus> updateClient(@PathVariable("id_client") int id,
                                                   @RequestBody @Valid ClientExtendedDto clientDto,
                                                   BindingResult bindingResult) {

        Client client = convertToClient(clientDto);
        client.setId(id);
        clientValidator.validate(client, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ClientErrorException(ErrorHandler.getErrorMessage(bindingResult));
        }
        clientService.update(id, client);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id_client}")
    public ResponseEntity<HttpStatus> deleteClient(@PathVariable("id_client") int id) {
        clientService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(ClientErrorException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(InsufficientAccessException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(DateTimeParseException e) {
        String errorMessage = "Некорректный формат даты. Введена '" + e.getParsedString() + "'. Введите дату в формате dd-MM-yyyy";
        ErrorResponse errorResponse = new ErrorResponse(errorMessage, System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException e) {
        String errorMessage = ErrorHandler.getErrorMessage(e.getBindingResult());
        ErrorResponse errorResponse = new ErrorResponse(errorMessage, System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private ClientDto convertToClientDto(Client client) {
        return modelMapper.map(client, ClientDto.class);
    }

    private ClientAllRoleDto convertToClientAllRoleDto(Client client) {
        return modelMapper.map(client, ClientAllRoleDto.class);
    }

    private Client convertToClient(ClientExtendedDto clientDto) {
        return modelMapper.map(clientDto, Client.class);
    }

    private List<ClientDto> convertToClientDtoList(List<Client> clientList) {
        Type listType = new TypeToken<List<ClientDto>>() {
        }.getType();
        return modelMapper.map(clientList, listType);
    }

    private Role getValidRole(String roleName) {
        try {
            return Role.valueOf(roleName);
        } catch (IllegalArgumentException e) {
            String errorMessage = "Роль " + roleName + " не найдена. Роль должна содержать одно из следующих значений: " + Arrays.toString(Role.values());
            throw new ClientErrorException(errorMessage);
        }
    }
}
