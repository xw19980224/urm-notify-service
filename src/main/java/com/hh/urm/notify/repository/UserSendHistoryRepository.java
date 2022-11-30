package com.hh.urm.notify.repository;

import com.hh.urm.notify.model.entity.UserSendHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @ClassName: UserSendHistory
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/11/30 11:01
 * @Version: 1.0
 */
public interface UserSendHistoryRepository extends JpaRepository<UserSendHistory, String>, JpaSpecificationExecutor<UserSendHistory> {
}