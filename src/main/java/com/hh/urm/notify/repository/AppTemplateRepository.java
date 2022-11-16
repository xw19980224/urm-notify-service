package com.hh.urm.notify.repository;

import com.hh.urm.notify.model.entity.AppTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @ClassName: AppTemplate
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/11/16 16:34
 * @Version: 1.0
 */
public interface AppTemplateRepository extends JpaRepository<AppTemplate, String>, JpaSpecificationExecutor<AppTemplate> {
}