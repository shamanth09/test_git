package com.mrk.myordershop.bean;

import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MOS_FIELD")
public class Field {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private int id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "VALUE")
	private String value;

	public Field() {
	}

	public Field(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public static Field field(String name, String value) {
		return new Field(name, value);
	}

	public static FieldList fieldList() {
		return new FieldList();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static class FieldList extends ArrayList<Field> {
		private static final long serialVersionUID = 1L;

		public FieldList addField(Field e) {
			super.add(e);
			return this;
		}

		public FieldList addField(String name, Object value) {
			super.add(Field.field(name, value.toString()));
			return this;
		}
	}

	@Override
	public String toString() {
		return "Field [id=" + id + ", name=" + name + ", value=" + value + "]";
	}
	
}
