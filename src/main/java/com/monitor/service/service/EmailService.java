package com.monitor.service.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.monitor.service.dto.ServiceDataDTO;
import com.monitor.service.model.ServiceData;
import com.monitor.service.repository.ServiceRepository;

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

	@Autowired
	@Qualifier("emailTemplateEngine")
	private TemplateEngine emailTemplateEngine;
	
	@Autowired
	private SendGridEmailService sendGridEmailService;
	
	@Autowired
	private ModelMapper mapper;
	
	 @Autowired
	private ServiceRepository serviceRepository;


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
		 TypeToken<List<ServiceData>> serviceDataTypeToken = new TypeToken<List<ServiceData>>() {};
		List<ServiceData> serviceDataList = mapper.map(serviceList, serviceDataTypeToken.getType());
		serviceDataList = filterServiceData(serviceDataList);
		if(!serviceDataList.isEmpty()) {
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
	
	public List<ServiceData> filterServiceData(List<ServiceData> serviceDataList) {
        LocalDateTime currentTime = LocalDateTime.now();

        return serviceDataList.stream()
                .filter(serviceData -> {
                	Optional<ServiceData> serviceDataOpt = serviceRepository.findById(serviceData.getId());
					if (serviceDataOpt.isPresent()) {
						ServiceData serviceDataDB = serviceDataOpt.get();
						if (serviceDataDB.getEmailSendTime() != null) {
							LocalDateTime emailSendTime = serviceDataDB.getEmailSendTime();
							Duration duration = Duration.between(emailSendTime, currentTime);
							if (duration.toMinutes() > 15) {
								serviceDataDB.setEmailSendTime(currentTime);
								serviceRepository.save(serviceDataDB);
								return true;
							}
							return false;
						}
						serviceData.setEmailSendTime(currentTime);
						serviceRepository.save(serviceData);
						return true;
					}
                    return false;
                   
                })
                .collect(Collectors.toList());
    }
}
