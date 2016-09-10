package com.mrk.myordershop.bean.dto;

import org.springframework.hateoas.core.Relation;

import com.mrk.myordershop.bean.dto.SearchFilter.SearchIn;
import com.mrk.myordershop.constant.Role;

@Relation(collectionRelation = "content")
public class UserSearchResource extends SearchResource {

	private Role role;

	public UserSearchResource(String result, SearchIn field, String lable,
			Class entity) {
		super(result, field, lable, entity);
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}
