package com.msp.jdbc.util;

public enum SubjectEnum {

    Toán("Toán"),
    Văn("Văn"),
    Anh("Anh"),
    Lý("Lý"),
    Hóa("Hóa"),
    Sinh("Sinh"),
    Sử("Sử"),
    Địa("Địa"),
    GDCD("GDCD"),
    ;

    private String name;

    SubjectEnum(String name) {
        this.name = name;
    }
}
