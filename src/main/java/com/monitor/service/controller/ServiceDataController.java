package com.monitor.service.controller;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.monitor.service.dto.ServiceDataDTO;
import com.monitor.service.dto.ServiceResponse;
import com.monitor.service.model.ServiceData;
import com.monitor.service.repository.ServiceRepository;
import com.monitor.service.service.EmailService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/servicedata")
@Slf4j
public class ServiceDataController {
	
    @Autowired
    private ServiceRepository serviceRepository;
    
    @Autowired
    private ModelMapper mapper;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private EmailService emailService;

    @GetMapping("/load")
    public List<ServiceDataDTO> getServiceData() {
    	List<ServiceData> serviceDataList = serviceRepository.findAll();
        TypeToken<List<ServiceDataDTO>> serviceDataDTOTypeToken = new TypeToken<List<ServiceDataDTO>>() {};
		List<ServiceDataDTO> serviceListDTO = mapper.map(serviceDataList, serviceDataDTOTypeToken.getType());
		log.info("load: {}",serviceListDTO);
		return serviceListDTO;
    }

    @PostMapping("/add")
    public ServiceDataDTO addServiceData(@RequestBody ServiceDataDTO dto) {
    	Optional<ServiceData> dataOpt = serviceRepository.findByUrl(dto.getUrl());
    	if(dataOpt.isPresent()) {
    		throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Service already exists!");
    	}
    	ServiceData data = new ServiceData();
		mapper.map(dto, data);
    	data = serviceRepository.save(data);
    	mapper.map(data, dto);
    	log.info("update: {}",dto);
    	return dto;
    }
    
    @PostMapping("/update")
    public ServiceDataDTO updateServiceData(@RequestBody ServiceDataDTO dto) {
    	Optional<ServiceData> dataOpt = serviceRepository.findById(dto.getId());
    	if(!dataOpt.isPresent()) {
    		throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Service doesn't exist!");
    	}
    	ServiceData data =dataOpt.get();
		mapper.map(dto, data);
    	data = serviceRepository.save(data);
    	mapper.map(data, dto);
    	log.info("update: {}",dto);
    	return dto;
    }
    
    @PostMapping("/getdetails")
    public ResponseEntity<ServiceResponse> getServiceDetails(@RequestBody ServiceDataDTO data) {
    	try {
			ResponseEntity<ServiceResponse> response = restTemplate.getForEntity(data.getUrl(), ServiceResponse.class);
    		log.info("getdetails: {}",response);
    		return	response;
    	}catch (Exception e) {
    		log.info("Eror: {}",e.getMessage());
    		ServiceResponse response =new ServiceResponse();
    		response.setStatus("DOWN");
    		log.info("getdetails: {}",response);
			return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
		}
    	
    }
    
    @PostMapping("/remove")
    public void removeService(@RequestBody ServiceDataDTO dto) {
		serviceRepository.deleteById(dto.getId());
		log.info("remove: {}",dto);
    }
    
    @PostMapping("/sendEmail")
    public void sendEmail(@RequestBody List<ServiceDataDTO> dtoList) {
		log.info("sendEmail: {}",dtoList);
		emailService.sendEmail(dtoList);
    }

}

