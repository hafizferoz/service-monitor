package com.monitor.service.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="service_data")
public class ServiceData implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5853841396318261742L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name = "service_name")
    private String name;
    
    @Column(name = "service_url")
    private String url;
    
    @Column(name = "start_url")
    private String startUrl;
    
    @Column(name = "stop_url")
    private String stopUrl;

    @Column(name = "service_status")
    private String status;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "stop_time")
    private LocalDateTime stopTime;

    @Column(name = "up_time")
    private String upTime;

    @Column(name = "down_time")
    private String downTime;
    
    @Column(name ="email_send_time")
    private LocalDateTime emailSendTime;

}

