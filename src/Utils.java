
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttClient;

import java.util.ArrayList;

/**
 * MQTTЭ�����OneNetƽ̨������
 */
public class Utils {

    /**
     * ����OneNet��������Ϣ
     */
    private static String serverIP = "183.230.40.39";               // ������ip
    private static int serverPort = 6002;                           // �������˿�
    private static String deviceId = "���豸id";                            // �ն�id
    private static String userId = "��Ӧ��id";                           // ��������½��
    private static String password = "���Ȩ��apiKey";                         // ��������½����
    
    /**
     * ����״̬����
     */
    public static final int MQTT_CONNECTED = 1000;                 // mqtt���ӳɹ�
    public static final int MQTT_CONNECTFAIL = 1001;               // mqtt����ʧ��
    public static final int MQTT_DISCONNECT = 1002;                // mqtt�Ͽ�����

    public static final int MQTT_SUBSCRIBED = 1010;                // ���ĳɹ�
    public static final int MQTT_SUBSCRIBEFAIL = 1011;             // ����ʧ��

    public static final int MQTT_MSG_TEST = 2001;                  // ���յ�TEST��Ϣ

    public static final int MQTT_PUBLISHED = 2010;                 // �����ɹ�
    public static final int MQTT_PUBLISHFAIL = 2011;               // ����ʧ��
    
    /**
     * �ͻ�������
     */
    private static Utils instance;                               // ��������
    private MqttClient mqttClient;                           // mqtt�ͻ���
    private MqttConnectOptions option;                              // mqtt����
    private MqttCallback clientCallback;                            // �ͻ��˻ص�
    private ArrayList<MsgHandler> listenerList = new ArrayList<>(); // ��Ϣ������
    
    /**
     * ��չ��캯��
     */
    private Utils(){}

    /**
     * ��ȡ����
     */
    public static Utils getInstance(){
        if(instance == null){
            instance = new Utils();
        }
        return instance;
    }

    /**
     * ��ʼ������
     */
	  public void initMqtt() { 
		  connect(); 
	  }

    /**
     * ����
     */
    public void reConnect() {
        //��������
        connect();
    }

    /**
     * ������Ϣ
     */
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
                	System.out.println("����ʧ�ܣ�\n" + e);
                    DispachEvent(MQTT_PUBLISHFAIL);
                }
            } else {
            	System.out.println("����δ���� - ������������\n");
                connect();
                DispachEvent(MQTT_PUBLISHFAIL);
            }
        } else {
        	System.out.println("�ͻ��˳�ʼ��ʧ��\n");
            DispachEvent(MQTT_PUBLISHFAIL);
        }
    }

    /**
     * ��������
     */
    public void subscribe(String topic){
        try {
            //������Ϣ
            mqttClient.subscribe(topic, 0);
            DispachEvent(MQTT_SUBSCRIBED);
        } catch (MqttException e) {
            DispachEvent(MQTT_SUBSCRIBEFAIL);
            System.out.println("���Ĵ���:" + e);
        }
    }

    /**
     * �����Ƿ�����
     */
    public boolean isConnected(){
        return mqttClient.isConnected();
    }

    /**
     * ��ȡ�豸deviceId
     */
    public String getDeviceId(){
        return deviceId;
    }

    /**
     * �������Ӷ������ӵ�OneNet���豸��
     */
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
                	System.out.println( "���յ���Ϣ:" + topic);
                    DispachMessage(topic, message);
                }
                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    //publish�ɹ������
                	System.out.println("���ͳɹ�");
                }
            };
            try {
                mqttClient = new MqttClient(("tcp://" + serverIP + ":" + serverPort), deviceId);
                MqttClient mqttClient1 = new MqttClient(("tcp://" + serverIP + ":" + serverPort), deviceId);
                mqttClient.setCallback(clientCallback);
            } catch (Exception e) {
                System.out.println("�����������:" + e);
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
                            System.out.println("���ӳɹ�");
                        } else {
                        	System.out.println("�����������");
                            DispachEvent(MQTT_CONNECTFAIL);
                        }
                    } catch (Exception e) {
                    	System.out.println("���Ӵ���:" + e);
                        DispachEvent(MQTT_CONNECTFAIL);
                    }
                }
            }).start();
        }
    }

    /**
     * ��Ϣת������ 
     */
    
    /**
     * ��ӽ�����
     */
    public void addListener(MsgHandler msgHandler){
        if(!listenerList.contains(msgHandler)) {
            listenerList.add(msgHandler);
        }
    }

    /**
     * �Ƴ�������
     */
    public void removeListener(MsgHandler msgHandler){
        listenerList.remove(msgHandler);
    }

    /**
     * �Ƴ����н�����
     */
    public void removeAll(){
        listenerList.clear();
    }

    /**
     * ������Ϣ
     */
    public void DispachMessage(String type, Object data){
        if(listenerList.isEmpty()) {
        	System.out.println("û����Ϣ������:" + type);
            return;
        }
        System.out.println("������Ϣ:" + type);
        for (MsgHandler msgHandler : listenerList)
        {
            msgHandler.onMessage(type, data);
        }
    }

    /**
     * �����¼�
     */
    public void DispachEvent(int event){
        if(listenerList.isEmpty()) {
        	System.out.println("û����Ϣ������:");
            return;
        }
        System.out.println("�ɷ��¼�:" + event);
        for (MsgHandler msgHandler : listenerList)
        {
            msgHandler.onEvent(event);
        }
    }

}
