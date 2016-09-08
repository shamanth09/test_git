package com.mrk.myordershop.bean.mail;

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

import com.mrk.myordershop.bean.Order;

public class OrderMessage implements Runnable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private JavaMailSenderImpl mailSender;
	@Autowired
	@Qualifier(value = "velocityEngine")
	private VelocityEngine velocityEngine;

	private String domainName;

	private String from;

	private Order order;

	@Override
	public void run() {
		if(order.getUser().getEmail() != null)
		mailSender.send(getRetailerMessage(order));
		if(order.getWholesaler().getEmail() != null)
		mailSender.send(getWholesalerMessage(order));
	}

	public void send(Order order, String serverName) {
		this.order = order;
		this.domainName = serverName;
//		new Thread(this).start();
	}

	MimeMessagePreparator getRetailerMessage(final Order order) {
		return new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
				helper.setFrom(from);
				helper.setTo(order.getUser().getEmail());
				helper.setSubject("Order placed successfully");
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("order", order);
				model.put("domain", domainName);
				helper.setText(VelocityEngineUtils.mergeTemplateIntoString(
						velocityEngine,
						"velocity/mail/retailer/order/success.vm", model), true);

			}
		};
	}

	MimeMessagePreparator getWholesalerMessage(final Order order) {
		return new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
				helper.setFrom(from);
				helper.setTo(order.getWholesaler().getEmail());
				helper.setSubject("Your Received an Order");
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("order", order);
				model.put("domain", domainName);
				helper.setText(VelocityEngineUtils.mergeTemplateIntoString(
						velocityEngine,
						"velocity/mail/wsaler/order/received.vm", model), true);

			}
		};
	}

	public JavaMailSenderImpl getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
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
