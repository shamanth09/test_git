package com.mrk.myordershop.bean.dto;

import java.util.ArrayList;
import java.util.List;

public class SearchFilter {

	public enum SearchIn {
		CUSTOMER_NAME("in Customer Name"), WHOLESALER_NAME("in Wholesaler Name"), PRODUCT_NAME(
				"in Product Name"), ORDER_NO("in Order No"), ORDER_STATUS(
				"in Order Status"), SUPPLIER_NAME("in Supplier Name"), NAME(
				"in Name"), EMAIL("in Email"), MOBILE("in Mobile"), FIRM_NAME(
				"in Conpany Name");
		private String suffix;

		SearchIn(String suffix) {
			this.suffix = suffix;
		}

		public String getSuffix() {
			return this.suffix;
		}
	}

	private String query;

	private List<SearchIn> searchIn = new ArrayList<SearchIn>();

	public void fillRetailerSearch() {
		if (this.searchIn.isEmpty()) {
			this.searchIn.add(SearchIn.WHOLESALER_NAME);
			this.searchIn.add(SearchIn.PRODUCT_NAME);
			this.searchIn.add(SearchIn.ORDER_NO);
			this.searchIn.add(SearchIn.ORDER_STATUS);
		}
	}

	public void fillContactSearchIn() {
		if (this.searchIn.isEmpty()) {
			this.searchIn.add(SearchIn.NAME);
			this.searchIn.add(SearchIn.EMAIL);
			this.searchIn.add(SearchIn.FIRM_NAME);
			this.searchIn.add(SearchIn.MOBILE);
		}
	}

	public void fillWholesalerSearchOnOrder() {
		if (this.searchIn.isEmpty()) {
			this.searchIn.add(SearchIn.CUSTOMER_NAME);
			this.searchIn.add(SearchIn.MOBILE);
			this.searchIn.add(SearchIn.PRODUCT_NAME);
			this.searchIn.add(SearchIn.ORDER_NO);
			this.searchIn.add(SearchIn.ORDER_STATUS);
		}
	}

	public void fillWholesalerSearchOnWSOrder() {
		if (this.searchIn.isEmpty()) {
			this.searchIn.add(SearchIn.SUPPLIER_NAME);
			this.searchIn.add(SearchIn.PRODUCT_NAME);
			this.searchIn.add(SearchIn.ORDER_NO);
			this.searchIn.add(SearchIn.ORDER_STATUS);
		}
	}

	public void fillSupplierSearch() {
		if (this.searchIn.isEmpty()) {
			this.searchIn.add(SearchIn.WHOLESALER_NAME);
			this.searchIn.add(SearchIn.PRODUCT_NAME);
			this.searchIn.add(SearchIn.ORDER_NO);
			this.searchIn.add(SearchIn.ORDER_STATUS);
		}
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public List<SearchIn> getSearchIn() {
		return searchIn;
	}

	public void setSearchIn(List<SearchIn> searchIn) {
		this.searchIn = searchIn;
	}

	public void addSearchIn(SearchIn searchIn) {
		this.searchIn.add(searchIn);
	}
}
