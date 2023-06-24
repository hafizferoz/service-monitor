package com.monitor.service.service;

public interface MailService {

	void sendText(String from, String to, String bcc, String cc, String subject, String body) throws Exception;
	void sendHTML(String from, String to, String bcc, String cc, String subject, String body) throws Exception;
}
