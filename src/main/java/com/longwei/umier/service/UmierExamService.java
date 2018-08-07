package com.longwei.umier.service;

import com.longwei.umier.dao.UmierExamDao;
import com.longwei.umier.entity.UmierExam;
import com.longwei.umier.entity.UmierExamQuestion;
import com.longwei.umier.entity.UmierExamRetRule;
import com.longwei.umier.entity.UmierUserAnswer;
import com.longwei.umier.vo.*;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UmierExamService {

    private static final int MAX_QUESTION_NUMBER = 5;

    @Autowired
    private UmierExamDao umierExamDao;

    public List<UmierExam> getExams() {
        return umierExamDao.getExams();
    }

    public List<UmierExamQuestion> getExamQuestions(ExamQueryVo examQueryVo) {
        return umierExamDao.getExamQuestions(examQueryVo.getQuestionId(), examQueryVo.getExamId());
    }

    public List<UmierExamQuestion> selectQuestions(int examId) {
        List<UmierExamQuestion> questions = umierExamDao.getExamQuestions(null, examId);
        if(questions.size() <= MAX_QUESTION_NUMBER) return questions;
        Set<Integer> numberSet = new HashSet<>();
        while (numberSet.size() < 5){
            numberSet.add(RandomUtils.nextInt(0, questions.size()));
        }

        List<UmierExamQuestion> q = new ArrayList<>(MAX_QUESTION_NUMBER);
        for (Integer i : numberSet) {
            q.add(questions.get(i));
        }
        return q;
    }

    public void createExam(UmierExamVo examVo) {
        UmierExam exam = new UmierExam();
        exam.setId(examVo.getId());
        exam.setName(examVo.getName());
        exam.setCreateTime(new Date());
        umierExamDao.insert(exam);
        List<UmierExamRetRule> persistRules = umierExamDao.getExamRetRules(exam.getId());
        List<UmierExamRetRule> questions = mergeExamQuestions(exam, persistRules, examVo.getRetRules());
        if(!questions.isEmpty()) {
            umierExamDao.insertRules(questions);
        }
    }

    private List<UmierExamRetRule> mergeExamQuestions(UmierExam exam,
                                                       List<UmierExamRetRule> persistQuestionVos,
                                                       List<UmierExamRetRuleVo> requestQuestionVos) {
        List<UmierExamRetRule> umierExamQuestions = new LinkedList<>();
        Set<Integer> ids = requestQuestionVos.stream().map(UmierExamRetRuleVo::getId).collect(Collectors.toSet());
        persistQuestionVos.forEach(it -> {
            if (!ids.contains(it.getId())) {
                it.setState(100);
                umierExamQuestions.add(it);
            }
        });

        requestQuestionVos.forEach(it -> {
            umierExamQuestions.add(it.toUmierExamRetRule(exam));
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

    public void addQuestions(int examId, UmierExamQuestionVo questionVo) {
        umierExamDao.insertQuestion(questionVo.toUmierExamQuestion(examId));
    }


}
