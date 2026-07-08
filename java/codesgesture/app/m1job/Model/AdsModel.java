package codesgesture.app.m1job.Model;

import java.io.Serializable;

public class AdsModel implements Serializable {

//    public AdsModel(String slider_path) {
//        this.slider_path = slider_path;
//    }

    private float id;
    private String slider = null;
    private String slider_path;


    // Getter Methods

    public float getId() {
        return id;
    }

    public String getSlider() {
        return slider;
    }

    public String getSlider_path() {
        return slider_path;
    }

    // Setter Methods

    public void setId(float id) {
        this.id = id;
    }

    public void setSlider(String slider) {
        this.slider = slider;
    }

    public void setSlider_path(String slider_path) {
        this.slider_path = slider_path;
    }
}