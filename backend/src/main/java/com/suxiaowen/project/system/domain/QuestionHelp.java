package com.suxiaowen.project.system.domain;

import java.io.Serializable;

/**
 * 互助平台表
 *
 * @TableName question_help
 */
public class QuestionHelp implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 互助任务ID
     */
    private String taskId;
    /**
     * 目标问卷ID
     */
    private String questionnaireId;
    /**
     * 悬赏积分
     */
    private Long price;
    /**
     * 目标个数
     */
    private Long amount;
    /**
     * 已填写个数
     */
    private Long counts;
    /**
     * 启用状态（0-启用 1-停用）
     */
    private String enabled;

    /**
     * 互助任务ID
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * 互助任务ID
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    /**
     * 目标问卷ID
     */
    public String getQuestionnaireId() {
        return questionnaireId;
    }

    /**
     * 目标问卷ID
     */
    public void setQuestionnaireId(String questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    /**
     * 悬赏积分
     */
    public Long getPrice() {
        return price;
    }

    /**
     * 悬赏积分
     */
    public void setPrice(Long price) {
        this.price = price;
    }

    /**
     * 目标个数
     */
    public Long getAmount() {
        return amount;
    }

    /**
     * 目标个数
     */
    public void setAmount(Long amount) {
        this.amount = amount;
    }

    /**
     * 已填写个数
     */
    public Long getCounts() {
        return counts;
    }

    /**
     * 已填写个数
     */
    public void setCounts(Long counts) {
        this.counts = counts;
    }

    /**
     * 启用状态（0-启用 1-停用）
     */
    public String getEnabled() {
        return enabled;
    }

    /**
     * 启用状态（0-启用 1-停用）
     */
    public void setEnabled(String enabled) {
        this.enabled = enabled;
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
        QuestionHelp other = (QuestionHelp) that;
        return (this.getTaskId() == null ? other.getTaskId() == null : this.getTaskId().equals(other.getTaskId()))
                && (this.getQuestionnaireId() == null ? other.getQuestionnaireId() == null : this.getQuestionnaireId().equals(other.getQuestionnaireId()))
                && (this.getPrice() == null ? other.getPrice() == null : this.getPrice().equals(other.getPrice()))
                && (this.getAmount() == null ? other.getAmount() == null : this.getAmount().equals(other.getAmount()))
                && (this.getCounts() == null ? other.getCounts() == null : this.getCounts().equals(other.getCounts()))
                && (this.getEnabled() == null ? other.getEnabled() == null : this.getEnabled().equals(other.getEnabled()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getTaskId() == null) ? 0 : getTaskId().hashCode());
        result = prime * result + ((getQuestionnaireId() == null) ? 0 : getQuestionnaireId().hashCode());
        result = prime * result + ((getPrice() == null) ? 0 : getPrice().hashCode());
        result = prime * result + ((getAmount() == null) ? 0 : getAmount().hashCode());
        result = prime * result + ((getCounts() == null) ? 0 : getCounts().hashCode());
        result = prime * result + ((getEnabled() == null) ? 0 : getEnabled().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", taskId=").append(taskId);
        sb.append(", questionnaireId=").append(questionnaireId);
        sb.append(", price=").append(price);
        sb.append(", amount=").append(amount);
        sb.append(", counts=").append(counts);
        sb.append(", enabled=").append(enabled);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}