package com.macauslot.recruitment_ms.util;

/**
 * 工作流枚举类
 */
public enum FlowEnum {

    FLOW1("HR篩選簡歷", "1.0", "處理中"),
    FLOW2("部門篩選簡歷", "2.0", "處理中"),
    FLOW3("HR預約面試", "3.0", "處理中"),
    FLOW4("HR面試", "4.0", "處理中"),
    FLOW5("部門面試", "5.0", "處理中"),
    FLOW6("部門審批", "6.0", "處理中"),
    FLOW7("HR審批", "7.0", "處理中"),
    FLOW8("HR發出聘請", "8.0", "處理中"),
    FLOW9("簽約", "9.0", "處理中"),
    FLOW10("入職中", "10.0", "處理中"),
    FLOW11("結束", "11.0", "已完成"),//（沒入職）
    FLOW12("已入職", "12.0", "已完成");


    private String cnName;
    private String code;
    private String briefStatus;

    private FlowEnum(String cnName, String code, String briefStatus) {
        this.cnName = cnName;
        this.code = code;
        this.briefStatus = briefStatus;

    }

    public String getCnName() {
        return cnName;
    }

    //    public void setCnName(String cnName) {
//        this.cnName = cnName;
//    }
    public String getCode() {
        return code;
    }
//    public void setCode(String code) {
//        this.code = code;
//    }

    public String getBriefStatus() {
        return briefStatus;
    }
}
