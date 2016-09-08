package com.mrk.myordershop.resource;

import java.text.DecimalFormat;

public class MeltingAndSealSummaryResource {

	private int melting;

	private String seal;

	private long count;

	private long totalOrderCount;

	public int getMelting() {
		return melting;
	}

	public void setMelting(int melting) {
		this.melting = melting;
	}

	public String getSeal() {
		return seal;
	}

	public void setSeal(String seal) {
		this.seal = seal;
	}

	public double getPercentage() {
		return Double
				.valueOf(new DecimalFormat("#.00").format((Double
						.valueOf(this.count) / Double
						.valueOf(this.totalOrderCount)) * 100));
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getTotalOrderCount() {
		return totalOrderCount;
	}

	public void setTotalOrderCount(long totalOrderCount) {
		this.totalOrderCount = totalOrderCount;
	}

}
