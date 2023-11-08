package org.goldenalf.privatepr.dto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReviewDto {
    @NotNull(message = "id отеля не может быть пустым")
    private int hotelId;

    @Min(value = 0, message = "минимальная оценка 0")
    @Max(value = 5, message = "максимальная оценка 5")
    @NotNull(message = "нужно поставить рейтинг от 0 до 5 включительно")
    private Integer rating;

    @NotNull(message = "Укажите Логин постояльца")
    @NotEmpty(message = "Логин постояльца не может быть пустым")
    private String clientLogin;
    private String review;
}
