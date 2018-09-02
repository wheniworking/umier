package com.longwei.umier.vo;

import com.longwei.umier.entity.UmierExamQuestion;

import java.util.List;

public class UmierExamUserRetVo {
    private int score;
    private String ret;
    private String recommendImag;
    private int recommendActivity;
    private String sharId;
    private List<UmierExamQuestion> errorQuestions;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public String getRecommendImag() {
        return recommendImag;
    }

    public void setRecommendImag(String recommendImag) {
        this.recommendImag = recommendImag;
    }

    public int getRecommendActivity() {
        return recommendActivity;
    }

    public void setRecommendActivity(int recommendActivity) {
        this.recommendActivity = recommendActivity;
    }

    public String getSharId() {
        return sharId;
    }

    public void setSharId(String sharId) {
        this.sharId = sharId;
    }

    public List<UmierExamQuestion> getErrorQuestions() {
        return errorQuestions;
    }

    public void setErrorQuestions(List<UmierExamQuestion> errorQuestions) {
        this.errorQuestions = errorQuestions;
    }
}
