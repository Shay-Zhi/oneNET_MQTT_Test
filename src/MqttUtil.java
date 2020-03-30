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
* ���칤�̴�ѧ ���������� ��͢��
* update on 2017/5/16
* �������Ϣ����ת���Լ�MsgHandler�ӿ�
* ֧�ָ��õ����ڶ�Activity
*
*/

public class MqttUtil {

    //region �����趨
    private static String serverIP = "183.230.40.39";               // ������ip
    private static int serverPort = 6002;                           // �������˿�
    private static String deviceId = "���豸id";                            // �ն�id
    private static String userId = "��Ӧ��id";                           // ��������½��
    private static String password = "���Ȩ��apiKey";                         // ��������½����
    //endregion

    //region ��Ϣ����
    public static final int MQTT_CONNECTED = 1000;                 // mqtt���ӳɹ�
    public static final int MQTT_CONNECTFAIL = 1001;               // mqtt����ʧ��
    public static final int MQTT_DISCONNECT = 1002;                // mqtt�Ͽ�����

    public static final int MQTT_SUBSCRIBED = 1010;                // ���ĳɹ�
    public static final int MQTT_SUBSCRIBEFAIL = 1011;             // ����ʧ��

    public static final int MQTT_MSG_TEST = 2001;                  // ���յ�TEST��Ϣ

    public static final int MQTT_PUBLISHED = 2010;                 // �����ɹ�
    public static final int MQTT_PUBLISHFAIL = 2011;               // ����ʧ��
    //endregion

    //region MQTT�ͻ��˷���
    private static MqttUtil instance;                               // ��������
    private MqttAndroidClient mqttClient;                           // mqtt�ͻ���
    private MqttConnectOptions option;                              // mqtt����
    private MqttCallback clientCallback;                            // �ͻ��˻ص�
    private Context mContext;                                       // ������
    private ArrayList<MsgHandler> listenerList = new ArrayList<>(); // ��Ϣ������
    //endregion

    //��չ��캯��
    private MqttUtil(){}

    //��ȡ����
    public static MqttUtil getInstance(){
        if(instance == null){
            instance = new MqttUtil();
            //deviceId =  android.os.Build.SERIAL + (int)(Math.random() * 10);
        }
        return instance;
    }

    //��ʼ������
	  public void initMqtt(Context context) { 
		  mContext = context; 
		  connect(); 
	  }
	 
	/*
	 * public void initMqtt() { connect(); }
	 */

    //����
    public void reConnect() {
        //��������
        connect();

    }

    //������Ϣ
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
                    //����ʧ��
                    Log.e("mqtt", "����ʧ��" + e);
                	//System.out.println("����ʧ�ܣ�\n" + e);
                    DispachEvent(MQTT_PUBLISHFAIL);
                }
            } else {
                Log.e("mqtt", "����δ���� - ������������" );
            	//System.out.println("����δ���� - ������������\n");
                connect();
                DispachEvent(MQTT_PUBLISHFAIL);
            }
        } else {
            Log.e("mqtt", "�ͻ��˳�ʼ��ʧ��" );
        	//System.out.println("�ͻ��˳�ʼ��ʧ��\n");
            DispachEvent(MQTT_PUBLISHFAIL);
        }
    }

    //��������
    public void subscribe(String topic){
        try {
            //������Ϣ
            mqttClient.subscribe(topic, 0);
            DispachEvent(MQTT_SUBSCRIBED);
        } catch (MqttException e) {
            DispachEvent(MQTT_SUBSCRIBEFAIL);
            Log.e("mqtt", "���Ĵ���:" + e);
        }
    }

    //�����Ƿ�����
    public boolean isConnected(){
        return mqttClient.isConnected();
    }

    //��ȡ����deviceId
    public String getDeviceId(){
        return deviceId;
    }

    //����
    private void connect() {
        if (mqttClient == null) {
            option = new MqttConnectOptions();
            option.setUserName(userId);
            option.setPassword(password.toCharArray());
            option.setCleanSession(false);
            //���ûص�
            clientCallback = new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    //�Ͽ�����
                    DispachEvent(MQTT_DISCONNECT);
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    //���յ���Ϣ
                    Log.v("mqtt", "���յ���Ϣ:" + topic);
                    DispachMessage(topic, message);
                }
                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    //publish�ɹ������
                    Log.v("mqtt","���ͳɹ�");
                }
            };
            try {
                mqttClient = new MqttAndroidClient(mContext, ("tcp://" + serverIP + ":" + serverPort), deviceId);
                MqttClient mqttClient1 = new MqttClient(("tcp://" + serverIP + ":" + serverPort), deviceId);
                mqttClient.setCallback(clientCallback);
            } catch (Exception e) {
                Log.e("mqtt", "�����������:" + e);
            }
        }
        if (!mqttClient.isConnected()) {
            //���������߳�
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        int count = 0;  //���Դ���
                        while(count < 5 && !mqttClient.isConnected()) {
                            mqttClient.connect(option);
                            Thread.sleep(1000);
                            count ++;
                        }
                        //���ӳɹ�
                        if(mqttClient.isConnected()) {
                            DispachEvent(MQTT_CONNECTED);
                            Log.v("mqtt", "���ӳɹ�");
                        } else {
                            Log.e("mqtt", "�����������");
                            DispachEvent(MQTT_CONNECTFAIL);
                        }
                    } catch (Exception e) {
                        Log.e("mqtt", "���Ӵ���:" + e);
                        DispachEvent(MQTT_CONNECTFAIL);
                    }
                }
            }).start();
        }
    }

    //region ��Ϣת������
    //��ӽ�����
    public void addListener(MsgHandler msgHandler){
        if(!listenerList.contains(msgHandler)) {
            listenerList.add(msgHandler);
        }
    }

    //�Ƴ�������
    public void removeListener(MsgHandler msgHandler){
        listenerList.remove(msgHandler);
    }

    //�Ƴ����н�����
    public void removeAll(){
        listenerList.clear();
    }

    //������Ϣ
    public void DispachMessage(String type, Object data){
        if(listenerList.isEmpty()) {
            Log.v("mqtt", "û����Ϣ������:" + type);
            return;
        }
        Log.v("mqtt", "������Ϣ:" + type);
        for (MsgHandler msgHandler : listenerList)
        {
            msgHandler.onMessage(type, data);
        }
    }

    //�����¼�
    public void DispachEvent(int event){
        if(listenerList.isEmpty()) {
            Log.v("mqtt", "û����Ϣ������:");
            return;
        }
        Log.v("mqtt", "�ɷ��¼�:" + event);
        for (MsgHandler msgHandler : listenerList)
        {
            msgHandler.onEvent(event);
        }
    }
    //endregion

}
