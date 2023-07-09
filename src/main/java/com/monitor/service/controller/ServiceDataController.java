package com.monitor.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.monitor.service.dto.ServiceDataDTO;
import com.monitor.service.dto.ServiceResponse;
import com.monitor.service.service.ServiceDataService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/servicedata")
@Slf4j
public class ServiceDataController {
	
    @Autowired
    private ServiceDataService serviceDataService;
    
    @GetMapping("/load")
    public List<ServiceDataDTO> getServiceData() {
		List<ServiceDataDTO> serviceListDTO = serviceDataService.getServiceData();
		log.info("Response send: {}",serviceListDTO);
		return serviceListDTO;
    }

    @PostMapping("/add")
    public ServiceDataDTO addServiceData(@RequestBody ServiceDataDTO dto) {
    	dto = serviceDataService.addServiceData(dto);
    	log.info("Response send: {}",dto);
    	return dto;
    }
    
    @PostMapping("/update")
    public ServiceDataDTO updateServiceData(@RequestBody ServiceDataDTO dto) {
    	dto = serviceDataService.updateServiceData(dto);
    	log.info("Response send: {}",dto);
    	return dto;
    }
    
    @PostMapping("/getdetails")
    public ResponseEntity<ServiceResponse> getServiceDetails(@RequestBody ServiceDataDTO data) {
			ResponseEntity<ServiceResponse> response = serviceDataService.getServiceDetails(data);
    		log.info("Response send: {}",response);
    		return	response;
    }
    
    @PostMapping("/remove")
    public void removeService(@RequestBody ServiceDataDTO dto) {
    	serviceDataService.removeService(dto);
		log.info("service remove: {}",dto);
    }
    
    @PostMapping("/sendEmail")
    public void sendEmail(@RequestBody List<ServiceDataDTO> dtoList) {
		serviceDataService.sendEmail(dtoList);
		log.info("email sent: {}",dtoList);
    }

}

