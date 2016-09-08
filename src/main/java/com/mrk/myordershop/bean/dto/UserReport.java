package com.mrk.myordershop.bean.dto;

public class UserReport {

	private long activeUserCount;

	private long inActiveUserCount;

	public long getActiveUserCount() {
		return activeUserCount;
	}

	public void setActiveUserCount(long activeUserCount) {
		this.activeUserCount = activeUserCount;
	}

	public long getInActiveUserCount() {
		return inActiveUserCount;
	}

	public void setInActiveUserCount(long inActiveUserCount) {
		this.inActiveUserCount = inActiveUserCount;
	}

	public long getTotal() {
		return this.inActiveUserCount + this.activeUserCount;
	}

}
