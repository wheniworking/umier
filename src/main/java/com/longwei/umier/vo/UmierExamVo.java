package com.longwei.umier.vo;

import java.util.List;

public class UmierExamVo {

    private int id;
    private String name;
    private List<UmierExamRetRuleVo> retRules;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UmierExamRetRuleVo> getRetRules() {
        return retRules;
    }

    public void setRetRules(List<UmierExamRetRuleVo> retRules) {
        this.retRules = retRules;
    }
}


