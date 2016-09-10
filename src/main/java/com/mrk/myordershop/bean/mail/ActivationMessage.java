package com.mrk.myordershop.bean.mail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.mrk.myordershop.bean.User;

public class ActivationMessage implements Runnable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private JavaMailSenderImpl mailSender;
	@Autowired
	@Qualifier(value = "velocityEngine")
	private VelocityEngine velocityEngine;

	private String from;

	private User user;

	private String activationUrl;

	@Override
	public void run() {
		mailSender.send(getMessage(activationUrl));
	}

	public void send(String activationUrl, User user) {
		this.activationUrl = activationUrl;
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
				helper.setSubject("Activate your account");
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("activationUrl", activationUrl);
				model.put("userName", user.getName());
				model.put("currentYear", new SimpleDateFormat("yyyy").format(new Date()));
				System.out.println(VelocityEngineUtils.mergeTemplateIntoString(
						velocityEngine, "velocity/mail/common/activation.vm",
						model));
				helper.setText(VelocityEngineUtils.mergeTemplateIntoString(
						velocityEngine, "velocity/mail/common/activation.vm",
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
