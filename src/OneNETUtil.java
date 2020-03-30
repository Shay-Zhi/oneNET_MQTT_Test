//package onenet;
/**
 * OneNET 工具类
 * @author 孤胆枪手
 *
 */
public final class OneNETUtil {
	/**
	 * 构建数据上报类型3的byte数组
	 * @param jsonString json字符串
	 * @return 构建成功的byte数组
	 */
	public static byte[] type3Bytes(String jsonString) {
		int length = jsonString.length();
		byte[] payload3 = new byte[length + 3];
		payload3[0] = 0x03;
		payload3[1] = (byte) (length >> 8);
		payload3[2] = (byte) (length);
		byte[] jsonBytes = jsonString.getBytes();
		for (int i = 0; i < length; i++) {
			payload3[3 + i] = jsonBytes[i];
		}
		return payload3;
	}
}
