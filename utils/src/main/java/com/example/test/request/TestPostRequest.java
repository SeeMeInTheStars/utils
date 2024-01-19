package com.example.test.request;

import lombok.Data;

@Data
public class TestPostRequest {
    private Long eleId;

    private Long scsxPlanId;

    private Boolean success;

    private String errMsg;

    private String sign;

    private Long adPlanId;
}
