package com.mrkinnoapps.myordershopadmin.mail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.mrkinnoapps.myordershopadmin.bean.entity.User;

public class ForgotPasswordMessage implements Runnable {

	private static final long serialVersionUID = 1L;

	private Logger log = Logger.getLogger(ForgotPasswordMessage.class);

	@Autowired
	private JavaMailSenderImpl mailSender;
	@Autowired
	@Qualifier(value = "velocityEngine")
	private VelocityEngine velocityEngine;

	private String from;

	private User user;

	private String actionUrl;

	@Override
	public void run() {
		mailSender.send(getMessage(actionUrl));
	}

	public void send(String actionUrl, User user) {
		this.actionUrl = actionUrl;
		this.user = user;
		new Thread(this).start();
	}

	MimeMessagePreparator getMessage(final String activationUrl) {
		return new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
				helper.setFrom(from);
				helper.setTo(user.getEmail());
				helper.setSubject("My Order Shop Password Reset");
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("activationUrl", activationUrl);
				model.put("userName", user.getName());
				model.put("currentYear",
						new SimpleDateFormat("yyyy").format(new Date()));
				log.debug(VelocityEngineUtils.mergeTemplateIntoString(
						velocityEngine, "velocity/mail/common/frgtpws.vm",
						model));
				helper.setText(VelocityEngineUtils.mergeTemplateIntoString(
						velocityEngine, "velocity/mail/common/frgtpws.vm",
						model), true);

			}
		};
	}

	public JavaMailSenderImpl getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}
}
