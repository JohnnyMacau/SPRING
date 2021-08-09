package com.macauslot.recruitmentadmin.util;

/**
 * 工作流枚举类
 *
 */
public enum FlowEnum {
    /**
     * 各個流轉節點
     */
    FLOW0("職位申請", "0.0", "0"),

    FLOW1("HR篩選簡歷", "1.0", "1"),
    FLOW2("部門篩選簡歷", "2.0", "2"),
    FLOW3("HR預約面試", "3.0", "3"),
    FLOW4("HR面試", "4.0", "4"),
    FLOW5("部門面試", "5.0", "5"),
    FLOW6("部門審批", "6.0", "6"),
    FLOW7("HR審批", "7.0", "7"),
    FLOW8("HR發出聘請", "8.0", "8"),
    FLOW9("簽約", "9.0", "9"),
    FLOW10("入職中", "10.0", "10"),
    FLOW11("結束", "11.0", "11"),//（沒入職）
    FLOW12("已入職", "12.0", "12");

    private final String cnName;
    private final String code;
    private final String code2;

    FlowEnum(String cnName, String code, String code2) {
        this.cnName = cnName;
        this.code = code;
        this.code2 = code2;
    }

    public String getCnName() {
        return cnName;
    }

    public String getCode() {
        return code;
    }

    public String getCode2() {
        return code2;
    }
}
