package myMqtt.beans;

import java.util.ArrayList;
import java.util.Date;

/***
 * 设备信息
 */
public class DeviceInfo {
    private Boolean isPrivate;
    private Date create_time;
    private Date act_time;
    private ArrayList<DeviceKey> keys;
    private String auth_info;
    private String title;
    private ArrayList<String> tags;
    private String protocol;
    private Boolean online;
    private DeviceLocation location;
    private String id;
    private ArrayList<DataStreamTemplate> datastreams;
    private String desc;

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getAct_time() {
        return act_time;
    }

    public void setAct_time(Date act_time) {
        this.act_time = act_time;
    }

    public ArrayList<DeviceKey> getKeys() {
        return keys;
    }

    public void setKeys(ArrayList<DeviceKey> keys) {
        this.keys = keys;
    }

    public String getAuth_info() {
        return auth_info;
    }

    public void setAuth_info(String auth_info) {
        this.auth_info = auth_info;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public DeviceLocation getLocation() {
        return location;
    }

    public void setLocation(DeviceLocation location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<DataStreamTemplate> getDatastreams() {
        return datastreams;
    }

    public void setDatastreams(ArrayList<DataStreamTemplate> datastreams) {
        this.datastreams = datastreams;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "isPrivate=" + isPrivate +
                ", create_time=" + create_time +
                ", act_time=" + act_time +
                ", keys=" + keys +
                ", auth_info='" + auth_info + '\'' +
                ", title='" + title + '\'' +
                ", tags=" + tags +
                ", protocol='" + protocol + '\'' +
                ", online=" + online +
                ", location=" + location +
                ", id='" + id + '\'' +
                ", datastreams=" + datastreams +
                ", desc='" + desc + '\'' +
                '}';
    }
}
