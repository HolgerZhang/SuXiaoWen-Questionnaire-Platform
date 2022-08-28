package com.suxiaowen.project.system.domain.mongo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class QAnswer implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Document ID
     */
    private String id;

    /**
     * 题目Document ID与答案映射
     */
    private Map<String, String> answers;

    public QAnswer() {
        answers = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, String> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "QAnswer{" +
                "id='" + id + '\'' +
                ", answers=" + answers +
                '}';
    }
}
