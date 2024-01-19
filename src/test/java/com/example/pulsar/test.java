package com.example.pulsar;


import com.example.test.PulsarApplication;
import com.example.test.api.ApiInstance;
import com.example.test.service.TestService;
import com.example.test.utils.JexlValidateUtils;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@SpringBootTest(classes = PulsarApplication.class)
public class test {
    @Autowired
    private  ApiInstance apiInstance;

    /**
     *
     *
     * @author wyl
     * @since 2023/8/25
     */
    @Test
    public void testDoubleDice(){
        // n表示骰子个数
        int n =3;
        // 每个骰子的最大点数
        int dice = 20;
        //一个骰子骰出某一个值的概率
        double probability = 1d/dice;
        double[] dp = new double[dice + 1];         // dp[i][j]表示第总共i个骰子，掷出的结果为j的概率
        //i代表多骰取高的最终结果
        for(int i = 1 ; i <= dice;i++){
            double temp = 0;
            //求i个骰子时，所有骰子骰出的数值小于等于j的概率
            for(int t = 1 ;t <= i; t++){
                //每个骰子都少于i的概率
                temp += probability;
            }
            //求所有骰子都少于i的概率
            double pow = Math.pow(temp, n);
            //
            if(i > 1){
                double temp2 = 0;
                for(int j = 1;j < i;j++){
                    //求多骰取高的概率小于i的概率，即i之前的所有概率加起来
                    temp2 += dp[j];
                }
                pow -= temp2;
            }
            dp[i] = pow;
        }
        double result = 0;
        for(int i = 1;i<=dice;i++){
            result += dp[i] * Double.parseDouble(String.valueOf(i));
        }

        System.out.println(result);
    }

    /**
     *
     *
     * @author wyl
     * @since 2023/8/22
     * @param map (key:各个骰子数值， value:对应概率)
     *        n   (投掷n次取最高)
     */
    @Test
    public double getMaxDice(Map<Integer, Double> map, Integer n){

        //list按key大小排序
        List<Map.Entry<Integer, Double>> list = map.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toList());

        //计算后的概率结果,key为值，value为概率
        List<Map.Entry<Integer, Double>> dp = new ArrayList<>();

        for(int i = 0 ;i < list.size(); i++){
            Map.Entry<Integer, Double> entry = list.get(i);
            //计算所有n次的骰子全部小于或等于num的概率，减去所有骰子取高最终结果小于num的概率，结果就是最终结果是num的概率
            Double probability = entry.getValue();
            Double proLessNum = 0D;
            if(i > 0){
                for(int j = i - 1 ;j >= 0 ; j--){
                    probability += list.get(j).getValue();
                    proLessNum += dp.get(j).getValue();
                }
            }
            double pow = Math.pow(probability, n);
            AbstractMap.SimpleEntry<Integer, Double> resultEntry = new AbstractMap.SimpleEntry<>(entry.getKey(), pow - proLessNum);
            dp.add(resultEntry);
        }
        //计算期望
        AtomicReference<Double> result = new AtomicReference<>(0D);
        dp.forEach(
                entry -> result.updateAndGet(v -> v + entry.getKey() * entry.getValue())
        );

        System.out.println(result);
        return result.get();
    }


    /**
     * 获取多骰总值的概率
     *
     * @author wyl
     * @since 2023/8/25
     * @param dice 各个骰子的最大值，如｛7,6｝就是一个1d7和一个1d6的骰子
     */
    public Map<Integer, Double> dicesProbability(List<Integer> dice) {
        System.out.println("骰子组成");
        dice.forEach(i -> System.out.println("1d"+i));

        Integer n = dice.size();
        double[] dp = new double[dice.get(0) + 1];
        Arrays.fill(dp, 1.0 / dice.get(0));
        dp[0]= 0;
        Integer maxSum = dice.get(0);
        for (int i = 2; i <= n; i++) {
            Integer max = dice.get(i - 1);
            double[] tmp = new double[max + dp.length + 1];
            for (int j = 1; j < dp.length; j++) {
                for (int k = 1; k <= dice.get(i - 1); k++) {
                    tmp[j + k] += dp[j] / dice.get(i-1) ;
                }
            }
            dp = tmp;
        }
        Map<Integer, Double> result = new HashMap<>();
        for(int i = 0 ;i < dp.length; i++){
            System.out.println("总值为" + i + "的概率为:" + dp[i]);
            result.put(i, dp[i]);
        }

        return result;
    }



    @Test
    public void testDice(){
        /*Map<Integer, Double> map = new HashMap<>();
        map.put(1, 1D/6);
        map.put(2, 1D/6);
        map.put(3, 1D/6);
        map.put(4, 1D/6);
        map.put(5, 1D/6);
        map.put(6, 1D/6);
        getMaxDice(map, 1);*/

        List<Integer> diceList = new ArrayList<>();
        double hitRate = 1;

        diceList.add(8);
        diceList.add(8);
        diceList.add(8);
        diceList.add(8);
        diceList.add(8);
        diceList.add(8);
        diceList.add(8);
        diceList.add(8);
        Map<Integer, Double> map = dicesProbability(diceList);
        Integer n = 1;
        System.out.println("此概率骰" + 2 + "次取最高，命中率为" +hitRate +"，期望为" + getMaxDice(map, n) * hitRate);

      /*  List<Integer> diceList = new ArrayList<>();
        double hitRate = 0.45;
        System.out.println("永燃之刃伤害骰");
        System.out.println("减5加10");
        diceList.add(6);
        diceList.add(6);
        diceList.add(4);
        Map<Integer, Double> map = dicesProbability(diceList);
        Integer n = 1;
        System.out.println("此概率骰" + 1 + "次取最高再加10，命中率为" + hitRate +"，期望为" + (getMaxDice(map, n)+10) * hitRate);*/

    }

    @Test
    public void testLocalThread() throws InterruptedException {
        TestService testService = new TestService();
        testService.testThreadLocal();
    }

    @Test
    public void testReplace(){
        String link = "http://";

        String newLink = replaceIpAndPort(link);

        System.out.println(newLink);
    }

    public String replaceIpAndPort(String inputUrl) {
        // 匹配URL中的IP地址和端口
        Pattern pattern = Pattern.compile("^(http[s]?://)([^:/]+)(:([^/]+))?(.*)$");
        Matcher matcher = pattern.matcher(inputUrl);

        if (matcher.matches()) {
            String protocol = matcher.group(1);
            String ipAddress = "112.94.70.154";
            String port = ":20102";
            String domain = matcher.group(2);
            String path = matcher.group(5);

            // 构建替换后的URL
            String replacedUrl = protocol + ipAddress + port + path;
            return replacedUrl;
        } else {
            // 如果输入的URL不匹配正则表达式，则返回原始URL
            return inputUrl;
        }
    }

    @Test
    public void testJexl(){
        Map<String, Object> velocityMap = new HashMap<>();
        velocityMap.put("velocity", new BigDecimal("10.0"));
        velocityMap.put("speed", 20);
        velocityMap.put("velocity3", new BigDecimal("30.0"));
        velocityMap.put("velocity4", new BigDecimal("10.0"));
        String expression = "velocity + speed == velocity4 || velocity4 == 10";

        boolean isValid = JexlValidateUtils.validateMapWithExpression(velocityMap, expression);

        System.out.println("Map values validation result: " + isValid);
    }
    @Test
    public void testApi() throws Exception {
        String baidu = apiInstance.postBaidu();
    }
}

