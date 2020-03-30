import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Test {
	public static void main(String[] args) {
		String payload = "hello, world";
		int length = payload.length();
		byte[] payload3 = new byte[length + 3];
		payload3[0] = 0x03;
		payload3[1] = (byte) (length >> 8);
		payload3[2] = (byte) (length);
		System.out.println("Size: " + length);
		System.out.println("16���Ƹ�λ��" + payload3[1]);
		System.out.println("16���Ƶ�λ��" + payload3[2]);
		byte[] jsonBytes = payload.getBytes();
		for (int i = 0; i < length; i++) {
			payload3[3 + i] = jsonBytes[i];
		}
		for(int i = 0; i < length+3; i++) {
			System.out.println(payload3[i]);
		}

		Date date = new Date();
		//DateFormat df = DateFormat.getDateTimeInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String start = df.format(date).replace(" ", "T");
		System.out.println(start);

		Calendar beforeTime = Calendar.getInstance();
		beforeTime.add(Calendar.MINUTE, -5);// 5����֮ǰ��ʱ��
		Date beforeD = beforeTime.getTime();
		String before5 = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").format(beforeD);  // ǰ�����ʱ��
		System.out.println(before5);
	}
}
