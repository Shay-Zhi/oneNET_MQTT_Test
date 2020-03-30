package myMqtt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import myMqtt.beans.DataStreamTemplate;
import myMqtt.beans.DeviceInfo;
import myMqtt.beans.DeviceKey;
import myMqtt.beans.DeviceLocation;

import java.util.ArrayList;
import java.util.Date;

public class JsonUtils {
    /***
     * �����յ����豸��ϢJSON�ַ���������Bean����
     * @param deviceInfo �豸JSON�ַ���
     */
    public void parseDeviceJsonInfo(String deviceInfo){
        JSONObject jsonObject = JSONObject.parseObject(deviceInfo);
        // ȡ��data�Ĳ���
        jsonObject = jsonObject.getJSONObject("data");
        System.out.println(jsonObject);

        Boolean isPrivate = jsonObject.getBoolean("private");         // �Ƿ�Ϊ˽���豸
        Date createTime = jsonObject.getDate("create_time");    // ����ʱ��
        Date actTime = jsonObject.getDate("act_time");          // ʱ��
        String authInfo = jsonObject.getString("auth_info");        // ��Ȩ��Ϣ
        String lastCt = jsonObject.getString("last_ct");            // ��һ���޸�ʱ��
        String title = jsonObject.getString("title");               // �豸����
        String protocol = jsonObject.getString("protocol");         // ʹ�õ�Э������
        Boolean online = jsonObject.getBoolean("online");           // �Ƿ�����
        String id = jsonObject.getString("id");                     // �豸ID

        JSONArray keys = jsonObject.getJSONArray("keys");
        System.out.println(id);
        ArrayList<DeviceKey> deviceKeys = new ArrayList<>();                         // �豸api-key
        for(int i = 0; i < keys.size(); i++){
            JSONObject key = keys.getJSONObject(i);
            String keyTitle = key.getString("title");
            String devKey = key.getString("key");
            // ����key���ݵ�DeviceKey����
            DeviceKey deviceKey = new DeviceKey();
            deviceKey.setTitle(keyTitle);
            deviceKey.setKey(devKey);
            System.out.println(deviceKey);
            // ��������뼯��
            deviceKeys.add(deviceKey);
        }

        JSONArray tags = jsonObject.getJSONArray("tags");
        ArrayList<String> tagsList = null;
        for(int i = 0; i < tags.size(); i++){
            JSONObject tag = tags.getJSONObject(i);
            // ��ʱδ֪tags�����
        }

        JSONObject location = jsonObject.getJSONObject("location");
        Float lat = location.getFloat("lat");
        Float lon = location.getFloat("lon");
        DeviceLocation deviceLocation = new DeviceLocation();           // �豸λ��
        deviceLocation.setLat(lat);
        deviceLocation.setLon(lon);

        JSONArray dataStreams = jsonObject.getJSONArray("datastreams");
        ArrayList<DataStreamTemplate> dataStreamList = new ArrayList<>();            // �豸��������ģ��
        for(int i = 0; i < dataStreams.size(); i++){
            JSONObject dataStream = dataStreams.getJSONObject(i);
            String unit = dataStream.getString("unit");
            String temId = dataStream.getString("id");
            String unitSymbol = dataStream.getString("unit_symbol");
            Date creatTime = dataStream.getDate("create_time");
            // ����key���ݵ�DeviceKey����
            DataStreamTemplate dataStreamTemplate = new DataStreamTemplate();
            dataStreamTemplate.setUnit(unit);
            dataStreamTemplate.setId(temId);
            dataStreamTemplate.setUnitSymbol(unitSymbol);
            dataStreamTemplate.setCreateTime(creatTime);
            // ��������뼯��
            dataStreamList.add(dataStreamTemplate);
        }

        // ���������ݱ��浽DeviceInfo����
        DeviceInfo device = new DeviceInfo();
        device.setPrivate(isPrivate);
        device.setCreate_time(createTime);
        device.setAct_time(actTime);
        device.setKeys(deviceKeys);
        device.setAuth_info(authInfo);
        device.setTitle(title);
        device.setTags(tagsList);
        device.setProtocol(protocol);

        System.out.println(device);
    }

    /***
     * ������ȡ�����豸��ʷ����
     * @param dataPoint ��ȡ���豸��ʷ����JSON
     * @return JSONArray
     */
    public JSONArray parseDataPointsJson(String dataPoint){
        // ��StringתΪJSONObject
        JSONObject jsonObject = JSONObject.parseObject(dataPoint);
        // ��ȡ��ӦJSON�����������ݲ���
        jsonObject = jsonObject.getJSONObject("data");
        System.out.println("jsonObject: " + jsonObject);
        // ��ȡ���������ݵ�����
        String count = jsonObject.getString("count");
        System.out.println("count: " + count);
        // ��ȡ������ģ�����������
        JSONArray datastreams = jsonObject.getJSONArray("datastreams");
        System.out.println("datastreams: " + datastreams);

        // ��ȡ�˴λ�ȡ��ÿ��ģ���е����ݵ�����
        int dataPintCount = datastreams.getJSONObject(1).getJSONArray("datapoints").size();
        System.out.println("datapoint count: " + dataPintCount);
        // ��datastreams�������datapoint�����ݴ���һ��
        JSONArray resultJson = new JSONArray();     // �����������������ļ�¼
        for(int i = 0; i < dataPintCount; i ++){
            // ���е�i��������¼��ƴ��
            JSONObject obj = new JSONObject();      // �������յ��������ļ�¼
            for(int j = 0; j < datastreams.size(); j ++){
                JSONObject point = datastreams.getJSONObject(j);
                // ��ǰdatapoint���ֶ��������ֶ�ֵ���ֶ�ֵ����dataPintCount����ֻȡ��i��
                String fieldName = point.getString("id");
                String fieldValue = JSONObject.parseObject(point.getJSONArray("datapoints")
                                              .get(i).toString()).getString("value");
                //System.out.println("fieldName: " + fieldName + " \nfieldValue: "
                //      + JSONObject.parseObject(point.getJSONArray("datapoints").get(i).toString()).getString("value"));
                obj.put(fieldName, fieldValue);
                //System.out.println(">>> Now obj: " + obj);
                continue;   // ȡ����ǰpoint�ĵ�i��ֵ�󣬽�����һ��point�Ĵ���
            }
            resultJson.add(obj);
            //System.out.println(">>> Now resultJson: " + resultJson);
        }

        return resultJson;
    }
}
