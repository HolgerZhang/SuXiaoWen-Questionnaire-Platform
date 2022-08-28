package com.suxiaowen.project.system.domain.mongo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 问卷逻辑项
 * 用于MongoDB
 *
 * @author Holger
 */
public class QLogic implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Document ID
     */
    private String id;

    /**
     * 归属的题目ID
     */
    private String belongs;
    /**
     * 逻辑类型：转跳|隐藏|显示
     */
    private String type;
    /**
     * 逻辑具体内容
     * C 文本：该题目选中了指定文本
     * T：该题目已填写（有非空文本）
     * X 文本：该题目答案中包含指定文本
     */
    private String detail;
    /**
     * 目标题目ID列表
     * 当逻辑类型为转跳时长度必为1
     */
    private List<String> targets;
    /**
     * 反向选择，true表示上述条件不满足时逻辑成立
     */
    private Boolean negative;

    public QLogic() {
        targets = new ArrayList<>();
        negative = false;
    }

    public String getBelongs() {
        return belongs;
    }

    public void setBelongs(String belongs) {
        this.belongs = belongs;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getTargets() {
        return targets;
    }

    public void setTargets(List<String> targets) {
        this.targets = targets;
    }

    public Boolean getNegative() {
        return negative;
    }

    public void setNegative(Boolean negative) {
        this.negative = negative;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "QLogic(" + id + "){" +
                "belongs='" + belongs + '\'' +
                ", type='" + type + '\'' +
                ", detail='" + detail + '\'' +
                ", targets=" + targets +
                ", negative=" + negative +
                '}';
    }
}
