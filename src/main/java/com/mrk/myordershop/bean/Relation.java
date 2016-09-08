package com.mrk.myordershop.bean;

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

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mrk.myordershop.constant.RelationDirection;
import com.mrk.myordershop.constant.RelationStatus;

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

	@OneToOne
	@JoinColumn(name = "RELATION_SETTING_ID")
	@Cascade({ CascadeType.ALL })
	private RelationSetting setting;

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

	public RelationSetting getSetting() {
		return setting;
	}

	public void setSetting(RelationSetting setting) {
		this.setting = setting;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Relation other = (Relation) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
