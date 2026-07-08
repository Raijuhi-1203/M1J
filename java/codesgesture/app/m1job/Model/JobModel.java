package codesgesture.app.m1job.Model;

import java.io.Serializable;

public class JobModel implements Serializable {
    private String id;
    private String isactive;
    private String ad_img;
    private String contactno;
    private String create_date;
    private String jobcat;
    private String mobile2;
    private String mobile3;


    // Getter Methods

    public String getId() {
        return id;
    }

    public String getIsactive() {
        return isactive;
    }

    public String getAd_img() {
        return ad_img;
    }

    public String getContactno() {
        return contactno;
    }

    public String getCreate_date() {
        return create_date;
    }

    public String getJobcat() {
        return jobcat;
    }

    public String getMobile2() {
        return mobile2;
    }

    public String getMobile3() {
        return mobile3;
    }

    // Setter Methods

    public void setId(String id) {
        this.id = id;
    }

    public void setIsactive(String isactive) {
        this.isactive = isactive;
    }

    public void setAd_img(String ad_img) {
        this.ad_img = ad_img;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public void setJobcat(String jobcat) {
        this.jobcat = jobcat;
    }

    public void setMobile2(String mobile2) {
        this.mobile2 = mobile2;
    }

    public void setMobile3(String mobile3) {
        this.mobile3 = mobile3;
    }
}