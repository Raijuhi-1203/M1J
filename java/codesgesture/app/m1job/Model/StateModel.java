package codesgesture.app.m1job.Model;

import java.io.Serializable;

public class StateModel implements Serializable {
    private float id;
    private String state_nm;


    // Getter Methods

    public float getId() {
        return id;
    }

    public String getState_nm() {
        return state_nm;
    }

    // Setter Methods

    public void setId(float id) {
        this.id = id;
    }

    public void setState_nm(String state_nm) {
        this.state_nm = state_nm;
    }
}