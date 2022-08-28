package com.suxiaowen.project.system.domain.mongo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 问卷结构实体类
 * 用于MongoDB
 *
 * @author Holger
 */
public class QuestionnaireStructure implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Document ID
     */
    private String id;
    /**
     * 题目列表
     */
    private List<Question> questions;
    /**
     * 题目逻辑列表
     */
    private List<QLogic> logics;

    public QuestionnaireStructure() {
        questions = new ArrayList<>();
        logics = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<QLogic> getLogics() {
        return logics;
    }

    public void setLogics(List<QLogic> logics) {
        this.logics = logics;
    }

    @Override
    public String toString() {
        return "QuestionnaireStructure{" +
                "id='" + id + '\'' +
                ", questions=" + questions +
                ", logics=" + logics +
                '}';
    }
}
