package com.suxiaowen.project.system.domain.mongo;

import com.suxiaowen.common.xss.Xss;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class UniAccountInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Document ID
     */
    private String id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 学号
     */
    private String studentId;
    /**
     * 真实姓名
     */
    private String name;
    /**
     * 专业
     */
    private String major;
    /**
     * 年级
     */
    private String grade;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Xss(message = "学号不能包含脚本字符")
    @NotBlank(message = "学号不能为空")
    @Size(min = 0, max = 15, message = "学号长度不能超过15个字符")
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @Xss(message = "真实姓名不能包含脚本字符")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Xss(message = "专业不能包含脚本字符")
    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    @Xss(message = "年级不能包含脚本字符")
    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UniAccountInfo(" + id + "){" +
                "userId=" + userId +
                ", studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", major='" + major + '\'' +
                ", grade='" + grade + '\'' +
                '}';
    }
}
