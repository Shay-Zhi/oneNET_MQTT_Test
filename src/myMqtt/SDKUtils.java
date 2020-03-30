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
 * ��������oneNET API�Ĺ�����
 * @author Shay Zhi
 * @version 2020-03-27
 */
public class SDKUtils {
    // oneNET APi��ַ�Ĺ̶�����
    private String oneNETApiBaseUrl = "http://api.heclouds.com";

    /***
     * ��ѯ�����豸����Ϣ
     * @param deviceId �豸��ID����ΪURL�������������URL��
     * @param apiKey �豸��api-key�������HttpGet��ͷ�������������֤
     */
    public void getOneDeviceInfo(String deviceId, String apiKey){
        // ��ѯ�豸��API��ַ������Ϊ�豸ID
        String extraApiUrl = "/devices/" + deviceId;

        // ��ȡHTTP�Ŀͻ��˶���
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // ��������
        StringBuffer params = new StringBuffer();
        params.append("device_id="+deviceId);
        // ����GET�������
        HttpGet httpGet = new HttpGet(oneNETApiBaseUrl + extraApiUrl + "?" + params);
        // ��Ӧ
        CloseableHttpResponse httpResponse = null;
        // ������Ϣ
        RequestConfig requestConfig = RequestConfig.custom()
                                        .setConnectTimeout(5000)
                                        .setConnectionRequestTimeout(5000)
                                        .setSocketTimeout(5000)
                                        .setRedirectsEnabled(true)
                                        .build();
        // ����GET����
        httpGet.setConfig(requestConfig);
        httpGet.addHeader("api-key", apiKey);

        try {
            // ����GET����
            httpResponse = httpClient.execute(httpGet);

            // ��ȡ��Ӧ������
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                String responseString = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                System.out.println("Response: " + responseString);
                JsonUtils jsonUtils = new JsonUtils();
                jsonUtils.parseDeviceJsonInfo(responseString);
            } else {
                System.out.println("Response: NULL");
            }

            // �ͷ���Դ
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
     * ������ѯ��������ֻ�ܲ�ѯ���µ��������������ݣ�Ҳ����1����
     * @param deviceId �豸ID
     * @param apiKey �豸api-key
     * @param dataStreamIds ������ģ��ID�����ID�ö��Ÿ������޲���Ĭ�ϲ�ѯ����������ģ��
     */
    public void getDataStreams(String deviceId, String apiKey, ArrayList<String> dataStreamIds){
        // ��ѯ�豸��API��ַ������Ϊ�豸ID
        String extraApiUrl = "/devices/" + deviceId + "/datastreams";

        // ��ȡHTTP�Ŀͻ��˶���
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // �������֣���
        //StringBuffer params = new StringBuffer();
        //params.append("datastream_ids="+dataStreamIds);

        // ����GET�������
        //HttpGet httpGet = new HttpGet(oneNETApiBaseUrl + extraApiUrl + "?" + params);
        HttpGet httpGet = new HttpGet(oneNETApiBaseUrl + extraApiUrl);

        // ��Ӧ
        CloseableHttpResponse httpResponse = null;
        // ������Ϣ
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .setSocketTimeout(5000)
                .setRedirectsEnabled(true)
                .build();
        // ����GET����
        httpGet.setConfig(requestConfig);
        httpGet.addHeader("api-key", apiKey);

        try {
            // ����GET����
            httpResponse = httpClient.execute(httpGet);

            // ��ȡ��Ӧ������
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                String responseString = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                System.out.println("Response: " + responseString);
                JsonUtils jsonUtils = new JsonUtils();
                //jsonUtils.parseDeviceJsonInfo(responseString);
            } else {
                System.out.println("Response: NULL");
            }

            // �ͷ���Դ
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
     * ��ѯ�豸��ʷ����
     * @param deviceId String �豸ID
     * @param apiKey String �豸api-key
     * @param DataStreamId String ������
     * @param start String ��ȡ���ݵ�Ŀ�ʼʱ�䣨��ȷ���룬2015-01-10T08:00:35��
     * @param end String ��ȡ���ݵ�Ľ���ʱ�䣨��ȷ���룬2015-01-10T08:00:35��
     * @param duration int ��ѯʱ�����䣬��λΪ��
     * @param limit int �޶�����������෵�ص����ݵ�����Ĭ��100����ΧΪ(0,6000]
     * @param cursor String ָ���������������cursorλ�ÿ�ʼ��ȡ����
     * @param sort Enum ʱ������ʽ��DESC������ASC������Ĭ��ΪASC
     */
    public JSONArray getDeviceDataPoint(String deviceId, String apiKey, String DataStreamId, String start, String end,
                                        int duration, int limit, String cursor, Enum sort){
        // ��ѯ�豸��API��ַ������Ϊ�豸ID
        String extraApiUrl = "/devices/" + deviceId + "/datapoints";

        // ��ȡHTTP�Ŀͻ��˶���
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // �������֣���
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

        // ����GET�������
        HttpGet httpGet = new HttpGet(oneNETApiBaseUrl + extraApiUrl + "?" + params);
        //HttpGet httpGet = new HttpGet(oneNETApiBaseUrl + extraApiUrl);

        // ��Ӧ
        CloseableHttpResponse httpResponse = null;
        // ������Ϣ
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .setSocketTimeout(5000)
                .setRedirectsEnabled(true)
                .build();
        // ����GET����
        httpGet.setConfig(requestConfig);
        httpGet.addHeader("api-key", apiKey);

        try {
            // ����GET����
            httpResponse = httpClient.execute(httpGet);

            // ��ȡ��Ӧ������
            HttpEntity entity = httpResponse.getEntity();
            String responseString = null;
            if (entity != null) {
                responseString = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                System.out.println("Response: " + responseString);
            } else {
                System.out.println("Response: NULL");
            }

            // �ͷ���Դ
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
     * ��ȡ��ǰʱ��֮ǰ5�����ǿ̵�ʱ���ַ���
     * @return ʱ���ַ���
     */
    public String get5MinutesAgo(){
        Calendar beforeTime = Calendar.getInstance();
        beforeTime.add(Calendar.MINUTE, -5); // 5����֮ǰ��ʱ��
        Date beforeD = beforeTime.getTime();
        String before5 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(beforeD).replace(" ", "T");  // ��ʽ��
        return before5;
    }

}
