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
import org.jsoup.select.Elements;
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
            pilotClient.updatePilot(request.getPilotId(), request.getRsiHandle(), pilot);
        };
    }

    private PilotInfoDto parsePilot(String htmlString) {

        Document doc = Jsoup.parse(htmlString);
        Element element = doc.getElementById("public-profile");

        String rsiHandle = getElement(element, "div.profile-content.overview-content.clearfix > div.box-content.profile-wrapper.clearfix > div > div.profile.left-col > div > div.info > p:nth-child(2) > strong");
        String ueeRecordNumber = getElement(element, "div.profile-content.overview-content.clearfix > p > strong");
        String imageUrl = element.select("div.profile-content.overview-content.clearfix > div.box-content.profile-wrapper.clearfix > div > div.profile.left-col > div > div.thumb > img").attr("src");
        String enlistDate = getElement(element, "div.profile-content.overview-content.clearfix > div.left-col > div > p:nth-child(1) > strong");
        String location = getElement(element, "div.profile-content.overview-content.clearfix > div.left-col > div > p:nth-child(2) > strong");
        String fluency = getElement(element, "div.profile-content.overview-content.clearfix > div.left-col > div > p:nth-child(3) > strong");
        String orgSymbol = getElement(element, "div.profile-content.overview-content.clearfix > div.box-content.profile-wrapper.clearfix > div > div.main-org.right-col.visibility-V > div > div.info > p:nth-child(2) > strong");

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

    private String getElement(Element selector, String s) {
        String str = "";
        Elements elements = selector.select(s);
        if (!elements.isEmpty()) {
            str = elements.first().text();
        }
        return str;
    }
}
