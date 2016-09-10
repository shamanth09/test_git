package com.mrk.myordershop.bean;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mrk.myordershop.constant.ActiveFlag;

/**
 * @author Naveen
 * 
 */
@Entity
@Table(name = "MOS_NOTIFICATION")
@Relation(collectionRelation = "content")
public class Notification {

	public enum TIER {
		neworder, orderstatuschange, relationrequest, relationstatuschange, relationremove
	};

	@JsonIgnore
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private int id;

	@JsonIgnore
	@Column(name = "ACTIVE_FLAG")
	@Enumerated(EnumType.STRING)
	private ActiveFlag activeFlag;

	@JsonIgnore
	@Column(name = "USER_ID")
	private String userId;

	@Column(name = "TIER")
	@Enumerated(EnumType.STRING)
	private TIER tier;

	@Column(name = "MESSAGE")
	private String message;

	@JsonIgnore
	@OneToMany(fetch = FetchType.EAGER, cascade = javax.persistence.CascadeType.ALL)
	@JoinTable(name = "MOS_NOTIFICATION_FIELD", joinColumns = { @JoinColumn(name = "NOTIFICATION_ID", referencedColumnName = "ID") }, inverseJoinColumns = { @JoinColumn(name = "FIELD_ID", referencedColumnName = "ID") })
	private List<Field> fields;

	@JsonIgnore
	@Column(name = "CREATE_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTimestamp;

	public Notification(TIER tier, String userId) {
		this.tier = tier;
		this.userId = userId;
		this.createTimestamp = new Date();
	}

	public Notification() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ActiveFlag getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(ActiveFlag activeFlag) {
		this.activeFlag = activeFlag;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public TIER getTier() {
		return tier;
	}

	public void setTier(TIER tier) {
		this.tier = tier;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Field> getFields() {
		return fields;
	}

	public Date getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Date createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	@JsonProperty("content")
	public Map<String, Object> getFieldsAsMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("id", this.id);
		result.put("tier", this.tier);
		result.put("createTimestamp", this.createTimestamp);
		if (this.fields != null && this.fields.size() > 0) {
			for (Field field : this.fields) {
				result.put(field.getName(), field.getValue());
			}
		}
		return result;
	}

	public Field findField(String fieldName) {
		Field result = null;
		if (this.fields != null && !this.fields.isEmpty()) {
			for (Field field : this.fields) {
				if (field.getName().equals(fieldName)) {
					result = field;
				}
			}
		}
		return result;
	}

	public void setFields(List<Field> field) {
		this.fields = field;
	}

	@Override
	public String toString() {
		return "Notification [id=" + id + ", activeFlag=" + activeFlag
				+ ", userId=" + userId + ", tier=" + tier + ", message="
				+ message + ", fields=" + fields + ", createTimestamp="
				+ createTimestamp + "]";
	}

}
