package com.mrk.myordershop.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.hateoas.Identifiable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.mrk.myordershop.bean.dto.View;
import com.mrk.myordershop.constant.ActiveFlag;
import com.mrk.myordershop.constant.Gender;

@Entity
@Table(name = "MOS_USER")
@org.hibernate.annotations.GenericGenerator(name = "uuid", strategy = "uuid.hex")
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements Serializable, Identifiable<String> {

	private static final long serialVersionUID = 1L;
	
	@JsonView(View.UserBasic.class)
	@Id
	@GeneratedValue(generator = "uuid")
	@Column(name = "ID")
	private String id;

	@JsonView(View.UserMeta.class)
	@Column(name = "ACTIVE_FLAG")
	@Enumerated(EnumType.STRING)
	private ActiveFlag activeFlag;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "MOS_USER_USERROLES", joinColumns = { @JoinColumn(name = "USERT_ID", referencedColumnName = "ID") }, inverseJoinColumns = { @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID") })
	private Set<UserRole> userRoles = new HashSet<UserRole>();

	@JsonView(View.UserBasic.class)
	@Column(name = "NAME")
	private String name;

	@JsonView(View.UserBasic.class)
	@Column(name = "EMAIL", unique = true)
	private String email;

	@JsonIgnore
	@Column(name = "PASSWORD")
	private String password;

	@JsonView(View.UserDetail.class)
	@Column(name = "GENDER")
	@Enumerated(EnumType.STRING)
	private Gender gender;

	@JsonView(View.UserBasic.class)
	@Column(name = "MOBILE", unique = true)
	private String mobile;

	@JsonView(View.UserDetail.class)
	@Column(name = "TELEPHONE")
	private String telephone;

	@JsonView(View.UserDetail.class)
	@Temporal(TemporalType.DATE)
	@Column(name = "DOB")
	private Date dob;

	@JsonView(View.UserMeta.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIMESTAMP")
	private Date createTimestamp = new Date();

	@JsonView(View.UserMeta.class)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIMESTAMP")
	private Date updateTimestamp;

	@JsonIgnore
	@OneToOne(mappedBy = "user")
	private Address address;

	@JsonIgnore
	@OneToOne
	@Cascade(value = { CascadeType.SAVE_UPDATE })
	@JoinColumn(name = "IMAGE_ID")
	private Image image;

	@JsonView(View.UserBasic.class)
	@JsonProperty("roles")
	public ArrayList<String> getRolesAsArray() {
		ArrayList<String> roles = new ArrayList<String>();
		for (UserRole role : this.userRoles) {
			roles.add(role.getRole().toString());
		}
		return roles;
	}

	@JsonView(View.UserBasic.class)
	public String getFirmName() {
		return address.getTitle();
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getId() {

		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ActiveFlag getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(ActiveFlag activeFlag) {
		this.activeFlag = activeFlag;
	}

	public Set<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Date getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Date createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", activeFlag=" + activeFlag + ", userRoles="
				+ userRoles + ", name=" + name + ", email=" + email
				+ ", password=" + password + ", gender=" + gender + ", mobile="
				+ mobile + ", telephone=" + telephone + ", createTimestamp="
				+ createTimestamp + ", updateTimestamp=" + updateTimestamp
				+ "]";
	}

}
