package com.longwei.umier.vo;

import com.longwei.umier.entity.UmierExam;
import com.longwei.umier.entity.UmierExamRetRule;

import java.util.Date;

public class UmierExamRetRuleVo {
    private int id;
    private String description;
    private int lowerScore;
    private int upperScore;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public UmierExamRetRule toUmierExamRetRule(UmierExam exam) {
        UmierExamRetRule rule = new UmierExamRetRule();
        rule.setId(this.id);
        rule.setDescription(this.description);
        rule.setExamId(exam.getId());
        rule.setLowerScore(this.lowerScore);
        rule.setUpperScore(this.upperScore);
        rule.setCreateTime(new Date());
        rule.setState(0);
        return rule;
    }
}
