package com.monitor.service.service;

public enum EmailTemplate {

	MAIL_TEMPLATE("mailTemplate");

	private final String emailTemplate;
	
	private EmailTemplate(String emailTemplate) {
		this.emailTemplate = emailTemplate;
	}

	String getValue() {
		return emailTemplate;
	} 
}
