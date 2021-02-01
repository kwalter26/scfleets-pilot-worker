package com.fusionkoding.scfleetspilotworker.client;

import com.fusionkoding.scfleetspilotworker.client.models.OrgRequestBody;
import com.fusionkoding.scfleetspilotworker.client.models.OrgResponse;
import com.fusionkoding.scfleetspilotworker.client.models.ShipMatrixResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class RsiSiteClient {

    private static final String GET_PILOT = "https://robertsspaceindustries.com/citizens/";
    private final RestTemplate restTemplate;

    public String getPilotInfo(String rsiPilotId) {
        return restTemplate.getForObject(GET_PILOT + rsiPilotId, String.class);
    }

}
