package org.goldenalf.privatepr.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class clientDto {
    private String name;

    private String login;

    private String password;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd-MM-YYYY")
    private String birthdate;
}
