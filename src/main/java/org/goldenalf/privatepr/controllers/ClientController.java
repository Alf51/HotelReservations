package org.goldenalf.privatepr.controllers;

import lombok.RequiredArgsConstructor;
import org.goldenalf.privatepr.dto.ClientDto;
import org.goldenalf.privatepr.models.Client;
import org.goldenalf.privatepr.services.ClientService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;


@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    public ClientDto getClient(@PathVariable("id") int id) {
        //TODO обработать ошибку
        return convertToClientDto(clientService.getClient(id).get());
    }

    @GetMapping("/all")
    public List<ClientDto> getAllClient() {
        return convertToClientDtoList(clientService.getAllClient());
    }

    @PostMapping("/new")
    //TODO обработать ошибку
    public ResponseEntity<HttpStatus> saveClient(@RequestBody ClientDto clientDto) {
        Client client = convertToClient(clientDto);

        clientService.save(client);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    //TODO обработать ошибку
    public ResponseEntity<HttpStatus> updateClient(@PathVariable("id") int id,
                                                  @RequestBody ClientDto clientDto) {
        Client client = convertToClient(clientDto);
        client.setId(id);

        clientService.update(id, client);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteClient(@PathVariable("id") int id) {
        clientService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    private ClientDto convertToClientDto(Client client) {
        return modelMapper.map(client, ClientDto.class);
    }

    private Client convertToClient(ClientDto clientDto) {
        return modelMapper.map(clientDto, Client.class);
    }

    private List<ClientDto> convertToClientDtoList(List<Client> clientList) {
        Type listType = new TypeToken<List<ClientDto>>() {
        }.getType();
        return modelMapper.map(clientList, listType);
    }
}
