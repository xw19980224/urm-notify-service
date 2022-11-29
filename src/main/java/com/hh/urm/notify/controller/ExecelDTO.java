package com.hh.urm.notify.controller;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: Demo
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/11/24 18:21
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ExcelTarget("ExecelDTO")
public class ExecelDTO implements Serializable {

    @Excel(name = "主邀请人")
    private String mainReferrerSuperId;
    @Excel(name = "被邀请人")
    private String superId;
    @Excel(name = "时间")
    private String time;
}
