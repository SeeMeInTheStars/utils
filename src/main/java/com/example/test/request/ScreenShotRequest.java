package com.example.test.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScreenShotRequest {
    private Long screenShotId;
    private Long eleId;
    private Long adId;
    private Long scsxPlanId;
    private Integer source;
    private String screenShot;
    private LocalDateTime shotTime;
    private String sign;

}
