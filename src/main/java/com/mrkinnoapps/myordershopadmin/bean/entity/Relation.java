package com.mrkinnoapps.myordershopadmin.bean.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mrkinnoapps.myordershopadmin.bean.constant.RelationDirection;
import com.mrkinnoapps.myordershopadmin.bean.constant.RelationStatus;

@Entity
@Table(name = "MOS_RELATION")
public class Relation implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private long id;

	@Column(name = "STATUS")
	@Enumerated(EnumType.STRING)
	private RelationStatus status;

	@JsonIgnore
	@Transient
	private String currentUserEmail;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "PRIMARY_USER_ID")
	private User primaryUser;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "SECONDARY_USER_Id")
	private User secondaryUser;

	@Column(name = "CREATE_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTimeStamp;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public RelationStatus getStatus() {
		return status;
	}

	public void setStatus(RelationStatus status) {
		this.status = status;
	}

	public User getPrimaryUser() {
		return primaryUser;
	}

	public void setPrimaryUser(User primaryUser) {
		this.primaryUser = primaryUser;
	}

	public User getSecondaryUser() {
		return secondaryUser;
	}

	public void setSecondaryUser(User secondaryUser) {
		this.secondaryUser = secondaryUser;
	}

	@JsonProperty
	public Date getCreateTimeStamp() {
		return createTimeStamp;
	}

	@JsonIgnore
	public void setCreateTimeStamp(Date createTimeStamp) {
		this.createTimeStamp = createTimeStamp;
	}

	public String getCurrentUserEmail() {
		return currentUserEmail;
	}

	public void setCurrentUserEmail(String currentUserName) {
		this.currentUserEmail = currentUserName;
	}

	@JsonProperty("user")
	public User getUser() {
		return this.currentUserEmail != null
				&& this.currentUserEmail.equalsIgnoreCase(this.primaryUser
						.getEmail()) ? secondaryUser
				: this.currentUserEmail != null
						&& this.currentUserEmail
								.equalsIgnoreCase(this.secondaryUser.getEmail()) ? primaryUser
						: null;
	}

	@JsonProperty("direction")
	public String getDirection() {
		return this.currentUserEmail != null
				&& this.currentUserEmail.equalsIgnoreCase(this.primaryUser
						.getEmail()) ? RelationDirection.FROM_YOU.toString()
				: this.currentUserEmail != null
						&& this.currentUserEmail
								.equalsIgnoreCase(this.secondaryUser.getEmail()) ? RelationDirection.TO_YOU
						.toString() : null;
	}

}
