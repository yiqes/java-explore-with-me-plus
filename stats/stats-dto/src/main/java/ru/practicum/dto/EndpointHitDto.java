package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EndpointHitDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Integer id;
    @NotBlank
    @Size(max = 255)
    String app;
    @NotBlank
    @Size(max = 2048)
    String uri;
    @NotBlank
    @Pattern(
            regexp = "^((25[0-5]|2[0-4]\\d|1?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|1?\\d\\d?)$",
            message = "Неверный формат IP-адреса"
    )
    String ip;
    @NotNull
    LocalDateTime timestamp;
}