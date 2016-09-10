package com.mrkinnoapps.myordershopadmin.mail;

public interface MailMessageFactory {

	OrderMessage getOrderMessage();

	WholesalerOrderMessage getWholesalerOrderMessage();

	ActivationMessage getActivationMessage();

	ForgotPasswordMessage getForgotPasswordMessage();

	LoginAttemptMessage getLoginAttemptMessage();
}
