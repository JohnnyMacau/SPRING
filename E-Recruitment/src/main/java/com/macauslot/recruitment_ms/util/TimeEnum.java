package com.macauslot.recruitment_ms.util;

public enum TimeEnum {

    /**
     * 时分秒
     */
    YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),
    YYYY_MM_DD("yyyy-MM-dd");





    private final String name;

    TimeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
