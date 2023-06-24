package com.monitor.service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.monitor.service.dto.ServiceDataDTO;

import lombok.extern.slf4j.Slf4j;

@Service("emailService")
@Slf4j
public class EmailService {

	public static final String EMAIL_BCC = "EMAIL_BCC";
	public static final String EMAIL_CC = "EMAIL_CC";

	@Value("${mail.sender}")
	private String senderEmail;
	
	@Value("${mail.to}")
	private String recieverEmail;
	
	@Value("${mail.companyName}")
	private String companyName;
	
	@Value("${mail.adminName}")
	private String adminName;

	private String emailContent = null;

	private TemplateEngine emailTemplateEngine;
	private SendGridEmailService sendGridEmailService;

	@Autowired
	public void setSendGridEmailService(SendGridEmailService sendGridEmailService) {
		this.sendGridEmailService = sendGridEmailService;
	}

	@Autowired
	public void setEmailTemplateEngine(@Qualifier("emailTemplateEngine") TemplateEngine emailTemplateEngine) {
		this.emailTemplateEngine = emailTemplateEngine;
	}

	public String prepareAndSendSendGridEmail(String recipient, String subject, Context context, String templateName) {
		try {
			emailContent = null;
			String emailBCC = null;
			String emailCC = null;
			if (!(context == null || context.getVariableNames() == null)) {
				if (context.getVariable(EMAIL_BCC) != null) {
					emailBCC = (String) context.getVariable(EMAIL_BCC);
				}
				if (context.getVariable(EMAIL_CC) != null) {
					emailCC = (String) context.getVariable(EMAIL_CC);
				}
			}
			emailContent = build(context, templateName);
			sendGridEmailService.sendHTML(senderEmail, recipient, emailCC, emailBCC, subject, emailContent);
		} catch (Exception e) {
			log.info("Error sending email: " + emailContent);
		}
		return emailContent;
	}

	public String build(Context context, String templateName) {
		return emailTemplateEngine.process(templateName, context);
	}

	public void sendEmail(List<ServiceDataDTO> serviceList) {

		String emailContent = null;
		String mailSubject = null;
		String recipient = recieverEmail;
		Context context = new Context();
		context.setVariable(EMAIL_BCC, recieverEmail);
		context.setVariable("serviceList", serviceList);
		context.setVariable("name", adminName);
		context.setVariable("companyName", companyName);
		mailSubject = "ALERT: SERVICE DOWN";

		emailContent = prepareAndSendSendGridEmail(recipient, mailSubject, context,
				EmailTemplate.MAIL_TEMPLATE.getValue());
		log.info("emailContent:" + emailContent);

	}

}
