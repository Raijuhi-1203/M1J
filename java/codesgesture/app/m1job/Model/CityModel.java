package codesgesture.app.m1job.Model;

import java.io.Serializable;

public class CityModel implements Serializable {
    private float id;
    private float stateid;
    private String cityname;


    // Getter Methods

    public float getId() {
        return id;
    }

    public float getStateid() {
        return stateid;
    }

    public String getCityname() {
        return cityname;
    }

    // Setter Methods

    public void setId(float id) {
        this.id = id;
    }

    public void setStateid(float stateid) {
        this.stateid = stateid;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }
}