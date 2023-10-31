package org.goldenalf.privatepr.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "books")
@NoArgsConstructor
@Getter
@Setter
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "check_in")
    @Temporal(TemporalType.DATE)
    private LocalDate check_in;

    @Column(name = "check_out")
    @Temporal(TemporalType.DATE)
    private LocalDate check_out;
}
