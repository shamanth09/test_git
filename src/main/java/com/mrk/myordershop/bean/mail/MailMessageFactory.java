package com.mrk.myordershop.bean.mail;

public interface MailMessageFactory {

	OrderMessage getOrderMessage();

	WholesalerOrderMessage getWholesalerOrderMessage();

	ActivationMessage getActivationMessage();

	ForgotPasswordMessage getForgotPasswordMessage();

	LoginAttemptMessage getLoginAttemptMessage();
}
