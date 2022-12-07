package com.hh.urm.notify.model.limit.entity;

import io.swagger.models.auth.In;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @ClassName: BlackWhiteList
 * @Author: MaxWell
 * @Description: 黑白名单
 * @Date: 2022/12/7 23:07
 * @Version: 1.0
 */
@Entity
@Table(name = "t_black_white_list", schema = "notify")
public class BlackWhiteList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, length = 25)
    private Long id;

    @Column(name = "code" ,nullable = true,length = 25)
    private String code;

    @Column(name = "type" ,nullable = true,length = 1)
    private Integer type;

    @Column(name = "roll" ,nullable = true,length = 25)
    private String roll;

    @Column(name = "list_type" ,nullable = true,length = 1)
    private Integer listType;

    @Column(name = "_create_by", nullable = false, length = 25)
    private String createBy;

    @Column(name = "_create_time", nullable = false)
    private Timestamp createTime;

    @Column(name = "_update_by", nullable = false, length = 25)
    private String updateBy;

    @Column(name = "_update_time", nullable = false)
    private Timestamp updateTime;
}
