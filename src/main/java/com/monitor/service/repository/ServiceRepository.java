package com.monitor.service.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monitor.service.model.ServiceData;

@Repository
@Transactional
public interface ServiceRepository extends JpaRepository<ServiceData, Integer> {

	Optional<ServiceData> findByUrl(String url);
}

