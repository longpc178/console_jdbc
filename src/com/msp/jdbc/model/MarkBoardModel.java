package com.msp.jdbc.model;

public class MarkBoardModel {

    private Long id;
    private Long studentId;
    private String subjectName;
    private float mark;
    private int markYear;

    public MarkBoardModel(Long studentId, String subjectName, float mark, int markYear) {
        this.studentId = studentId;
        this.subjectName = subjectName;
        this.mark = mark;
        this.markYear = markYear;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public float getMark() {
        return mark;
    }

    public void setMark(float mark) {
        this.mark = mark;
    }

    public int getMarkYear() {
        return markYear;
    }

    public void setMarkYear(int markYear) {
        this.markYear = markYear;
    }
}
