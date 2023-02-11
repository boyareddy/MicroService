package com.roche.connect.htp.adapter.writerepository;

import org.springframework.stereotype.Repository;

import com.hcl.hmtp.common.server.repositories.SimpleWriteRepository;
import com.roche.connect.htp.adapter.model.Cycle;
@Repository
public interface CycleWriteRepository extends SimpleWriteRepository<Cycle>{

}
