package org.goldenalf.privatepr.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    @Column(name = "check_in")
    @Temporal(TemporalType.DATE)
    private LocalDate check_in;

    @Column(name = "check_out")
    @Temporal(TemporalType.DATE)
    private LocalDate check_out;
}
