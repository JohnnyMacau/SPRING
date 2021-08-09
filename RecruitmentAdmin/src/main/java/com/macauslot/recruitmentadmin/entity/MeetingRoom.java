package com.macauslot.recruitmentadmin.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@DynamicUpdate
@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
@Table(name = "meeting_room")
public class MeetingRoom extends BaseEntity<MeetingRoom> implements Serializable {
    private static final long serialVersionUID = 6557355280027711200L;
    private Integer meetingRoomId;
    private String meetingRoomName;



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_room_id", nullable = false)
	public Integer getMeetingRoomId() {
		return meetingRoomId;
	}


	public void setMeetingRoomId(Integer meetingRoomId) {
		this.meetingRoomId = meetingRoomId;
	}


    @Column(name = "meeting_room_name", nullable = false)
	public String getMeetingRoomName() {
		return meetingRoomName;
	}


	public void setMeetingRoomName(String meetingRoomName) {
		this.meetingRoomName = meetingRoomName;
	}

    
}
