package com.macauslot.recruitment_ms.util;

public enum TimeEnum2 {
    /**
     * $system_try_time 這個是相隔多久之内尝试会叠加尝试次数
     */
    system_try_time(300),
    /**
     * $system_lock_time 這個是鎖的時間，單位分鐘
     */
    system_lock_time (30),
    /**
     * 尝试次数为5就锁
     */
    system_try_count(5);


    private final long num;

    TimeEnum2(long num) {
        this.num = num;
    }

    public long getNum() {
        return num;
    }
}
