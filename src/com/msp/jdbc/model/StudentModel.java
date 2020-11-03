package com.msp.jdbc.model;

import java.util.List;

public class StudentModel {

    private Long id;
    private String code;
    private String name;
    private String dateOfBirth;
    private String classCode;
    private float avgMark;
    private List<MarkBoardModel> markBoardList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public float getAvgMark() {
        return avgMark;
    }

    public void setAvgMark(float avgMark) {
        this.avgMark = avgMark;
    }

    public List<MarkBoardModel> getMarkBoardList() {
        return markBoardList;
    }

    public void setMarkBoardList(List<MarkBoardModel> markBoardList) {
        this.markBoardList = markBoardList;
    }
}
