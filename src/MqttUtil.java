import android.content.Context;
import android.util.Log;
import org.eclipse.paho.android.service.MqttAndroidClient;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttClient;

import java.util.ArrayList;

/**
* Created by CHNhawk on 2017/3/7
* 重庆工商大学 物联网工程 熊廷宇
* update on 2017/5/16
* 添加了消息队列转发以及MsgHandler接口
* 支持更好地用于多Activity
*
*/

public class MqttUtil {

    //region 网络设定
    private static String serverIP = "183.230.40.39";               // 服务器ip
    private static int serverPort = 6002;                           // 服务器端口
    private static String deviceId = "填设备id";                            // 终端id
    private static String userId = "填应用id";                           // 服务器登陆名
    private static String password = "填鉴权或apiKey";                         // 服务器登陆密码
    //endregion

    //region 消息类型
    public static final int MQTT_CONNECTED = 1000;                 // mqtt连接成功
    public static final int MQTT_CONNECTFAIL = 1001;               // mqtt连接失败
    public static final int MQTT_DISCONNECT = 1002;                // mqtt断开连接

    public static final int MQTT_SUBSCRIBED = 1010;                // 订阅成功
    public static final int MQTT_SUBSCRIBEFAIL = 1011;             // 订阅失败

    public static final int MQTT_MSG_TEST = 2001;                  // 接收到TEST消息

    public static final int MQTT_PUBLISHED = 2010;                 // 发布成功
    public static final int MQTT_PUBLISHFAIL = 2011;               // 发布失败
    //endregion

    //region MQTT客户端服务
    private static MqttUtil instance;                               // 单例对象
    private MqttAndroidClient mqttClient;                           // mqtt客户端
    private MqttConnectOptions option;                              // mqtt设置
    private MqttCallback clientCallback;                            // 客户端回调
    private Context mContext;                                       // 上下文
    private ArrayList<MsgHandler> listenerList = new ArrayList<>(); // 消息接收者
    //endregion

    //封闭构造函数
    private MqttUtil(){}

    //获取单例
    public static MqttUtil getInstance(){
        if(instance == null){
            instance = new MqttUtil();
            //deviceId =  android.os.Build.SERIAL + (int)(Math.random() * 10);
        }
        return instance;
    }

    //初始化连接
	  public void initMqtt(Context context) { 
		  mContext = context; 
		  connect(); 
	  }
	 
	/*
	 * public void initMqtt() { connect(); }
	 */

    //重连
    public void reConnect() {
        //尝试重连
        connect();

    }

    //发布消息
    public void publish(String topic, String payload) {
        MqttMessage msg = new MqttMessage();
        msg.setPayload(payload.getBytes());
        msg.setQos(1);
        if (mqttClient != null){
            if(mqttClient.isConnected()) {
                try {
                    mqttClient.publish(topic, msg);
                    DispachEvent(MQTT_PUBLISHED);
                } catch (MqttException e) {
                    //发布失败
                    Log.e("mqtt", "发布失败" + e);
                	//System.out.println("发布失败：\n" + e);
                    DispachEvent(MQTT_PUBLISHFAIL);
                }
            } else {
                Log.e("mqtt", "网络未连接 - 尝试重新连接" );
            	//System.out.println("网络未连接 - 尝试重新连接\n");
                connect();
                DispachEvent(MQTT_PUBLISHFAIL);
            }
        } else {
            Log.e("mqtt", "客户端初始化失败" );
        	//System.out.println("客户端初始化失败\n");
            DispachEvent(MQTT_PUBLISHFAIL);
        }
    }

    //订阅主题
    public void subscribe(String topic){
        try {
            //订阅消息
            mqttClient.subscribe(topic, 0);
            DispachEvent(MQTT_SUBSCRIBED);
        } catch (MqttException e) {
            DispachEvent(MQTT_SUBSCRIBEFAIL);
            Log.e("mqtt", "订阅错误:" + e);
        }
    }

    //返回是否连接
    public boolean isConnected(){
        return mqttClient.isConnected();
    }

    //获取本机deviceId
    public String getDeviceId(){
        return deviceId;
    }

    //连接
    private void connect() {
        if (mqttClient == null) {
            option = new MqttConnectOptions();
            option.setUserName(userId);
            option.setPassword(password.toCharArray());
            option.setCleanSession(false);
            //设置回调
            clientCallback = new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    //断开连接
                    DispachEvent(MQTT_DISCONNECT);
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    //接收到消息
                    Log.v("mqtt", "接收到信息:" + topic);
                    DispachMessage(topic, message);
                }
                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    //publish成功后调用
                    Log.v("mqtt","发送成功");
                }
            };
            try {
                mqttClient = new MqttAndroidClient(mContext, ("tcp://" + serverIP + ":" + serverPort), deviceId);
                MqttClient mqttClient1 = new MqttClient(("tcp://" + serverIP + ":" + serverPort), deviceId);
                mqttClient.setCallback(clientCallback);
            } catch (Exception e) {
                Log.e("mqtt", "启动服务错误:" + e);
            }
        }
        if (!mqttClient.isConnected()) {
            //匿名连接线程
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        int count = 0;  //重试次数
                        while(count < 5 && !mqttClient.isConnected()) {
                            mqttClient.connect(option);
                            Thread.sleep(1000);
                            count ++;
                        }
                        //连接成功
                        if(mqttClient.isConnected()) {
                            DispachEvent(MQTT_CONNECTED);
                            Log.v("mqtt", "连接成功");
                        } else {
                            Log.e("mqtt", "连接网络错误");
                            DispachEvent(MQTT_CONNECTFAIL);
                        }
                    } catch (Exception e) {
                        Log.e("mqtt", "连接错误:" + e);
                        DispachEvent(MQTT_CONNECTFAIL);
                    }
                }
            }).start();
        }
    }

    //region 消息转发部分
    //添加接收者
    public void addListener(MsgHandler msgHandler){
        if(!listenerList.contains(msgHandler)) {
            listenerList.add(msgHandler);
        }
    }

    //移除接收者
    public void removeListener(MsgHandler msgHandler){
        listenerList.remove(msgHandler);
    }

    //移除所有接收者
    public void removeAll(){
        listenerList.clear();
    }

    //发送消息
    public void DispachMessage(String type, Object data){
        if(listenerList.isEmpty()) {
            Log.v("mqtt", "没有消息接收者:" + type);
            return;
        }
        Log.v("mqtt", "发送消息:" + type);
        for (MsgHandler msgHandler : listenerList)
        {
            msgHandler.onMessage(type, data);
        }
    }

    //发送事件
    public void DispachEvent(int event){
        if(listenerList.isEmpty()) {
            Log.v("mqtt", "没有消息接收者:");
            return;
        }
        Log.v("mqtt", "派发事件:" + event);
        for (MsgHandler msgHandler : listenerList)
        {
            msgHandler.onEvent(event);
        }
    }
    //endregion

}
