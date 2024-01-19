package com.example.test.api;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ApiInstance extends Api{


    @Override
    Map<String, String> getAuthHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return headers;
    }

    public String getBaidu() throws Exception {
        String url = "https://www.baidu.com";
        String s = doGet(url, null);
        System.out.println(s);
        return s;
    }

    public String postBaidu() throws Exception {
        String url = "https://www.baidu.com";
        String s = doPost(url, null, null);
        System.out.println(s);
        return s;
    }

}
