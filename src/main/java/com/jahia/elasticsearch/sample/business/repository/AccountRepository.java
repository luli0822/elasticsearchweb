package com.jahia.elasticsearch.sample.business.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jahia.elasticsearch.sample.business.model.AccountEntity;

@Repository
@Transactional
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
	@Modifying
	@Query("Update AccountEntity a set a.name = ?1 where a.docSysId = ?2")
	void update(String name, String docSysId);
}
