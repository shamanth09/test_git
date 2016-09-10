package com.mrk.myordershop.util;

import java.util.List;

import org.springframework.data.domain.PageImpl;

public class MyPage<T> extends PageImpl<T> {

	private int total;
	private int offset;
	private int limit;

	public MyPage(List<T> content, int total, int offset, int limit) {
		super(content);
		this.total = total;
		this.offset = offset;
		this.limit = limit;
	}

	public MyPage(List<T> content) {
		super(content);
	}

	public int getPageNo() {
		int remail = (this.offset + this.getContent().size()) % this.limit;
		if (remail > 0)
			return ((this.offset + this.getContent().size()) / this.limit) + 1;
		return ((this.offset + this.getContent().size()) / this.limit);
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public List<T> getContent() {
		return super.getContent();
	}

}
