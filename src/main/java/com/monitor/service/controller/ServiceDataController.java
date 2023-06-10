package com.monitor.service.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.monitor.service.dto.ServiceDataDTO;
import com.monitor.service.dto.ServiceResponse;
import com.monitor.service.model.ServiceData;
import com.monitor.service.repository.ServiceRepository;

@RestController
@RequestMapping("/service-data")
public class ServiceDataController {
	
    @Autowired
    private ServiceRepository serviceRepository;
    
    @Autowired
    private ModelMapper mapper;
    
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/load")
    public List<ServiceDataDTO> getServiceData() {
    	List<ServiceData> serviceDataList = serviceRepository.findAll();
        TypeToken<List<ServiceDataDTO>> serviceDataDTOTypeToken = new TypeToken<List<ServiceDataDTO>>() {};
		List<ServiceDataDTO> serviceListDTO = mapper.map(serviceDataList, serviceDataDTOTypeToken.getType());
		return serviceListDTO;
    }

    @PostMapping("/update")
    public ServiceDataDTO addServiceData(@RequestBody ServiceDataDTO dto) {
    	ServiceData data = new ServiceData();
		mapper.map(dto, data);
    	data = serviceRepository.save(data);
    	mapper.map(data, dto);
    	return dto;
    }
    
    @PostMapping("/getdetails")
    public ResponseEntity<ServiceResponse> getServiceDetails(@RequestBody ServiceDataDTO data) {
    		return	restTemplate.getForEntity(data.getUrl(), ServiceResponse.class);
    	
    }

}

