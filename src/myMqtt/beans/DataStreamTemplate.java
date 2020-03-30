package myMqtt.beans;

import java.util.Date;

/***
 * 设备 - 数据流模板
 */
public class DataStreamTemplate {
    private String unit;
    private String id;
    private String unitSymbol;
    private Date createTime;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnitSymbol() {
        return unitSymbol;
    }

    public void setUnitSymbol(String unitSymbol) {
        this.unitSymbol = unitSymbol;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "DataStreamTemplate{" +
                "unit='" + unit + '\'' +
                ", id='" + id + '\'' +
                ", unitSymbol='" + unitSymbol + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
