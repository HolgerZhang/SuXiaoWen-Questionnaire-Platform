package com.suxiaowen.project.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 问卷表
 *
 * @TableName questionnaire
 */
public class Questionnaire implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 问卷ID
     */
    private String questionnaireId;
    /**
     * 问卷标题
     */
    private String title;
    /**
     * 问卷简介
     */
    private String introduction;
    /**
     * 问卷结构 Document ID
     */
    private String structure;
    /**
     * 创建者
     */
    private Long userId;
    /**
     * 创建时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 到期时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dueTime;
    /**
     * 问卷状态（0-未发布 1-收集中 2-已结束）
     */
    private String questionnaireStatus;
    /**
     * 删除标记（0-未删除 1-已删除）
     */
    private String delFlag;
    /**
     * 星标（0-否 1-是）
     */
    private String starFlag;
    /**
     * 可修改答卷（0-否 1-是）
     */
    private String modifyFlag;
    /**
     * 可匿名答题（0-否 1-是）
     */
    private String anonymousFlag;

    /**
     * 问卷ID
     */
    public String getQuestionnaireId() {
        return questionnaireId;
    }

    /**
     * 问卷ID
     */
    public void setQuestionnaireId(String questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    /**
     * 问卷标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 问卷标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 问卷简介
     */
    public String getIntroduction() {
        return introduction;
    }

    /**
     * 问卷简介
     */
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    /**
     * 问卷结构 Document ID
     */
    public String getStructure() {
        return structure;
    }

    /**
     * 问卷结构 Document ID
     */
    public void setStructure(String structure) {
        this.structure = structure;
    }

    /**
     * 创建者
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 创建者
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 到期时间
     */
    public Date getDueTime() {
        return dueTime;
    }

    /**
     * 到期时间
     */
    public void setDueTime(Date dueTime) {
        this.dueTime = dueTime;
    }

    /**
     * 问卷状态（0-未发布 1-收集中 2-已结束）
     */
    public String getQuestionnaireStatus() {
        return questionnaireStatus;
    }

    /**
     * 问卷状态（0-未发布 1-收集中 2-已结束）
     */
    public void setQuestionnaireStatus(String questionnaireStatus) {
        this.questionnaireStatus = questionnaireStatus;
    }

    /**
     * 删除标记（0-未删除 1-已删除）
     */
    public String getDelFlag() {
        return delFlag;
    }

    /**
     * 删除标记（0-未删除 1-已删除）
     */
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    /**
     * 星标（0-否 1-是）
     */
    public String getStarFlag() {
        return starFlag;
    }

    /**
     * 星标（0-否 1-是）
     */
    public void setStarFlag(String starFlag) {
        this.starFlag = starFlag;
    }

    /**
     * 可修改答卷（0-否 1-是）
     */
    public String getModifyFlag() {
        return modifyFlag;
    }

    /**
     * 可修改答卷（0-否 1-是）
     */
    public void setModifyFlag(String modifyFlag) {
        this.modifyFlag = modifyFlag;
    }

    /**
     * 可匿名答题（0-否 1-是）
     */
    public String getAnonymousFlag() {
        return anonymousFlag;
    }

    /**
     * 可匿名答题（0-否 1-是）
     */
    public void setAnonymousFlag(String anonymousFlag) {
        this.anonymousFlag = anonymousFlag;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Questionnaire other = (Questionnaire) that;
        return (this.getQuestionnaireId() == null ? other.getQuestionnaireId() == null : this.getQuestionnaireId().equals(other.getQuestionnaireId()))
                && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
                && (this.getIntroduction() == null ? other.getIntroduction() == null : this.getIntroduction().equals(other.getIntroduction()))
                && (this.getStructure() == null ? other.getStructure() == null : this.getStructure().equals(other.getStructure()))
                && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getDueTime() == null ? other.getDueTime() == null : this.getDueTime().equals(other.getDueTime()))
                && (this.getQuestionnaireStatus() == null ? other.getQuestionnaireStatus() == null : this.getQuestionnaireStatus().equals(other.getQuestionnaireStatus()))
                && (this.getDelFlag() == null ? other.getDelFlag() == null : this.getDelFlag().equals(other.getDelFlag()))
                && (this.getStarFlag() == null ? other.getStarFlag() == null : this.getStarFlag().equals(other.getStarFlag()))
                && (this.getModifyFlag() == null ? other.getModifyFlag() == null : this.getModifyFlag().equals(other.getModifyFlag()))
                && (this.getAnonymousFlag() == null ? other.getAnonymousFlag() == null : this.getAnonymousFlag().equals(other.getAnonymousFlag()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getQuestionnaireId() == null) ? 0 : getQuestionnaireId().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getIntroduction() == null) ? 0 : getIntroduction().hashCode());
        result = prime * result + ((getStructure() == null) ? 0 : getStructure().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getDueTime() == null) ? 0 : getDueTime().hashCode());
        result = prime * result + ((getQuestionnaireStatus() == null) ? 0 : getQuestionnaireStatus().hashCode());
        result = prime * result + ((getDelFlag() == null) ? 0 : getDelFlag().hashCode());
        result = prime * result + ((getStarFlag() == null) ? 0 : getStarFlag().hashCode());
        result = prime * result + ((getModifyFlag() == null) ? 0 : getModifyFlag().hashCode());
        result = prime * result + ((getAnonymousFlag() == null) ? 0 : getAnonymousFlag().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", questionnaireId=").append(questionnaireId);
        sb.append(", title=").append(title);
        sb.append(", introduction=").append(introduction);
        sb.append(", structure=").append(structure);
        sb.append(", userId=").append(userId);
        sb.append(", createTime=").append(createTime);
        sb.append(", dueTime=").append(dueTime);
        sb.append(", questionnaireStatus=").append(questionnaireStatus);
        sb.append(", delFlag=").append(delFlag);
        sb.append(", starFlag=").append(starFlag);
        sb.append(", modifyFlag=").append(modifyFlag);
        sb.append(", anonymousFlag=").append(anonymousFlag);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}