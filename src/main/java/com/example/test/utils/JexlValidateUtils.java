package com.example.test.utils;

import cn.hutool.extra.expression.engine.jexl.JexlEngine;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;

import java.math.BigDecimal;
import java.util.Map;

public class JexlValidateUtils {

    private static final JexlEngine jexl = new JexlEngine();

    public static boolean validateMapWithExpression(Map<String, Object> map, String expression) {

        JexlExpression jexlExpression = jexl.getEngine().createExpression(expression);

        JexlContext context = new MapContext();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            context.set(entry.getKey(), entry.getValue());
        }

        // 计算表达式
        Object result = jexlExpression.evaluate(context);

        // 判断表达式的结果是否为true
        return result instanceof Boolean && (Boolean) result;
    }
}
