package org.goldenalf.privatepr.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.goldenalf.privatepr.dto.ClientDto;
import org.goldenalf.privatepr.models.Client;
import org.goldenalf.privatepr.services.ClientService;
import org.goldenalf.privatepr.utils.erorsHandler.ErrorHandler;
import org.goldenalf.privatepr.utils.erorsHandler.ErrorResponse;
import org.goldenalf.privatepr.utils.erorsHandler.clientError.ClientErrorException;
import org.goldenalf.privatepr.utils.erorsHandler.validator.ClientValidator;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;


@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;
    private final ModelMapper modelMapper;
    private final ClientValidator clientValidator;

    @GetMapping("/{id}")
    public ClientDto getClient(@PathVariable("id") int id) {
        return convertToClientDto(clientService.getClient(id).orElseThrow(() -> new ClientErrorException("Клиент не найден")));
    }

    @GetMapping("/all")
    public List<ClientDto> getAllClient() {
        return convertToClientDtoList(clientService.getAllClient());
    }

    @PostMapping("/new")
    public ResponseEntity<HttpStatus> saveClient(@RequestBody @Valid Client client,
                                                 BindingResult bindingResult) {
        clientValidator.validate(client, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new ClientErrorException(ErrorHandler.getErrorMessage(bindingResult));
        }

        clientService.save(client);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> updateClient(@PathVariable("id") int id,
                                                   @RequestBody @Valid ClientDto clientDto,
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

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteClient(@PathVariable("id") int id) {
        clientService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(ClientErrorException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException e) {
        String errorMessage = ErrorHandler.getErrorMessage(e.getBindingResult());
        ErrorResponse errorResponse = new ErrorResponse(errorMessage, System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    private ResponseEntity<ErrorResponse> handleException(HttpMessageNotReadableException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
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
