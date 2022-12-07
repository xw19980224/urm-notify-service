package com.hh.urm.notify.repository;

import com.hh.urm.notify.model.template.entity.AppTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * @ClassName: AppTemplate
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/11/16 16:34
 * @Version: 1.0
 */
public interface AppTemplateRepository extends JpaRepository<AppTemplate, String>, JpaSpecificationExecutor<AppTemplate> {

    /**
     * 根据code编码获取模板信息
     *
     * @param code 编码
     * @return 模板信息
     */
    Optional<AppTemplate> findOneByCode(String code);

}