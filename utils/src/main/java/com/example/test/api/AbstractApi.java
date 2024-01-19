package com.example.test.api;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public abstract class AbstractApi {

    abstract Map<String, String> getAuthHeaders();

    abstract String doGet(String url, Object params) throws Exception;

    abstract String doPost(String url, Object params, String dateFormat) throws Exception;

}
