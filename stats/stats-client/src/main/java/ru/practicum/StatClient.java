package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class StatClient {
    private final RestClient restClient;
    private final DateTimeFormatter DATA_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public StatClient() {
        String STATS_SERVER_URL = "http://localhost:9090";
        this.restClient = RestClient.builder().baseUrl(STATS_SERVER_URL).build();
    }

    public void sendHit(EndpointHitDto endpointHitDto) {
        try {
            restClient.post()
                    .uri("/hit")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(endpointHitDto)
                    .retrieve()
                    .toBodilessEntity();
            log.info("Статистика посещений записана");
        } catch (Exception e) {
            log.error("Ошибка записи статистики посещений: {}", e.getMessage());
            throw new RuntimeException("Ошибка записи статистики посещений: " + e);
        }
    }

    public List<ViewStatsDto> getStats(String start, String end, List<String> uris, boolean unique) {
        try {
            var uriBuilder = UriComponentsBuilder.fromPath("/stats")
                    .queryParam("start", start)
                    .queryParam("end", end)
                    .queryParam("unique", unique);
            if (uris != null && !uris.isEmpty()) {
                uriBuilder.queryParam("uris", uris);
            }
            log.info("Статистика посещений получена");
            return restClient.get()
                    .uri(uriBuilder.build().toString())
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<ViewStatsDto>>() {});
        } catch (Exception e) {
            log.error("Ошибка получения статистики посещений: {}", e.getMessage());
            throw new RuntimeException("Ошибка получения статистики посещений: " + e);
        }
    }
}