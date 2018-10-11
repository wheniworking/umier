package com.longwei.umier.service;

import com.longwei.umier.dao.UmierExamDao;
import com.longwei.umier.entity.*;
import com.longwei.umier.interceptor.WxAuthInfoHolder;
import com.longwei.umier.vo.*;
import org.apache.commons.lang3.RandomUtils;
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

    public List<UmierExamQuestion> getExamQuestions(ExamQueryVo examQueryVo) {
        return umierExamDao.getExamQuestions(examQueryVo.getQuestionId(), examQueryVo.getExamId());
    }

    public List<UmierExamQuestion> selectQuestions() {
        List<UmierExam> exams = umierExamDao.getExams();
        List<Integer> examIds = exams.stream().map(UmierExam::getId).collect(Collectors.toList());
        List<ExamQuestionIdVo> examQuestionIdVos = umierExamDao.selectQuestionsByExamId(examIds);
        Map<Integer, List<ExamQuestionIdVo>> map = examQuestionIdVos.stream().collect(Collectors.groupingBy(ExamQuestionIdVo::getExamId));
        List<Integer> quesIds = new ArrayList<>(map.size());
        for (Map.Entry<Integer, List<ExamQuestionIdVo>> e : map.entrySet()) {
            quesIds.add(getRandomElement(e.getValue()).getQuestionId());
        }
        List<UmierExamQuestion> questions = umierExamDao.selectQuestions(quesIds);
        questions.forEach(it -> {
            it.setAnswer("");
            it.setAnswerDesc("");
        });
        return questions;
    }

    private <T> T getRandomElement(List<T> list) {
        int size = list.size();
        int randId = RandomUtils.nextInt(0, size);
        return list.get(randId);
    }

    public void createExam(UmierExamVo examVo) {
        UmierExam exam = new UmierExam();
        exam.setId(examVo.getId());
        exam.setName(examVo.getName());
        exam.setCreateTime(new Date());
        umierExamDao.insert(exam);
    }

    public void createExamRules(List<UmierExamRetRuleVo> retRules) {
        List<UmierExamRetRule> persistRules = umierExamDao.getExamRetRules();
        List<UmierExamRetRule> questions = mergeExamQuestions(persistRules, retRules);
        if (!questions.isEmpty()) {
            umierExamDao.insertRules(questions);
        }
    }

    private List<UmierExamRetRule> mergeExamQuestions(List<UmierExamRetRule> persistRules,
                                                      List<UmierExamRetRuleVo> requestRules) {
        List<UmierExamRetRule> rules = new LinkedList<>();
        Set<Integer> ids = requestRules.stream().map(UmierExamRetRuleVo::getId).collect(Collectors.toSet());
        persistRules.forEach(it -> {
            if (!ids.contains(it.getId())) {
                it.setState(100);
                rules.add(it);
            }
        });

        requestRules.forEach(it -> {
            rules.add(it.toUmierExamRetRule());
        });
        return rules;
    }


    public UmierExamUserRetVo submitExam(UmierUserAnswerVo userAnswer) {
        WxMpUserInfoVo userInfo = WxAuthInfoHolder.get();
        List<Integer> questionIds = userAnswer.getAnswers().stream().map(AnswerPair::getQuestionId).collect(Collectors.toList());
        List<UmierExamQuestion> questions = umierExamDao.getExamQuestionByIds(questionIds);
        Map<Integer, UmierExamQuestion> qMap = questions.stream().collect(Collectors.toMap(UmierExamQuestion::getId, r -> r));
        List<UmierExamQuestion> errorQuestions = new LinkedList<>();
        int count = 0;
        List<UserExamHistory> histories = new ArrayList<>();
        for (AnswerPair it : userAnswer.getAnswers()) {
            UmierExamQuestion question = qMap.get(it.getQuestionId());
            UserExamHistory history = new UserExamHistory();
            history.setUnionId(userInfo.getUnionId());
            history.setQuestionId(it.getQuestionId());
            history.setAnswer(it.getUserAnswer());
            history.setCreateTime(new Date());
            history.setState(0);
            if (question != null) {
                if (question.getAnswer().equalsIgnoreCase(it.getUserAnswer())) {
                    count++;
                    history.setCorrect(true);
                } else {
                    errorQuestions.add(question);
                }
            }
            histories.add(history);
        }
        umierExamDao.insertUserExamHistory(histories);
        int totalScore = (int) (count * 100.0 / questionIds.size());
        UmierUserExamRecord record = new UmierUserExamRecord();
        record.setUnionId(userInfo.getUnionId());
        record.setNickname(userInfo.getNickname());
        record.setShareId(UUID.randomUUID().toString().replaceAll("-", ""));
        record.setScore(totalScore);
        record.setCreateTime(new Date());
        record.setState(0);

        UmierExamRetRule rule = null;
        UmierExamRetRule bestRule = null;
        List<UmierExamRetRule> rules = umierExamDao.getExamRetRules();
        for (UmierExamRetRule r : rules) {
            if (totalScore >= r.getLowerScore() && totalScore < r.getUpperScore()) {
                rule = r;
            }
            if (bestRule == null) {
                bestRule = r;
            } else {
                if (r.getUpperScore() > bestRule.getUpperScore()) {
                    bestRule = r;
                }
            }
        }


        UmierExamUserRetVo ret = new UmierExamUserRetVo();
        ret.setScore(totalScore);
        ret.setSharId(record.getShareId());
        if (rule != null) {
            ret.setRet(rule.getDescription());
        } else {
            if (totalScore == 100) {
                ret.setRet(bestRule.getDescription());
            }
        }
        ret.setErrorQuestions(errorQuestions);
        record.setRet(ret.getRet());
        umierExamDao.insertUserAnswer(record);
        return ret;
    }

    public void addQuestions(int examId, UmierExamQuestionVo questionVo) {
        umierExamDao.insertQuestion(questionVo.toUmierExamQuestion(examId));
    }

    public UmierExamShareVo getPageShareData(String shareId) {
        UmierUserExamRecord record = umierExamDao.getUserExamRecord(shareId);
        WxMpUserInfoVo currentUser = WxAuthInfoHolder.get();
        record.setNickname(currentUser.getNickname());
        boolean isSameUser = false;
        if (currentUser.getUnionId().equals(record.getUnionId())) {
            isSameUser = true;
        }

        return new UmierExamShareVo(isSameUser, record);
    }


    public List<UmierExamRetRule> getExamRules() {
        return umierExamDao.getExamRetRules();
    }

    public void deleteExam(int examId) {
        umierExamDao.deleteExam(examId);
        umierExamDao.deleteExamQuestions(examId);
    }

    public Object getUserExamRecord(int index, int size) {
        Map map = new HashMap();
        map.put("list", umierExamDao.getUserExamRecordList(index * size, size));
        map.put("total", umierExamDao.countUserExam());
        return map;
    }
}
