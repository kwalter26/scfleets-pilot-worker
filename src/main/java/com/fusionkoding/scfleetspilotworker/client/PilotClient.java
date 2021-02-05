package com.fusionkoding.scfleetspilotworker.client;

import com.fusionkoding.scfleetspilotworker.config.RestTemplateConifg;
import com.fusionkoding.scfleetspilotworker.dtos.PilotInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RequiredArgsConstructor
@Component
public class PilotClient {

    private final RestTemplate restTemplate;

    private String url = "http://localhost:9090/pilots/{pilotId}/profiles/{rsiHandle}/";

    public void updatePilot(String pilotId, String rsiHandle, PilotInfoDto pilotInfoDto) {

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + "");

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);

        HttpEntity<PilotInfoDto> entity = new HttpEntity<>(pilotInfoDto, headers);

        String urlStr = builder.buildAndExpand(pilotId,rsiHandle).toUriString();
        log.info("Updating pilot: " + urlStr);

        try {
            String response = restTemplate.patchForObject(urlStr, entity, String.class);
        } catch (Exception ex) {
            log.error(ex.getMessage(),ex);
        }
    }

}
