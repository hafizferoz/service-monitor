package com.monitor.service.repository;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.monitor.service.model.ServiceData;

@Repository
@Transactional
public interface ServiceRepository extends CrudRepository<ServiceData, Integer> {
}

