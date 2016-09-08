package com.mrk.myordershop.bean.mail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.DateTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.mrk.myordershop.bean.User;
import com.mrk.myordershop.log.bean.LoginHistory;

public class LoginAttemptMessage implements Runnable {

	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger
			.getLogger(LoginAttemptMessage.class);

	@Autowired
	private JavaMailSenderImpl mailSender;
	@Autowired
	@Qualifier(value = "velocityEngine")
	private VelocityEngine velocityEngine;

	private String from;

	private LoginHistory history;

	private User user;

	@Override
	public void run() {
		mailSender.send(getMessage());
	}

	public void send(User user, LoginHistory history) {
		this.user = user;
		this.history = history;
		new Thread(this).start();
	}

	MimeMessagePreparator getMessage() {
		return new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
				helper.setFrom(from);
				helper.setTo(user.getEmail());
				helper.setSubject("Login Attempt "
						+ history.getAttempt().getValue());
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("date", new DateTool());
				model.put("userName", user.getName());
				model.put("history", history);
				model.put("currentYear",
						new SimpleDateFormat("yyyy").format(new Date()));

				@SuppressWarnings("deprecation")
				String html = VelocityEngineUtils.mergeTemplateIntoString(
						velocityEngine, "velocity/mail/common/loginAttempt.vm",
						model);
				helper.setText(html, true);

				log.info(html);
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
