package com.fusionkoding.scfleetspilotworker.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InfoRequest {
    String pilotId;
    String rsiHandle;
}
