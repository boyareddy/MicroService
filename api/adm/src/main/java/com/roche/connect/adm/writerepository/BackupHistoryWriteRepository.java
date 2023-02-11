package com.roche.connect.adm.writerepository;

import org.springframework.stereotype.Repository;

import com.hcl.hmtp.common.server.repositories.SimpleReadRepository;
import com.roche.connect.adm.model.BackupHistory;

@Repository
public interface BackupHistoryWriteRepository extends SimpleReadRepository<BackupHistory> {

}
