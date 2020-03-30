package myMqtt.beans;

/***
 * 设备Key信息
 */
public class DeviceKey {
    private String title;
    private String key;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "DeviceKey{" +
                "title='" + title + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
