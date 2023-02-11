package com.roche.connect.forte.adapter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.roche.connect.forte.adapter.model.ForteJob;

public interface ForteJobWriteRepository extends JpaRepository<ForteJob, String>, JpaSpecificationExecutor<ForteJob>  {

	public ForteJob findById(String jobId);
	public List<ForteJob> findByDeviceRunIdAndJobStatus(String deviceRunId,String jobStatus);
}
