package com.example.test.api;


import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.RequiredArgsConstructor;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;


import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

/**
 * 智能城管案件接口 对接数字城管
 *
 * @author wyl
 * @since 2023/9/8
 */
@Service
@RequiredArgsConstructor
public abstract class Api extends AbstractApi {
	private final Integer SUCCESS_STATUS = 200;

	@Override
	abstract Map<String, String> getAuthHeaders();


	public String formatLocalDateTime(LocalDateTime localDateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return localDateTime.format(formatter);
	}

	@Override
	public String doGet(String url, Object params) throws Exception {
		Map<String, String> authHeaders = getAuthHeaders();
		String finalUrl = url + buildUrlParamFromObject(params);
		HttpRequest httpRequest = HttpRequest.get(finalUrl);
		httpRequest.addHeaders(authHeaders);
		HttpResponse execute = httpRequest.execute();
		String body = execute.body();
		return body;
	}

	@Override
	public String doPost(String url, Object params, String dateFormat) throws Exception {
		Map<String, String> authHeaders = getAuthHeaders();

		HttpRequest httpRequest = HttpRequest.post(url);
		httpRequest.addHeaders(authHeaders);
		if(StringUtils.isBlank(dateFormat)){
			httpRequest.body(JSON.toJSONString(params));
		}else {
			httpRequest.body(JSON.toJSONStringWithDateFormat(params, dateFormat));
		}
		HttpResponse execute = httpRequest.execute();
		String body = execute.body();
		return body;
	}

	public String buildUrlParamFromObject(Object object) throws Exception {
		if(object == null){
			return "";
		}
		StringBuilder urlBuilder = new StringBuilder();
		Field[] fields = object.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			String fieldName = field.getName();
			Object fieldValue;
			fieldValue = field.get(object);

			if (fieldValue != null) {
				if (urlBuilder.length() > 0) {
					urlBuilder.append("&");
				}else{
					urlBuilder.append("?");
				}

				String fieldValueString;
				if (fieldValue instanceof LocalDateTime) {
					fieldValueString = formatLocalDateTime((LocalDateTime) fieldValue);
				} else {
					fieldValueString = fieldValue.toString();
				}
				urlBuilder.append(URLEncoder.encode(fieldName, "UTF-8"))
					.append("=")
					.append(URLEncoder.encode(fieldValueString, "UTF-8"));
			}
		}

		return urlBuilder.toString();
	}


}
