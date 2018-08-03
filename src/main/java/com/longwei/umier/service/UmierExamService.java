package com.longwei.umier.service;

import com.longwei.umier.dao.UmierExamDao;
import com.longwei.umier.entity.UmierExam;
import com.longwei.umier.entity.UmierExamQuestion;
import com.longwei.umier.entity.UmierUserAnswer;
import com.longwei.umier.vo.AnswerPair;
import com.longwei.umier.vo.UmierExamQuestionVo;
import com.longwei.umier.vo.UmierExamVo;
import com.longwei.umier.vo.UmierUserAnswerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UmierExamService {

    @Autowired
    private UmierExamDao umierExamDao;

    public List<UmierExam> getExams() {
        return umierExamDao.getExams();
    }

    public List<UmierExamQuestion> getExamQuestions(int examId) {
        return umierExamDao.getExamQuestionsWithoutAnswer(examId);
    }

    public void createExam(UmierExamVo examVo) {
        UmierExam exam = new UmierExam();
        exam.setId(examVo.getId());
        exam.setName(examVo.getName());
        exam.setCreateTime(new Date());
        umierExamDao.insert(exam);
        List<UmierExamQuestion> persistQuestionVos = umierExamDao.getExamQuestions(exam.getId());
        List<UmierExamQuestion> questions = mergeExamQuestions(exam, persistQuestionVos, examVo.getQuestions());
        umierExamDao.insertQuestions(questions);
    }

    private List<UmierExamQuestion> mergeExamQuestions(UmierExam exam,
                                                       List<UmierExamQuestion> persistQuestionVos,
                                                       List<UmierExamQuestionVo> requestQuestionVos) {
        List<UmierExamQuestion> umierExamQuestions = new LinkedList<>();
        Set<Integer> ids = requestQuestionVos.stream().map(UmierExamQuestionVo::getId).collect(Collectors.toSet());
        persistQuestionVos.forEach(it -> {
            if (!ids.contains(it.getId())) {
                it.setState(100);
                umierExamQuestions.add(it);
            }
        });

        requestQuestionVos.forEach(it -> {
            umierExamQuestions.add(it.toUmierExamQuestion(exam));
        });

        return umierExamQuestions;
    }


    public void submitExam(UmierUserAnswerVo userAnswer) {
        List<Integer> questionIds = userAnswer.getAnswers().stream().map(AnswerPair::getQuestionId).collect(Collectors.toList());
        List<UmierExamQuestion> questions = umierExamDao.getExamQuestionByIds(userAnswer.getExamId(), questionIds);
        Map<Integer, UmierExamQuestion> qMap = questions.stream().collect(Collectors.toMap(UmierExamQuestion::getId, r -> r));
        int totalScore = 0;
        List<UmierUserAnswer> umierUserAnswers = new LinkedList<>();
        for (AnswerPair it : userAnswer.getAnswers()) {
            UmierExamQuestion question = qMap.get(it.getQuestionId());
            int score = 0;
            if (question != null) {
                if (question.getAnswer().equals(it.getUserAnswer())) {
                   score = question.getScore();
                }
            }

            UmierUserAnswer answer = new UmierUserAnswer();
            answer.setUnionId(userAnswer.getUnionId());
            answer.setExamId(userAnswer.getExamId());
            answer.setQuestionId(it.getQuestionId());
            answer.setAnswer(it.getUserAnswer());
            answer.setScore(score);
            answer.setCreateTime(new Date());
            answer.setState(0);
            umierUserAnswers.add(answer);

            totalScore += score;
        }
        umierExamDao.insertUserAnswer(umierUserAnswers);
    }

}
