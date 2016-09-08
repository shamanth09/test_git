package com.mrk.myordershop.bean.mail;

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

import com.mrk.myordershop.bean.WholesalerOrder;

public class WholesalerOrderMessage implements Runnable {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(WholesalerOrderMessage.class.getName());
	@Autowired
	private JavaMailSenderImpl mailSender;
	@Autowired
	@Qualifier(value = "velocityEngine")
	private VelocityEngine velocityEngine;

	private String domainName;

	private String from;

	private WholesalerOrder order;

	@Override
	public void run() {
		log.info(order.getSupplier().getEmail());
		if (order.getSupplier().getEmail() != null)
			mailSender.send(getSuplierMessage(order));
		if (order.getUser().getEmail() != null)
			mailSender.send(getWholesalerMessage(order));
	}

	public void send(WholesalerOrder order, String serverName) {
		this.order = order;
		this.domainName = serverName;
//		new Thread(this).start();
	}

	MimeMessagePreparator getSuplierMessage(final WholesalerOrder order) {
		return new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
				helper.setFrom(from);
				helper.setTo(order.getSupplier().getEmail());
				helper.setSubject("Your Received an Order");
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("order", order);
				model.put("domain", domainName);
				helper.setText(VelocityEngineUtils.mergeTemplateIntoString(
						velocityEngine,
						"velocity/mail/supplier/order/received.vm", model),
						true);

			}
		};
	}

	MimeMessagePreparator getWholesalerMessage(final WholesalerOrder order) {
		return new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
				helper.setFrom(from);
				helper.setTo(order.getUser().getEmail());
				helper.setSubject("Order Fowared successfully");
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("order", order);
				model.put("domain", domainName);
				helper.setText(VelocityEngineUtils.mergeTemplateIntoString(
						velocityEngine,
						"velocity/mail/wsaler/order/success.vm", model), true);

			}
		};
	}

	public JavaMailSenderImpl getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
	}

	public WholesalerOrder getOrder() {
		return order;
	}

	public void setOrder(WholesalerOrder order) {
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
