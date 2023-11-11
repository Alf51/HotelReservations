package org.goldenalf.privatepr.utils.erorsHandler.validator;

import lombok.AllArgsConstructor;
import org.goldenalf.privatepr.models.Client;
import org.goldenalf.privatepr.services.ClientService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;


@Component
@AllArgsConstructor
public class ClientValidator implements Validator {
    private final ClientService clientService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Client.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Client client = (Client) target;

        Optional<Client> existClient = clientService.getClient(client.getId());
        if (existClient.isPresent() && existClient.get().getLogin().equals(client.getLogin())) {
            //Значит клиент уже существует и мы производим обновление
            return;
        }
        boolean isLoginExisted = clientService.getAllClient().stream()
                .anyMatch(holtelClient -> holtelClient.getLogin().equals(client.getLogin()));

        if (isLoginExisted) {
            errors.rejectValue("login", "409", "Данный логин уже существует");
        }
    }
}
