package com.mrk.myordershop.bean.dto;

public class View {

	public interface Basic{};
	public interface Moderate{};
	public interface Detailed{};
	public interface Meta{};
	
	public interface OrderMeta{}
	public interface OrderBasic{}
	public interface OrderModerate extends OrderBasic{}
	public interface OrderDetailed extends OrderModerate{}
	
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

	public interface SupplierSummary extends UserBasic, SPOStatus, ContactBasic{}

}
