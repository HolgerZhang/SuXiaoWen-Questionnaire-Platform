package com.suxiaowen.project.system.domain.mongo;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * 问卷题目
 * 用于MongoDB
 *
 * @author Holger
 */
public class Question implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Document ID
     */
    private String id;
    /**
     * 题目类型
     */
    private String type;
    /**
     * 题目
     */
    private String title;
    /**
     * 题目解释说明
     */
    private String description;
    /**
     * 题目序号
     */
    private Long index;
    /**
     * 是否必填
     */
    private Boolean required;
    /**
     * 题目选项信息
     */
    private List<String> information;

    public Question() {
        index = 0L;
        required = false;
        information = new LinkedList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public List<String> getInformation() {
        return information;
    }

    public void setInformation(List<String> information) {
        this.information = information;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", index=" + index +
                ", required=" + required +
                ", information=" + information +
                '}';
    }
}
