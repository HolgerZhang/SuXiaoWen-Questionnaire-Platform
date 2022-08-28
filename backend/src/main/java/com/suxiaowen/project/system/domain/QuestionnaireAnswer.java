package com.suxiaowen.project.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 答卷表
 *
 * @TableName questionnaire_answer
 */
public class QuestionnaireAnswer implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 答卷ID submit答卷时的UUID
     */
    private String answerId;
    /**
     * 问卷ID 创建问卷时的UUID
     */
    private String questionnaireId;
    /**
     * 答卷内容 Document ID
     */
    private String detail;
    /**
     * 填写者
     */
    private Long userId;
    /**
     * 填写时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 填写IP
     */
    private String fillIp;
    /**
     * 星标（0-否 1-是）
     */
    private String starFlag;
    /**
     * 答卷状态（0-有效 1-无效 2-邀请）
     */
    private String answerStatus;

    /**
     * 答卷ID
     */
    public String getAnswerId() {
        return answerId;
    }

    /**
     * 答卷ID
     */
    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

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
     * 答卷内容 Document ID
     */
    public String getDetail() {
        return detail;
    }

    /**
     * 答卷内容 Document ID
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

    /**
     * 填写者
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 填写者
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 填写时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 填写时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 填写IP
     */
    public String getFillIp() {
        return fillIp;
    }

    /**
     * 填写IP
     */
    public void setFillIp(String fillIp) {
        this.fillIp = fillIp;
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
     * 答卷状态（0-有效 1-无效 2-邀请）
     */
    public String getAnswerStatus() {
        return answerStatus;
    }

    /**
     * 答卷状态（0-有效 1-无效 2-邀请）
     */
    public void setAnswerStatus(String answerStatus) {
        this.answerStatus = answerStatus;
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
        QuestionnaireAnswer other = (QuestionnaireAnswer) that;
        return (this.getAnswerId() == null ? other.getAnswerId() == null : this.getAnswerId().equals(other.getAnswerId()))
                && (this.getQuestionnaireId() == null ? other.getQuestionnaireId() == null : this.getQuestionnaireId().equals(other.getQuestionnaireId()))
                && (this.getDetail() == null ? other.getDetail() == null : this.getDetail().equals(other.getDetail()))
                && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getFillIp() == null ? other.getFillIp() == null : this.getFillIp().equals(other.getFillIp()))
                && (this.getStarFlag() == null ? other.getStarFlag() == null : this.getStarFlag().equals(other.getStarFlag()))
                && (this.getAnswerStatus() == null ? other.getAnswerStatus() == null : this.getAnswerStatus().equals(other.getAnswerStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getAnswerId() == null) ? 0 : getAnswerId().hashCode());
        result = prime * result + ((getQuestionnaireId() == null) ? 0 : getQuestionnaireId().hashCode());
        result = prime * result + ((getDetail() == null) ? 0 : getDetail().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getFillIp() == null) ? 0 : getFillIp().hashCode());
        result = prime * result + ((getStarFlag() == null) ? 0 : getStarFlag().hashCode());
        result = prime * result + ((getAnswerStatus() == null) ? 0 : getAnswerStatus().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", answerId=").append(answerId);
        sb.append(", questionnaireId=").append(questionnaireId);
        sb.append(", detail=").append(detail);
        sb.append(", userId=").append(userId);
        sb.append(", createTime=").append(createTime);
        sb.append(", fillIp=").append(fillIp);
        sb.append(", starFlag=").append(starFlag);
        sb.append(", answerStatus=").append(answerStatus);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}