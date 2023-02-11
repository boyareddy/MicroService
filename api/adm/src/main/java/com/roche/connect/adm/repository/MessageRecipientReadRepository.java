package com.roche.connect.adm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hcl.hmtp.common.server.repositories.SimpleReadRepository;
import com.roche.connect.adm.model.MessageRecipient;

public interface MessageRecipientReadRepository extends SimpleReadRepository<MessageRecipient> {

	@Query("SELECT mr FROM MessageRecipient mr WHERE mr.messageTemplate.id = :templateId")
	public List<MessageRecipient> findByTemplateId(@Param("templateId") long templateId);

}
