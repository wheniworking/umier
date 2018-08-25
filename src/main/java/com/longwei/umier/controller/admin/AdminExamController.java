package com.longwei.umier.controller.admin;

import com.longwei.umier.service.UmierExamService;
import com.longwei.umier.utils.DataMap;
import com.longwei.umier.utils.ResponseBuilder;
import com.longwei.umier.vo.ExamQueryVo;
import com.longwei.umier.vo.UmierExamQuestionVo;
import com.longwei.umier.vo.UmierExamVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/admin/exam")
public class AdminExamController {

    private final UmierExamService umierExamService;

    @Autowired
    public AdminExamController(UmierExamService umierExamService) {
        this.umierExamService = umierExamService;
    }

    @GetMapping
    public DataMap getExams(){
        return ResponseBuilder.create().ok().data(umierExamService.getExams()).build();
    }

    @GetMapping("/rules/{examId}")
    public DataMap getExamRules(@PathVariable int examId) {
        return ResponseBuilder.create().ok().data(umierExamService.getExamRules(examId)).build();
    }

    @GetMapping("/questions")
    public DataMap getExamQuestions(ExamQueryVo examQueryVo) {
        return ResponseBuilder.create().ok().data(umierExamService.getExamQuestions(examQueryVo)).build();
    }

    @PostMapping
    public DataMap createExam(@RequestBody UmierExamVo examVo) {
        umierExamService.createExam(examVo);
        return ResponseBuilder.create().ok().build();
    }

    @PostMapping("/{examId}/questions")
    public DataMap addQuestions(@PathVariable int examId, @RequestBody UmierExamQuestionVo questionVo) {
        umierExamService.addQuestions(examId, questionVo);
        return ResponseBuilder.create().ok().build();
    }


}
