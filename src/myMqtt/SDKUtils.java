package myMqtt;

import com.alibaba.fastjson.JSONArray;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/***
 * 用来调用oneNET API的工具类
 * @author Shay Zhi
 * @version 2020-03-27
 */
public class SDKUtils {
    // oneNET APi地址的固定部分
    private String oneNETApiBaseUrl = "http://api.heclouds.com";

    /***
     * 查询单个设备的信息
     * @param deviceId 设备的ID，作为URL参数添加在请求URL中
     * @param apiKey 设备的api-key，添加在HttpGet的头部，用于身份验证
     */
    public void getOneDeviceInfo(String deviceId, String apiKey){
        // 查询设备的API地址，参数为设备ID
        String extraApiUrl = "/devices/" + deviceId;

        // 获取HTTP的客户端对象
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 参数部分
        StringBuffer params = new StringBuffer();
        params.append("device_id="+deviceId);
        // 创建GET请求对象
        HttpGet httpGet = new HttpGet(oneNETApiBaseUrl + extraApiUrl + "?" + params);
        // 响应
        CloseableHttpResponse httpResponse = null;
        // 配置信息
        RequestConfig requestConfig = RequestConfig.custom()
                                        .setConnectTimeout(5000)
                                        .setConnectionRequestTimeout(5000)
                                        .setSocketTimeout(5000)
                                        .setRedirectsEnabled(true)
                                        .build();
        // 配置GET请求
        httpGet.setConfig(requestConfig);
        httpGet.addHeader("api-key", apiKey);

        try {
            // 发送GET请求
            httpResponse = httpClient.execute(httpGet);

            // 获取响应请求体
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                String responseString = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                System.out.println("Response: " + responseString);
                JsonUtils jsonUtils = new JsonUtils();
                jsonUtils.parseDeviceJsonInfo(responseString);
            } else {
                System.out.println("Response: NULL");
            }

            // 释放资源
            if (httpClient != null) {
                httpClient.close();
            }
            if (httpResponse != null) {
                httpResponse.close();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /***
     * 批量查询数据流（只能查询最新的所有数据流数据，也就是1条）
     * @param deviceId 设备ID
     * @param apiKey 设备api-key
     * @param dataStreamIds 数据量模板ID。多个ID用逗号隔开。无参则默认查询所有数据量模板
     */
    public void getDataStreams(String deviceId, String apiKey, ArrayList<String> dataStreamIds){
        // 查询设备的API地址，参数为设备ID
        String extraApiUrl = "/devices/" + deviceId + "/datastreams";

        // 获取HTTP的客户端对象
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // 参数部分（）
        //StringBuffer params = new StringBuffer();
        //params.append("datastream_ids="+dataStreamIds);

        // 创建GET请求对象
        //HttpGet httpGet = new HttpGet(oneNETApiBaseUrl + extraApiUrl + "?" + params);
        HttpGet httpGet = new HttpGet(oneNETApiBaseUrl + extraApiUrl);

        // 响应
        CloseableHttpResponse httpResponse = null;
        // 配置信息
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .setSocketTimeout(5000)
                .setRedirectsEnabled(true)
                .build();
        // 配置GET请求
        httpGet.setConfig(requestConfig);
        httpGet.addHeader("api-key", apiKey);

        try {
            // 发送GET请求
            httpResponse = httpClient.execute(httpGet);

            // 获取响应请求体
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                String responseString = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                System.out.println("Response: " + responseString);
                JsonUtils jsonUtils = new JsonUtils();
                //jsonUtils.parseDeviceJsonInfo(responseString);
            } else {
                System.out.println("Response: NULL");
            }

            // 释放资源
            if (httpClient != null) {
                httpClient.close();
            }
            if (httpResponse != null) {
                httpResponse.close();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /***
     * 查询设备历史数据
     * @param deviceId String 设备ID
     * @param apiKey String 设备api-key
     * @param DataStreamId String 数据流
     * @param start String 提取数据点的开始时间（精确到秒，2015-01-10T08:00:35）
     * @param end String 提取数据点的结束时间（精确到秒，2015-01-10T08:00:35）
     * @param duration int 查询时间区间，单位为秒
     * @param limit int 限定本次请求最多返回的数据点数，默认100，范围为(0,6000]
     * @param cursor String 指定本次请求继续从cursor位置开始提取数据
     * @param sort Enum 时间排序方式，DESC：倒序，ASC：升序，默认为ASC
     */
    public JSONArray getDeviceDataPoint(String deviceId, String apiKey, String DataStreamId, String start, String end,
                                        int duration, int limit, String cursor, Enum sort){
        // 查询设备的API地址，参数为设备ID
        String extraApiUrl = "/devices/" + deviceId + "/datapoints";

        // 获取HTTP的客户端对象
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // 参数部分（）
        StringBuffer params = new StringBuffer();
        if(DataStreamId != null){
            params.append("datastream_id=" + DataStreamId);
            params.append("&");
        }
        if(start != null){
            params.append("start=" + start);
            params.append("&");
        }
        if(end != null){
            params.append("end=" + end);
            params.append("&");
        }
        if(String.valueOf(duration) != null && duration != 0){
            params.append("duration=" + duration);
            params.append("&");
        }
        if(String.valueOf(limit) != null && limit != 0){
            params.append("limit=" + limit);
            params.append("&");
        }
        if(cursor != null){
            params.append("cursor=" + cursor);
        }

        // 创建GET请求对象
        HttpGet httpGet = new HttpGet(oneNETApiBaseUrl + extraApiUrl + "?" + params);
        //HttpGet httpGet = new HttpGet(oneNETApiBaseUrl + extraApiUrl);

        // 响应
        CloseableHttpResponse httpResponse = null;
        // 配置信息
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .setSocketTimeout(5000)
                .setRedirectsEnabled(true)
                .build();
        // 配置GET请求
        httpGet.setConfig(requestConfig);
        httpGet.addHeader("api-key", apiKey);

        try {
            // 发送GET请求
            httpResponse = httpClient.execute(httpGet);

            // 获取响应请求体
            HttpEntity entity = httpResponse.getEntity();
            String responseString = null;
            if (entity != null) {
                responseString = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                System.out.println("Response: " + responseString);
            } else {
                System.out.println("Response: NULL");
            }

            // 释放资源
            if (httpClient != null) {
                httpClient.close();
            }
            if (httpResponse != null) {
                httpResponse.close();
            }

            if(responseString != null){
                return new JsonUtils().parseDataPointsJson(responseString);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    /***
     * 获取当前时间之前5分钟那刻的时间字符串
     * @return 时间字符串
     */
    public String get5MinutesAgo(){
        Calendar beforeTime = Calendar.getInstance();
        beforeTime.add(Calendar.MINUTE, -5); // 5分钟之前的时间
        Date beforeD = beforeTime.getTime();
        String before5 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(beforeD).replace(" ", "T");  // 格式化
        return before5;
    }

}
