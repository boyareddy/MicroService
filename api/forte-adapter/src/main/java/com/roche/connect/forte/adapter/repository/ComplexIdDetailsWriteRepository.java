package com.roche.connect.forte.adapter.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.roche.connect.forte.adapter.model.ComplexIdDetails;

public interface ComplexIdDetailsWriteRepository  extends JpaRepository<ComplexIdDetails, String>, JpaSpecificationExecutor<ComplexIdDetails> {

	
	public ComplexIdDetails findById(UUID Id);
	public ComplexIdDetails findByDeviceRunID(String runID);
	public ComplexIdDetails findByComplexId(String complexId);
	
}
