package com.fusionkoding.scfleetspilotworker.client.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrgResponse {
    String success;
    String code;
    String msg;
    OrgData data;
}
