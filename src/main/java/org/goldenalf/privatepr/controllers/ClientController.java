package org.goldenalf.privatepr.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.goldenalf.privatepr.dto.*;
import org.goldenalf.privatepr.models.Client;
import org.goldenalf.privatepr.services.impl.ClientServiceImpl;
import org.goldenalf.privatepr.utils.erorsHandler.ErrorHandler;
import org.goldenalf.privatepr.utils.exeptions.ClientErrorException;
import org.goldenalf.privatepr.utils.erorsHandler.validator.ClientValidator;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;


@Controller
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientServiceImpl clientService;
    private final ModelMapper modelMapper;
    private final ErrorHandler errorHandler;
    private final ClientValidator clientValidator;

    @GetMapping("/{id_client}")
    public String getClientById(@PathVariable("id_client") int id, Model model) {
        ClientDto clientDto = convertToClientDto(clientService.getClient(id).orElseThrow(() -> new ClientErrorException(errorHandler
                .getErrorMessage("validation.hotelBook.client-controller.exception.client-not-found"))));
        model.addAttribute("client", clientDto);

        return "client/show-client";
    }

    @GetMapping("login/{login}")
    public String getClientByLogin(@PathVariable("login") String login, Model model) {
        ClientDto clientDto = convertToClientDto(clientService.findByLogin(login).orElseThrow(() -> new ClientErrorException(errorHandler
                .getErrorMessage("validation.hotelBook.client-controller.exception.client-not-found"))));
        model.addAttribute("client", clientDto);

        return "client/show-client";
    }

    @GetMapping("/all")
    public String getAllClient(Model model) {
        List<ClientDto> clientList = convertToClientDtoList(clientService.getAllClient());
        model.addAttribute("clientList", clientList);
        return "client/client-all";
    }

    @GetMapping("/allRoles/{id_client}")
    public String getAllRolesClient(@PathVariable("id_client") int id, Model model) {
        Client client = clientService.getClient(id).orElseThrow(() -> new ClientErrorException(errorHandler
                .getErrorMessage("validation.hotelBook.client-controller.exception.client-not-found")));
        ClientAllRoleDto clientAllRoleDto = convertToClientAllRoleDto(client);
        model.addAttribute("client", clientAllRoleDto);
        return "client/role";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        ClientExtendedDto clientExtendedDto = convertToClientExtendedDto(clientService.getClient(id).orElseThrow(() -> new ClientErrorException(errorHandler
                .getErrorMessage("validation.hotelBook.client-controller.exception.client-not-found"))));
        model.addAttribute("client", clientExtendedDto);
        return "client/edit";
    }

    @GetMapping("/new")
    public String newClientCreate(@ModelAttribute("client") ClientExtendedDto clientExtendedDto) {
        return "client/new";
    }

    @GetMapping("/addRole/")
    public String getRolePage(@ModelAttribute("client") ClientRoleDto clientRoleDto) {
        return "auth/role-page";
    }

    @PostMapping("/new")
    public String saveClient(@ModelAttribute("client") @Valid ClientExtendedDto clientDto,
                             BindingResult bindingResult) {
        Client client = convertToClient(clientDto);
        clientValidator.validate(client, bindingResult);

        if (bindingResult.hasErrors()) {
            return "client/new";
        }

        clientService.save(client);
        return "redirect:/auth/login";
    }

    @PatchMapping(value = "/role/", params = "action=addRole")
    public String addRoleForClient(@ModelAttribute("client") @Valid ClientRoleDto clientRoleDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/auth/role-page";
        }
        clientService.addRoleForClient(clientRoleDto);
        return "redirect:/auth/admin";
    }

    @PatchMapping(value = "/role/", params = "action=deleteRole")
    public String removeRoleForClient(@ModelAttribute("client") @Valid ClientRoleDto clientRoleDto,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/auth/role-page";
        }
        clientService.removeRoleForClient(clientRoleDto);
        return "redirect:/auth/admin";
    }

    @PatchMapping("/{id_client}")
    public String updateClient(@PathVariable("id_client") int id,
                               @ModelAttribute("client") @Valid ClientExtendedDto clientDto,
                               BindingResult bindingResult) {
        Client client = convertToClient(clientDto);
        client.setId(id);
        clientValidator.validate(client, bindingResult);
        if (bindingResult.hasErrors()) {
            clientDto.setId(id);
            return "client/edit";
        }
        clientService.update(id, client);
        return "redirect:/client/all";
    }

    @DeleteMapping("/{id_client}")
    public String deleteClient(@PathVariable("id_client") int id) {
        clientService.delete(id);
        return "redirect:/hotel/all";
    }

    private ClientDto convertToClientDto(Client client) {
        return modelMapper.map(client, ClientDto.class);
    }

    private ClientExtendedDto convertToClientExtendedDto(Client client) {
        return modelMapper.map(client, ClientExtendedDto.class);
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
