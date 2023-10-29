package org.goldenalf.privatepr.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

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
    private int rating;

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;

    public Review(int rating, Date date) {
        this.rating = rating;
        this.date = date;
    }
}
