package com.hh.urm.notify.repository;

import com.hh.urm.notify.model.entity.SmsMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @ClassName: SmsMetadata
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/10/28 17:15
 * @Version: 1.0
 */
public interface SmsMetadataRepository extends JpaRepository<SmsMetadata, Long>, JpaSpecificationExecutor<SmsMetadata> {
}