package com.school.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@ToString
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Institude")
public class Institude {
    private Integer id;
    private String name;
    private Integer universityId;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

   @Basic
   @Column(name = "Name", nullable = false, length = 50)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	@Basic
	@Column(name = "University_Id", nullable = false)
	public Integer getUniversityId() {
		return universityId;
	}

	public void setUniversityId(Integer universityId) {
		this.universityId = universityId;
	}


}
