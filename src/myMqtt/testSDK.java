package myMqtt;

import com.alibaba.fastjson.JSONArray;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class testSDK {
    public static void main(String args[]){
        SDKUtils sdk = new SDKUtils();
        String deviceId = "588189102";
        String apiKey = "qNOpUeqG1WP4=yTuNRQJqSNu8Qk=";

        /**
         * ���ԣ���ѯ�����豸��Ϣ
         */
        //sdk.getOneDeviceInfo(deviceId, apiKey);

        /**
         * ���ԣ�������ѯ������
         */
        //sdk.getDataStreams(deviceId, apiKey, new ArrayList<String>());

        /**
         * ���ԣ���ѯ�豸��ʷ����
         */
        //Date date = new Date();
        //DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //String start = df.format(date).replace(" ", "T");
        String start = sdk.get5MinutesAgo();
        //System.out.println(start == start1);
        //System.out.println(start);
        //System.out.println(start1);
        JSONArray arr = sdk.getDeviceDataPoint(deviceId, apiKey, null, start, null, 0, 1000, null, null);
        System.out.println("��õ��豸��ʷ���� [" + start + " ~]��" + arr);
    }
}
