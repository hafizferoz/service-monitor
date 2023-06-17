package com.monitor.service.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ServiceDataDTO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6083100796675664890L;

	private Integer id;

    private String name;
    
    private String url;
    
    private String startUrl;
    
    private String stopUrl;

    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime stopTime;

    private String upTime;

    private String downTime;

}

