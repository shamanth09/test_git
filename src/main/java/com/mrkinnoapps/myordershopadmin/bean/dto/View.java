package com.mrkinnoapps.myordershopadmin.bean.dto;

public class View {

	public interface Basic{};
	public interface Detailed{};
	public interface Meta{};
	
	public interface OrderMeta{}
	public interface OrderBasic{}
	public interface OrderDetailed extends OrderBasic{}
	
	/* Order Status view */
	public interface Status {}
	public interface WSOStatus extends Status {}
	public interface SPOStatus extends Status {}

	/* User View */
	public interface UserMeta{}
	public interface UserBasic{}
	public interface UserDetail extends UserBasic{}
	
	/* Contact View */
	public interface ContactBasic{}
	public interface ContactDetail extends ContactBasic{}

	/* Summary View */
	public interface CustomerSummary extends UserBasic, WSOStatus, ContactBasic{}

}
