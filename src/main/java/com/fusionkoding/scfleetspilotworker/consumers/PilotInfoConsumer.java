package com.fusionkoding.scfleetspilotworker.consumers;

import com.fusionkoding.scfleetspilotworker.client.PilotClient;
import com.fusionkoding.scfleetspilotworker.client.RsiSiteClient;
import com.fusionkoding.scfleetspilotworker.dtos.InfoRequest;
import com.fusionkoding.scfleetspilotworker.dtos.PilotInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Component
public class PilotInfoConsumer {
    private final RsiSiteClient rsiSiteClient;
    private final PilotClient pilotClient;

    @Bean
    public Consumer<Message<InfoRequest>> pilotInfo() {
        return infoRequestMessage -> {
            InfoRequest request = infoRequestMessage.getPayload();
            String response = rsiSiteClient.getPilotInfo(request.getRsiHandle());
            PilotInfoDto pilot = parsePilot(response);
            log.info(pilot.getUeeRecordNumber());
            pilotClient.updatePilot(request.getPilotId(),request.getRsiHandle(),pilot);
        };
    }

    private PilotInfoDto parsePilot(String htmlString) {

        Document doc = Jsoup.parse(htmlString);
        Element element = doc.getElementById("public-profile");

        String rsiHandle = element.select("div.profile-content.overview-content.clearfix > div.box-content.profile-wrapper.clearfix > div > div.profile.left-col > div > div.info > p:nth-child(2) > strong").first().text();
        String ueeRecordNumber = element.select("div.profile-content.overview-content.clearfix > p > strong").first().text();
        String imageUrl = element.select("div.profile-content.overview-content.clearfix > div.box-content.profile-wrapper.clearfix > div > div.profile.left-col > div > div.thumb > img").attr("src");
        String enlistDate = element.select("div.profile-content.overview-content.clearfix > div.left-col > div > p:nth-child(1) > strong").first().text();
        String location = element.select("div.profile-content.overview-content.clearfix > div.left-col > div > p:nth-child(2) > strong").first().text();
        String fluency = element.select("div.profile-content.overview-content.clearfix > div.left-col > div > p:nth-child(3) > strong").first().text();
        String orgSymbol = element.select("div.profile-content.overview-content.clearfix > div.box-content.profile-wrapper.clearfix > div > div.main-org.right-col.visibility-V > div > div.info > p:nth-child(2) > strong").first().text();

        return PilotInfoDto.builder()
                .rsiHandle(rsiHandle)
                .ueeRecordNumber(ueeRecordNumber)
                .rsiProfileImgUrl(imageUrl)
                .enlistDate(enlistDate)
                .fluency(fluency)
                .location(location)
                .orgSymbol(orgSymbol)
                .build();
    }
}
