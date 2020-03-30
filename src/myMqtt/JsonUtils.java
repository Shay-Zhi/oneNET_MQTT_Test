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
     * 将接收到的设备信息JSON字符串解析成Bean对象
     * @param deviceInfo 设备JSON字符串
     */
    public void parseDeviceJsonInfo(String deviceInfo){
        JSONObject jsonObject = JSONObject.parseObject(deviceInfo);
        // 取出data的部分
        jsonObject = jsonObject.getJSONObject("data");
        System.out.println(jsonObject);

        Boolean isPrivate = jsonObject.getBoolean("private");         // 是否为私密设备
        Date createTime = jsonObject.getDate("create_time");    // 创建时间
        Date actTime = jsonObject.getDate("act_time");          // 时间
        String authInfo = jsonObject.getString("auth_info");        // 鉴权信息
        String lastCt = jsonObject.getString("last_ct");            // 上一次修改时间
        String title = jsonObject.getString("title");               // 设备名称
        String protocol = jsonObject.getString("protocol");         // 使用的协议名称
        Boolean online = jsonObject.getBoolean("online");           // 是否在线
        String id = jsonObject.getString("id");                     // 设备ID

        JSONArray keys = jsonObject.getJSONArray("keys");
        System.out.println(id);
        ArrayList<DeviceKey> deviceKeys = new ArrayList<>();                         // 设备api-key
        for(int i = 0; i < keys.size(); i++){
            JSONObject key = keys.getJSONObject(i);
            String keyTitle = key.getString("title");
            String devKey = key.getString("key");
            // 保存key数据到DeviceKey对象
            DeviceKey deviceKey = new DeviceKey();
            deviceKey.setTitle(keyTitle);
            deviceKey.setKey(devKey);
            System.out.println(deviceKey);
            // 将对象加入集合
            deviceKeys.add(deviceKey);
        }

        JSONArray tags = jsonObject.getJSONArray("tags");
        ArrayList<String> tagsList = null;
        for(int i = 0; i < tags.size(); i++){
            JSONObject tag = tags.getJSONObject(i);
            // 暂时未知tags的组成
        }

        JSONObject location = jsonObject.getJSONObject("location");
        Float lat = location.getFloat("lat");
        Float lon = location.getFloat("lon");
        DeviceLocation deviceLocation = new DeviceLocation();           // 设备位置
        deviceLocation.setLat(lat);
        deviceLocation.setLon(lon);

        JSONArray dataStreams = jsonObject.getJSONArray("datastreams");
        ArrayList<DataStreamTemplate> dataStreamList = new ArrayList<>();            // 设备的数据流模板
        for(int i = 0; i < dataStreams.size(); i++){
            JSONObject dataStream = dataStreams.getJSONObject(i);
            String unit = dataStream.getString("unit");
            String temId = dataStream.getString("id");
            String unitSymbol = dataStream.getString("unit_symbol");
            Date creatTime = dataStream.getDate("create_time");
            // 保存key数据到DeviceKey对象
            DataStreamTemplate dataStreamTemplate = new DataStreamTemplate();
            dataStreamTemplate.setUnit(unit);
            dataStreamTemplate.setId(temId);
            dataStreamTemplate.setUnitSymbol(unitSymbol);
            dataStreamTemplate.setCreateTime(creatTime);
            // 将对象加入集合
            dataStreamList.add(dataStreamTemplate);
        }

        // 将所有数据保存到DeviceInfo对象
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
     * 解析获取到的设备历史数据
     * @param dataPoint 获取的设备历史数据JSON
     * @return JSONArray
     */
    public JSONArray parseDataPointsJson(String dataPoint){
        // 将String转为JSONObject
        JSONObject jsonObject = JSONObject.parseObject(dataPoint);
        // 获取响应JSON的数据流数据部分
        jsonObject = jsonObject.getJSONObject("data");
        System.out.println("jsonObject: " + jsonObject);
        // 获取数据流数据的数量
        String count = jsonObject.getString("count");
        System.out.println("count: " + count);
        // 获取数据流模板的所有数据
        JSONArray datastreams = jsonObject.getJSONArray("datastreams");
        System.out.println("datastreams: " + datastreams);

        // 获取此次获取的每个模板中的数据的数量
        int dataPintCount = datastreams.getJSONObject(1).getJSONArray("datapoints").size();
        System.out.println("datapoint count: " + dataPintCount);
        // 将datastreams里的所有datapoint的数据串在一起
        JSONArray resultJson = new JSONArray();     // 用来接收所有完整的记录
        for(int i = 0; i < dataPintCount; i ++){
            // 进行第i条完整记录的拼接
            JSONObject obj = new JSONObject();      // 用来接收单条完整的记录
            for(int j = 0; j < datastreams.size(); j ++){
                JSONObject point = datastreams.getJSONObject(j);
                // 当前datapoint的字段名称与字段值，字段值（有dataPintCount个）只取第i个
                String fieldName = point.getString("id");
                String fieldValue = JSONObject.parseObject(point.getJSONArray("datapoints")
                                              .get(i).toString()).getString("value");
                //System.out.println("fieldName: " + fieldName + " \nfieldValue: "
                //      + JSONObject.parseObject(point.getJSONArray("datapoints").get(i).toString()).getString("value"));
                obj.put(fieldName, fieldValue);
                //System.out.println(">>> Now obj: " + obj);
                continue;   // 取到当前point的第i个值后，进行下一个point的处理
            }
            resultJson.add(obj);
            //System.out.println(">>> Now resultJson: " + resultJson);
        }

        return resultJson;
    }
}
