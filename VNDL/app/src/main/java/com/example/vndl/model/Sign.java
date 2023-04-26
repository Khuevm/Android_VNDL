package com.example.vndl.model;

import java.io.Serializable;

public class Sign implements Serializable {
    private int id;
    private int groupID;
    private String signCode, title, desc, imageName;

    public int getId() {
        return id;
    }

    public int getGroupID() {
        return groupID;
    }

    public String getSignCode() {
        return signCode;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getImageName() {
        return imageName;
    }

    public Sign(int id, int groupID, String signCode, String title, String desc, String imageName) {
        this.id = id;
        this.groupID = groupID;
        this.signCode = signCode;
        this.title = title;
        this.desc = desc;
        this.imageName = imageName;
    }
}
