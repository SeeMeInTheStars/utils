package com.example.test.controller;

import cn.hutool.core.util.ZipUtil;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.example.test.request.TestPostRequest;
import com.example.test.utils.HttpUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class TestController {

    private final String deliAppKey= "5376452d14effb3dcd6d25e2487c071d";

    private final String deliAppSecret = "hshxhdlg925kb1dd1fee83n98fpx9o4l";

    private final String deliApi = "/v2.0/cloudappapi";


    @PostMapping("/notice/adPlan")
    public String testPost(@RequestBody TestPostRequest params){
        System.out.println("adPlan:" + params);
        return "{\"success\":true}";
    }

    @PostMapping("/adLog")
    public String testAdPlan(@RequestParam Long eleId,
                             @RequestParam Long fileId,
                             @RequestParam MultipartFile file,
                             @RequestParam String fileMd5,
                             @RequestParam String sign
    ) throws IOException {

        System.out.println("adLog:");

        byte[] bytes = file.getBytes();
        System.out.println(new String(bytes));

        byte[] unGzip = ZipUtil.unGzip(bytes);
        System.out.println(new String(unGzip));

        return "{\"success\":true}";
    }

    @PostMapping("/notice/scsxPlan")
    public String testScsxPlan(@RequestBody TestPostRequest params){
        System.out.println("scsxPlan:");

        System.out.println(params);

        return "{\"success\":true}";
    }

    @PostMapping("/screenShot")
    public String testScreenShot(@RequestBody Map<String, Object> params) throws Exception {
        System.out.println("screenShot:");

        String sign = (String)params.get("sign");
        System.out.println("sign明文:" + sign);
        StringBuilder stringBuilder = new StringBuilder();
        List<Map.Entry<String, Object>> entries = params.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toList());
        for (Map.Entry<String, Object> entry : entries) {
            if(!entry.getKey().equals("sign")){
                stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        System.out.println("sign校验:" + HttpUtils.checkSign(sign,stringBuilder.toString()));
        return "{\"success\":true}";
    }

    @PostMapping("/lift/{id}/status")
    public String testBlackBox(@PathVariable String id, @RequestBody Map<String, String> params , @RequestHeader Map<String, String> headers){
        System.out.println(params);
        System.out.println(headers.get("sign"));

        return "{\"success\":true}";
    }

    @GetMapping("/test")
    public String test(){

        String strJSON = "{items=[{id=1647, address=sample, person=null, date=1686616249817, read=0, status=-1, type=1, subject=null, body=registration done successful, click on this to know more, serviceCenter=+910000000000}], total=1570}"
                .replaceAll("=",":")
                ;

        return "success";
    }

    @GetMapping("/deliTest")
    public String deliTest(){

        Long timeStamp = System.currentTimeMillis();
        System.out.println("timeStamp:");
        System.out.println(timeStamp);
        String toEncrypt = deliApi + timeStamp + deliAppKey + deliAppSecret;

        return DigestUtil.md5Hex(toEncrypt);

    }

}
