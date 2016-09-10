package com.mrkinnoapps.myordershopadmin.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;

import com.mrkinnoapps.myordershopadmin.mail.ActivationMessage;
import com.mrkinnoapps.myordershopadmin.mail.ForgotPasswordMessage;
import com.mrkinnoapps.myordershopadmin.mail.LoginAttemptMessage;
import com.mrkinnoapps.myordershopadmin.mail.MailMessageFactory;
import com.mrkinnoapps.myordershopadmin.mail.OrderMessage;
import com.mrkinnoapps.myordershopadmin.mail.WholesalerOrderMessage;
import com.mrkinnoapps.myordershopadmin.util.notifier.IOSPNotifier;
import com.mrkinnoapps.myordershopadmin.util.notifier.WebNotifier;

@Configuration
@PropertySource("classpath:/META-INF/mail.properties")
public class MailConfig {

	@Value("${push.ios.passphrase}")
	private String pushIosPassphrase;

	@Value("${push.ios.p12}")
	private String pushIosP12FileName;

	@Value("${mail.host}")
	private String host;

	@Value("${mail.port}")
	private String port;

	@Value("${mail.username}")
	private String username;

	@Value("${mail.password}")
	private String password;

	@Bean
	public IOSPNotifier getIospNotifier() {
		return new IOSPNotifier(this.pushIosPassphrase, this.pushIosP12FileName);
	}

	@Bean
	public WebNotifier getWebNotifier() {
		return new WebNotifier();
	}

	@Bean(name = "velocityEngine")
	public VelocityEngineFactoryBean getVelocityEngineFactoryBean() {
		Properties properties = new Properties();
		properties.put("resource.loader", "class");
		properties
				.put("class.resource.loader.class",
						"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		VelocityEngineFactoryBean bean = new VelocityEngineFactoryBean();
		bean.setVelocityProperties(properties);
		return bean;
	}

	@Bean
	@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
	public OrderMessage orderMessage() {
		OrderMessage message = new OrderMessage();
		message.setFrom(username);
		return message;
	}

	@Bean
	@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
	public WholesalerOrderMessage wholesalerOrderMessage() {
		WholesalerOrderMessage message = new WholesalerOrderMessage();
		message.setFrom(username);
		return message;
	}

	@Bean
	@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
	public ActivationMessage activationMessage() {
		ActivationMessage message = new ActivationMessage();
		message.setFrom(username);
		return message;
	}

	@Bean
	@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
	public ForgotPasswordMessage forgotPasswordMessage() {
		ForgotPasswordMessage message = new ForgotPasswordMessage();
		message.setFrom(username);
		return message;
	}

	@Bean
	@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
	public LoginAttemptMessage loginAttemptMessage() {
		LoginAttemptMessage message = new LoginAttemptMessage();
		message.setFrom(username);
		return message;
	}

	@Bean
	@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
	public JavaMailSenderImpl javaMailSenderImpl() {
		JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();
		javaMailSenderImpl.setHost(host);
		javaMailSenderImpl.setPassword(password);
		javaMailSenderImpl.setPort(Integer.parseInt(port));
		javaMailSenderImpl.setUsername(username);
		Properties properties=new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "false");
		properties.put("mail.smtp.quitwait", "false");
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.put("mail.smtp.socketFactory.fallback", "false");
		properties.put("mail.debug", "false");
		javaMailSenderImpl.setJavaMailProperties(properties);
		
		return javaMailSenderImpl;
	}
	
	@Bean
	@Qualifier(value = "mailMessageFactory")
	public MailMessageFactory getMailMessageFactory() {
		MailMessageFactory message = new MailMessageFactory() {

			@Override
			public WholesalerOrderMessage getWholesalerOrderMessage() {
				return wholesalerOrderMessage();
			}

			@Override
			public OrderMessage getOrderMessage() {
				return orderMessage();
			}

			@Override
			public LoginAttemptMessage getLoginAttemptMessage() {
				return loginAttemptMessage();
			}

			@Override
			public ForgotPasswordMessage getForgotPasswordMessage() {
				return forgotPasswordMessage();
			}

			@Override
			public ActivationMessage getActivationMessage() {
				return activationMessage();
			}
		};
		return message;
	}
}
