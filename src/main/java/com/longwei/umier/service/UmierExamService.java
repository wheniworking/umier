package com.longwei.umier.service;

import com.longwei.umier.dao.UmierExamDao;
import com.longwei.umier.entity.*;
import com.longwei.umier.interceptor.AuthInfoHolder;
import com.longwei.umier.vo.*;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    public UmierExamUserRetVo submitExam(UmierUserAnswerVo userAnswer) {
        List<Integer> questionIds = userAnswer.getAnswers().stream().map(AnswerPair::getQuestionId).collect(Collectors.toList());
        List<UmierExamQuestion> questions = umierExamDao.getExamQuestionByIds(userAnswer.getExamId(), questionIds);
        Map<Integer, UmierExamQuestion> qMap = questions.stream().collect(Collectors.toMap(UmierExamQuestion::getId, r -> r));
        int totalScore = 0;
        for (AnswerPair it : userAnswer.getAnswers()) {
            UmierExamQuestion question = qMap.get(it.getQuestionId());
            int score = 0;
            if (question != null) {
                if (question.getAnswer().equals(it.getUserAnswer())) {
                   score = question.getScore();
                }
            }
            totalScore += score;
        }
        UmierUserExamRecord record = new UmierUserExamRecord();
        record.setUnionId(userAnswer.getUnionId());
        record.setExamId(userAnswer.getExamId());
        record.setShareId(UUID.randomUUID().toString().replaceAll("-",""));
        record.setScore(totalScore);
        record.setCreateTime(new Date());
        record.setState(0);
        umierExamDao.insertUserAnswer(record);
        UmierExamRetRule rule = null;
        List<UmierExamRetRule> rules = umierExamDao.getExamRetRules(userAnswer.getExamId());
        for (UmierExamRetRule r : rules) {
            if (totalScore >= r.getLowerScore() && totalScore < r.getUpperScore()) {
                rule = r;
            }
        }

        UmierExamUserRetVo ret = new UmierExamUserRetVo();
        ret.setScore(totalScore);
        ret.setSharId(record.getShareId());
        if (rule != null) {
            ret.setRet(rule.getDescription());
            int acitivityId = rule.getGroupActivityId();

        }
        return ret;
    }

    public void addQuestions(int examId, UmierExamQuestionVo questionVo) {
        umierExamDao.insertQuestion(questionVo.toUmierExamQuestion(examId));
    }

    public UmierExamShareVo getPageShareData(String shareId) {
       UmierUserExamRecord record = umierExamDao.getUserExamRecord(shareId);
       WxMpUserInfoVo currentUser = AuthInfoHolder.get();
       boolean isSameUser = false;
        if (currentUser.getUnionId().equals(record.getUnionId())) {
            isSameUser = true;
        }
        return new UmierExamShareVo(isSameUser, record);
    }
}
