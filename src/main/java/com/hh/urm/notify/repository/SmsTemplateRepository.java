package com.hh.urm.notify.repository;

import com.hh.urm.notify.model.template.entity.SmsTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * @ClassName: SmsTemplate
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/10/28 17:15
 * @Version: 1.0
 */
public interface SmsTemplateRepository extends JpaRepository<SmsTemplate, String>, JpaSpecificationExecutor<SmsTemplate> {

    Optional<SmsTemplate> findOneByCode(String id);
}