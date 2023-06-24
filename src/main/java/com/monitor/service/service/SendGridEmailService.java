package com.monitor.service.service;

import java.io.IOException;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Personalization;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SendGridEmailService implements MailService  {
	
	private SendGrid sendGridClient;
	
    @Autowired
    public SendGridEmailService(SendGrid sendGridClient) {
        this.sendGridClient = sendGridClient;
    }
    @Override
    public void sendText(String from, String to, String bcc, String cc, String subject, String body) throws Exception {
        Response response = sendEmail(from, to,bcc,cc, subject, new Content("text/plain", body));
        log.info("Status Code: " + response.getStatusCode() + ", Body: " + response.getBody() + ", Headers: "
                + response.getHeaders());
    }
    @Override
    public void sendHTML(String from, String to,String bcc, String cc, String subject, String body) throws Exception {
        Response response = sendEmail(from, to,bcc,cc, subject, new Content("text/html", body));
        log.info("Status Code: " + response.getStatusCode() + ", Body: " + response.getBody() + ", Headers: "
                + response.getHeaders());
    }
    private Response sendEmail(String from, String to,String bcc, String cc, String subject, Content content) throws Exception {
        Mail mail = new Mail(new Email(from), subject, new Email(to), content);
        mail.setReplyTo(new Email("no-reply@privasia.com"));
        Personalization personalization = new Personalization();
        HashSet<String> existingEmails = new HashSet<String>();
        personalization.addTo(new Email(to));
        existingEmails.add(to);
        if(StringUtils.isNotBlank(bcc) &&  !existingEmails.contains(bcc)){
        personalization.addBcc(new Email(bcc));
        existingEmails.add(bcc);
        }
        if(StringUtils.isNotBlank(cc) &&  !existingEmails.contains(cc) ){
        personalization.addCc(new Email(cc));
        existingEmails.add(cc);
        }
        mail.addPersonalization(personalization);
        Request request = new Request();
        Response response = null;
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            response= sendGridClient.api(request);
        } catch (IOException ex) {
            log.info(ex.getMessage());
            throw new Exception("Error while sending message via sendgrid" + ex.getMessage()); 
        }
        return response;
    }
}
