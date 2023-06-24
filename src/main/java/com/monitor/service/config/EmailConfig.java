package com.monitor.service.config;

import java.util.Collections;

import javax.servlet.MultipartConfigElement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.support.ErrorPageFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import com.sendgrid.SendGrid;

@Configuration
public class EmailConfig {

	@Value("${mail.sendgrid.key}")
	String sendGridAPIKey;

	@Bean(name = "sendGridClient")
	public SendGrid getSendGridClient() {
		return new SendGrid(sendGridAPIKey);
	}

	public static void main(String[] args) {
		SpringApplication.run(EmailConfig.class, args);
	}

	@Bean(initMethod = "initialize", name = "emailTemplateEngine")
	public TemplateEngine getEmailTemplateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		// Resolver for HTML emails
		templateEngine.addTemplateResolver(htmlTemplateResolver());
		// Resolver for TEXT emails
		// templateEngine.addTemplateResolver(textTemplateResolver());
		return templateEngine;
	}

	@Bean
	public ITemplateResolver htmlTemplateResolver() {
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setOrder(Integer.valueOf(1));
		//templateResolver.setResolvablePatterns(Collections.singleton("html/*"));
		templateResolver.setPrefix("apptemplates/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode("HTML5");
		templateResolver.setCharacterEncoding("UTF-8");
		templateResolver.setCacheable(false);
		return templateResolver;
	}

	@Bean
	public ITemplateResolver textTemplateResolver() {
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setOrder(Integer.valueOf(2));
		templateResolver.setResolvablePatterns(Collections.singleton("text/*"));
		templateResolver.setPrefix("apptemplates/");
		templateResolver.setSuffix(".txt");
		templateResolver.setTemplateMode("TEXT");
		templateResolver.setCharacterEncoding("UTF-8");
		templateResolver.setCacheable(false);
		return templateResolver;
	}

//	@Bean
//	public Filter characterEncodingFilter() {
//		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
//		characterEncodingFilter.setEncoding("UTF-8");
//		characterEncodingFilter.setForceEncoding(true);
//		return characterEncodingFilter;
//	}

	@Bean
	public ErrorPageFilter errorPageFilter() {
		return new ErrorPageFilter();
	}

	@Bean
	public FilterRegistrationBean<ErrorPageFilter> disableSpringBootErrorFilter(ErrorPageFilter filter) {
		FilterRegistrationBean<ErrorPageFilter> filterRegistrationBean = new FilterRegistrationBean<ErrorPageFilter>();
		filterRegistrationBean.setFilter(filter);
		filterRegistrationBean.setEnabled(false);
		return filterRegistrationBean;
	}

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize(DataSize.ofKilobytes(128000));
		factory.setMaxRequestSize(DataSize.ofKilobytes(128000));
		return factory.createMultipartConfig();
	}

}
