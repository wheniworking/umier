package com.longwei.umier.dao;

import com.longwei.umier.entity.UmierExam;
import com.longwei.umier.entity.UmierExamQuestion;
import com.longwei.umier.entity.UmierExamRetRule;
import com.longwei.umier.entity.UmierUserExamRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UmierExamDao {

    List<UmierExam> getExams();

    void insert(UmierExam exam);

    UmierExam getExamById(@Param("id") int id);

    List<UmierExamQuestion> getExamQuestions(@Param("quesId")Integer quesId, @Param("examId")Integer examId);

    List<UmierExamQuestion> getExamQuestionsWithoutAnswer(@Param("examId")int examId);

    List<UmierExamQuestion> getExamQuestionByIds(@Param("examId")int examId, @Param("questionIds")  List<Integer> questionIds);

    void insertUserAnswer(UmierUserExamRecord record);

    List<UmierExamRetRule> getExamRetRules(@Param("examId")int examId);

    void insertRules(@Param("rules")List<UmierExamRetRule> questions);

    void insertQuestion(UmierExamQuestion umierExamQuestion);

    UmierUserExamRecord getUserExamRecord(@Param("shareId") String shareId);

    void deleteExam(@Param("examId")int examId);

    void deleteExamQuestions(@Param("examId") int examId);

    void deleteExamRule(@Param("examId")int examId);
}
