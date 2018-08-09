package com.longwei.umier.entity;

import java.util.Date;

public class UmierExamRetRule {

    private int id;
    private int examId;
    private int groupActivityId;
    private String description;
    private int lowerScore;
    private int upperScore;
    private Date createTime;
    private int state;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLowerScore() {
        return lowerScore;
    }

    public void setLowerScore(int lowerScore) {
        this.lowerScore = lowerScore;
    }

    public int getUpperScore() {
        return upperScore;
    }

    public void setUpperScore(int upperScore) {
        this.upperScore = upperScore;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getGroupActivityId() {
        return groupActivityId;
    }

    public void setGroupActivityId(int groupActivityId) {
        this.groupActivityId = groupActivityId;
    }
}
