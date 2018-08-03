package com.longwei.umier.dao;

import com.longwei.umier.entity.UmierExam;
import com.longwei.umier.entity.UmierExamQuestion;
import com.longwei.umier.entity.UmierUserAnswer;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UmierExamDao {

    List<UmierExam> getExams();

    void insert(UmierExam exam);

    UmierExam getExamById(@Param("id") int id);

    List<UmierExamQuestion> getExamQuestions(@Param("examId")int examId);

    List<UmierExamQuestion> getExamQuestionsWithoutAnswer(@Param("examId")int examId);

    void insertQuestions(@Param("questions") List<UmierExamQuestion> questions);

    List<UmierExamQuestion> getExamQuestionByIds(@Param("examId")int examId, @Param("questionIds")  List<Integer> questionIds);

    void insertUserAnswer(List<UmierUserAnswer> umierUserAnswers);
}
