package org.goldenalf.privatepr.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "rating")
    @Min(value = 0, message = "минимальная оценка 0")
    @Max(value = 5, message = "максимальная оценка 5")
    @NotNull(message = "нужно поставить рейтинг от 0 до 5 включительно")
    private Integer rating;

    @Column(name = "review")
    @NotNull(message = "Укажите имя постояльца")
    @NotEmpty(message = "имя постояльца не может быть пустым")
    private String review;

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "id_hotel", referencedColumnName = "id")
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "id_client", referencedColumnName = "id")
    private Client client;

    public Review(int rating, LocalDate date) {
        this.rating = rating;
        this.date = date;
    }
}
