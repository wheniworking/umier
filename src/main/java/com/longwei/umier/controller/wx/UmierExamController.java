package com.longwei.umier.controller.wx;


import com.longwei.umier.service.UmierExamService;
import com.longwei.umier.utils.DataMap;
import com.longwei.umier.utils.ResponseBuilder;
import com.longwei.umier.vo.UmierExamVo;
import com.longwei.umier.vo.UmierUserAnswerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/exam")
public class UmierExamController {

    private final UmierExamService umierExamService;

    @Autowired
    public UmierExamController(UmierExamService umierExamService) {
        this.umierExamService = umierExamService;
    }

    @GetMapping
    public DataMap getExams() {
        return ResponseBuilder.create().ok().data(umierExamService.getExams()).build();
    }

    @GetMapping("/{examId}/questions")
    public DataMap getExamQuestions(@PathVariable("examId") int examId) {
        return ResponseBuilder.create().ok().data(umierExamService.selectQuestions(examId)).build();
    }

    @PostMapping("/{examId}/submit")
    public DataMap submitExam(@RequestBody UmierUserAnswerVo userAnswer) {
        umierExamService.submitExam(userAnswer);
        return ResponseBuilder.create().ok().build();
    }
}
