package com.mrkinnoapps.myordershopadmin.mail;

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

import com.mrkinnoapps.myordershopadmin.bean.entity.User;

public class ActivationMessage implements Runnable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private JavaMailSenderImpl mailSender;
	@Autowired
	@Qualifier(value = "velocityEngine")
	private VelocityEngine velocityEngine;

	private String from;

	private User user;


	@Override
	public void run() {
		mailSender.send(getMessage());
	}

	public void send(User user) {
		this.user = user;
		new Thread(this).start();
	}

	MimeMessagePreparator getMessage() {
		return new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
				helper.setFrom(from);
				helper.setTo(user.getEmail());
				helper.setSubject("Welcome to My Order Shop");
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("userName", user.getName());
				model.put("password", user.getPassword());
				model.put("email", user.getEmail());
				model.put("mobile", user.getMobile());
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
