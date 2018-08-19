package com.longwei.umier.vo;

import java.util.List;

public class UmierUserAnswerVo {

    private int examId;
    List<AnswerPair> answers;

    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public List<AnswerPair> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerPair> answers) {
        this.answers = answers;
    }
}




