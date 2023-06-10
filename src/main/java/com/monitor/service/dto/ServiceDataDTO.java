package com.monitor.service.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

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

    private LocalDateTime startTime;

    private LocalDateTime stopTime;

    private Long upTime;

    private Long downTime;

}

