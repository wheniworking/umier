package com.longwei.umier.vo;

import com.longwei.umier.entity.UmierExam;
import com.longwei.umier.entity.UmierExamQuestion;

import java.util.Date;

public class UmierExamQuestionVo {
    private int id;
    private String question;
    private String choice;
    private int type;
    private String answer;
    private int score;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public UmierExamQuestion toUmierExamQuestion(int examId) {
        UmierExamQuestion q = new UmierExamQuestion();
        q.setId(id);
        q.setExamId(examId);
        q.setQuestion(question);
        q.setChoice(choice);
        q.setAnswer(answer);
        q.setScore(score);
        q.setType(type);
        q.setCreateTime(new Date());
        q.setState(0);
        return q;
    }
}
