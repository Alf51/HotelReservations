package org.goldenalf.privatepr.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public class ReviewDto {
    @NotNull(message = "нужно поставить рейтинг от 0 до 5 включительно")
    @Min(value = 0, message = "минимальная оценка 0")
    @Max(value = 5, message = "максимальная оценка 5")
    private int rating;

    @Temporal(TemporalType.DATE)
    private Date date;
}
