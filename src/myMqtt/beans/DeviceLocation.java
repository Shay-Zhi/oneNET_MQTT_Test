package myMqtt.beans;

/***
 * …Ë±∏Œª÷√
 */
public class DeviceLocation {
    private float lat;
    private float lon;

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "DeviceLocation{" +
                "lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
