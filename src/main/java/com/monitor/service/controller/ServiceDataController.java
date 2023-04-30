package com.monitor.service.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.monitor.service.model.ServiceData;
import com.monitor.service.repository.ServiceRepository;

@RestController
@RequestMapping("/service-data")
public class ServiceDataController {
	
    @Autowired
    private ServiceRepository serviceRepository;

    @GetMapping
    public List<ServiceData> getServiceData() {
//    	return serviceRepository.findAll();
       Iterable<ServiceData> serviceDataList = serviceRepository.findAll();
        return StreamSupport.stream(serviceDataList.spliterator(), false)
         .collect(Collectors.toList());
    }

    @PostMapping
    public ServiceData addServiceData(@RequestBody ServiceData data) {
    	return serviceRepository.save(data);
    }
}

