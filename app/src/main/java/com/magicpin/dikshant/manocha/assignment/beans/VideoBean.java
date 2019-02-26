package com.magicpin.dikshant.manocha.assignment.beans;

public class VideoBean {
    int serial;
    String link;
    static String description="This area can be used for video description, for introducing like buttons, user handles, etc. like those in social media mobile applications like facebook,twitter.";

    public VideoBean(int serial, String link) {
        this.serial = serial;
        this.link = link;
    }

    public static String getDescription() {
        return description;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
