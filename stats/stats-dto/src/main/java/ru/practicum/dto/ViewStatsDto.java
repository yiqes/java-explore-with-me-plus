package ru.practicum.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewStatsDto {
    @NotBlank
    @Size(max = 255)
    private String app;
    @NotBlank
    @Size(max = 2048)
    private String uri;
    @NotNull
    @PositiveOrZero
    private Integer hits;
}
