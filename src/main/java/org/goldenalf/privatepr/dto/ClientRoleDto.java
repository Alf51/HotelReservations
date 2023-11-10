package org.goldenalf.privatepr.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ClientRoleDto(@NotEmpty(message = "login не может быть пустым")
                            @NotNull(message = "введите login")
                            String login,

                            @NotNull(message = "укажите роль")
                            @NotEmpty(message = "Поле роль не может быть пустым")
                            String roleName) {
}
