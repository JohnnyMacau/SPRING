package com.macauslot.recruitmentadmin.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NoticeVO {

    private String cnFName;
    private String cnLName;
    private String solution;
    private String areaCode;
    private String mobile;
    private String emailAddress;
    private String interviewTime;
    private String meetingRoom;
    private String job_id;
    private String jobTitle;
    private String org;
    
    
}
