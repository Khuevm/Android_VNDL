package com.example.vndl.model;

import java.io.Serializable;

public class QuestionType implements Serializable {
    private int id;
    private String title;
    private String desc;
    private int totalQuestion;
    private String imageName;

    public QuestionType(int id, String title, String desc, int totalQuestion, String imageName) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.totalQuestion = totalQuestion;
        this.imageName = imageName;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public int getTotalQuestion() {
        return totalQuestion;
    }

    public String getImageName() {
        return imageName;
    }
}
