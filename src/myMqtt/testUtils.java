package myMqtt;

import java.text.DateFormat;
import java.util.Date;
import java.util.Random;

import com.alibaba.fastjson.JSONObject;

public class testUtils {
	public static void main(String[] args) {
		Utils utils = new Utils();
		utils.connect();
		
		JSONObject json = new JSONObject();
		Random r = new Random(1);
		
		//for(int i=0; i<5; i++) {
		//	json.put("humidity", 40+i);
		//	json.put("temperature", 27+i);
		//	utils.publish("$dp", json.toString());
		//}
		while(true) {
			int ran = r.nextInt(20);
			json.put("humidity", 40+ran);
			json.put("temperature", 27+ran);
			utils.publish("$dp", json.toString());
			try {
				Thread.sleep(1000*1);
				
				Date date = new Date();
				DateFormat df = DateFormat.getDateTimeInstance();
				//�ж������Ƿ�����
				System.out.println("["+df.format(date)+"] >>> ����״̬��" + utils.isConnected());
				if(!utils.isConnected()) {
					System.out.println("["+df.format(date)+"] >>> �����ж��쳣������������ ... ...");
					utils.reConnect();  	// ��������
					System.out.println("["+df.format(date)+"] >>> ����״̬��" + utils.isConnected());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
