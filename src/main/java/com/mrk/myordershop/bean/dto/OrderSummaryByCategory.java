package com.mrk.myordershop.bean.dto;

import org.springframework.hateoas.core.Relation;

/**
 * @author Naveen
 *
 */
@Relation(collectionRelation = "content")
public class OrderSummaryByCategory extends OrderSummary {

	private String name;

	private Integer categoryId;

	private long totalOrders;
	
	private Double weight;

	public OrderSummaryByCategory(Class entity, Integer categoryId,
			String name, long totalOrders, Double weight) {
		super(entity);
		this.categoryId = categoryId;
		this.name = name;
		this.totalOrders = totalOrders;
		this.weight = weight;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public long getTotalOrders() {
		return totalOrders;
	}

	public void setTotalOrders(long totalOrders) {
		this.totalOrders = totalOrders;
	}

	public void setTotalOrders(String totalOrders) {
		this.totalOrders = Integer.valueOf(totalOrders);
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

}
