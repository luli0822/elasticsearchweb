package com.jahia.elasticsearch.sample.business.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jahia.elasticsearch.sample.business.model.AccountEntity;

@Repository
@Transactional
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
}
