package org.goldenalf.privatepr.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ClientRoleDto(@NotEmpty(message = "{validation.hotelBook.client.login.not-empty}")
                            @NotNull(message = "{validation.hotelBook.client.login.not-null}")
                            String login,

                            @NotNull(message = "{validation.hotelBook.client.role.not-null}")
                            @NotEmpty(message = "{validation.hotelBook.client.role.not-empty}")
                            String roleName) {
}
