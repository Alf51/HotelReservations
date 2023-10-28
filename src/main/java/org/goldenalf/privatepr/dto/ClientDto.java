package org.goldenalf.privatepr.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientDto {
    private String name;

    private String login;

    private String password;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd-MM-YYYY")
    private Date birthdate;
}
