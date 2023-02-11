package com.roche.connect.adm.repository;

import com.hcl.hmtp.common.server.entity.Company;
import com.hcl.hmtp.common.server.repositories.SimpleReadRepository;
public interface CompanyReadRepository extends SimpleReadRepository<Company>{
	
	public Company findByDomainName(String domainName);

}
