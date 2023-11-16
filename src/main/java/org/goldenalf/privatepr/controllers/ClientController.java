package org.goldenalf.privatepr.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.goldenalf.privatepr.dto.ClientAllRoleDto;
import org.goldenalf.privatepr.dto.ClientDto;
import org.goldenalf.privatepr.dto.ClientExtendedDto;
import org.goldenalf.privatepr.dto.ClientRoleDto;
import org.goldenalf.privatepr.models.Client;
import org.goldenalf.privatepr.services.ClientService;
import org.goldenalf.privatepr.utils.erorsHandler.ErrorHandler;
import org.goldenalf.privatepr.utils.exeptions.ClientErrorException;
import org.goldenalf.privatepr.utils.erorsHandler.validator.ClientValidator;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;


@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;
    private final ModelMapper modelMapper;
    private final ErrorHandler errorHandler;
    private final ClientValidator clientValidator;

    @GetMapping("/{id_client}")
    public ClientDto getClient(@PathVariable("id_client") int id) {
        return convertToClientDto(clientService.getClient(id).orElseThrow(() -> new ClientErrorException(errorHandler
                .getErrorMessage("validation.hotelBook.client-controller.exception.client-not-found"))));
    }

    @GetMapping("/all")
    public List<ClientDto> getAllClient() {
        return convertToClientDtoList(clientService.getAllClient());
    }

    @GetMapping("/allRoles/{id_client}")
    public ClientAllRoleDto getAllRolesClient(@PathVariable("id_client") int id) {
        Client client = clientService.getClient(id).orElseThrow(() -> new ClientErrorException(errorHandler
                .getErrorMessage("validation.hotelBook.client-controller.exception.client-not-found")));
        return convertToClientAllRoleDto(client);
    }

    @PostMapping("/new")
    public ResponseEntity<HttpStatus> saveClient(@RequestBody @Valid ClientExtendedDto clientDto,
                                                 BindingResult bindingResult) {
        Client client = convertToClient(clientDto);
        clientValidator.validate(client, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new ClientErrorException(errorHandler.getErrorMessage(bindingResult));
        }

        clientService.save(client);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/addRole/")
    public ResponseEntity<HttpStatus> addRoleForClient(@RequestBody @Valid ClientRoleDto clientRoleDto) {
        clientService.addRoleForClient(clientRoleDto);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/removeRole/")
    public ResponseEntity<HttpStatus> removeRoleForClient(@RequestBody @Valid ClientRoleDto clientRoleDto) {
        clientService.removeRoleForClient(clientRoleDto);
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
            throw new ClientErrorException(errorHandler.getErrorMessage(bindingResult));
        }
        clientService.update(id, client);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id_client}")
    public ResponseEntity<HttpStatus> deleteClient(@PathVariable("id_client") int id) {
        clientService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
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
}
