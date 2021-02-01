package com.fusionkoding.scfleetspilotworker.consumers;

import com.fusionkoding.scfleetspilotworker.client.RsiSiteClient;
import com.fusionkoding.scfleetspilotworker.dtos.PilotInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Component
public class PilotInfoConsumer {
    private final RsiSiteClient rsiSiteClient;

    @Bean
    public Consumer<Message<String>> stringConsumer() {
        return pilotId -> {
            String id = pilotId.getPayload();
            String response = rsiSiteClient.getPilotInfo(id);
            PilotInfoDto pilot = parsePilot(response);
            log.info(pilot.toString());
        };
    }

    private PilotInfoDto parsePilot(String htmlString) {

        Document doc = Jsoup.parse(htmlString);
        Element element = doc.getElementById("public-profile");



        String rsiHandle = element.select("#public-profile > div.profile-content.overview-content.clearfix > div.box-content.profile-wrapper.clearfix > div > div.profile.left-col > div > div.info > p:nth-child(2) > strong").first().text();
        String ueeRecordNumber = element.select("#public-profile > div.profile-content.overview-content.clearfix > p > strong").first().text();
        String imageUrl = element.select("#public-profile > div.profile-content.overview-content.clearfix > div.box-content.profile-wrapper.clearfix > div > div.profile.left-col > div > div.thumb > img").attr("src");
        return PilotInfoDto.builder().rsiHandle(rsiHandle).ueeRecordNumber(ueeRecordNumber).rsiProfileImgUrl(imageUrl).build();
    }
}
