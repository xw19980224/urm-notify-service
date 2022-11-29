package com.hh.urm.notify.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.alibaba.fastjson.JSONObject;
import com.hh.urm.notify.model.entity.AppTemplate;
import com.hh.urm.notify.model.entity.SmsTemplate;
import com.hh.urm.notify.repository.AppTemplateRepository;
import com.hh.urm.notify.repository.SmsTemplateRepository;
import com.hh.urm.notify.utils.TimeUtil;
import com.hh.urm.notify.utils.base.ServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName: DemoController
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/11/4 10:24
 * @Version: 1.0
 */
@Slf4j
@RestController
public class DemoController {

    @Autowired
    private AppTemplateRepository appTemplateRepository;

    @Resource
    private SmsTemplateRepository smsTemplateRepository;

    @Resource
    private MongoTemplate mongoTemplate;

    @PostMapping("/demo")
    public String demo(@RequestBody JSONObject jsonObject) {
        SmsTemplate smsTemplate = new SmsTemplate();
        Timestamp nowTime = TimeUtil.nowTimestamp();
        smsTemplate.setName("短信测试");
        smsTemplate.setCreateTime(nowTime);
        smsTemplate.setUpdateTime(nowTime);
        smsTemplate.setCreateBy("xw");
        smsTemplate.setSender("8820100935957");
        smsTemplate.setAppKey("9e4VrLQd7BX3VZhq6x1g951q4aCr");
        smsTemplate.setAppSecret("r83k2Q9AQD31FrNO904okwTJ1ZBJ");
        smsTemplate.setTemplateId("4e34d053e21f448c933a069a636a4226");
        smsTemplate.setTemplateContent("您的好友 ${1}（尾号${2}）帮您预约了高合汽车试驾，后续将会有出行顾问与您确认时间及行程，感谢对高合汽车的支持！");
        smsTemplateRepository.save(smsTemplate);
        return "ok";
    }

    @PostMapping("/saveApp")
    public ServiceResponse<Object> saveApp() {
        AppTemplate appTemplate = new AppTemplate();
        appTemplate.setBusinessType(10000005);
        appTemplate.setTitle("您有一笔即将过期积分");
        appTemplate.setNotifyContent("【高合HiPhi】尊敬的用户，您的高合HiPhiApp的{points}贝还有{day}天即将到期，请尽快点击URLq前往高合之选商城进行兑换使用");
        appTemplate.setjPushType(2);
        appTemplate.setPushTypes("0,1");
        appTemplate.setLinkUrl("https://eeh5.hiphi.com/appv2/#/task");
        AppTemplate save = appTemplateRepository.save(appTemplate);
        return ServiceResponse.createSuccessResponse("", save);
    }

    @GetMapping("/execel")
    public void test() throws IOException {
        Query query = new Query();
        query.addCriteria(Criteria.where("main_referrer_super_id").exists(true));
        query.fields().exclude("_id").include("superId").include("main_referrer_super_id").include("invite_activity_time");
        query.addCriteria(Criteria.where("superId").is("PA780150822552997888"));
        List<Document> basicDBObjects = mongoTemplate.find(query, Document.class, "t_user_profile");

        List<String> superIds = basicDBObjects.stream().map(item -> item.getString("superId")).collect(Collectors.toList());

        Query q = new Query();
        q.addCriteria(Criteria.where("retention_time").exists(true));
        q.addCriteria(Criteria.where("superId").in(superIds));
        q.fields().exclude("_id").include("superId").include("retention_time");
        List<Document> tActionHistory = mongoTemplate.find(q, Document.class, "t_action_history");

        List<Document> actionHistory = tActionHistory.stream().filter(item -> {
            String superId = item.getString("superId");
            return superIds.contains(superId);
        }).collect(Collectors.toList());

        List<ExecelDTO> list = basicDBObjects.stream().map(item -> {
            String superId = item.getString("superId");
            String mainReferrerSuperId = item.getString("main_referrer_super_id");
            String inviteActivityTime = item.getString("invite_activity_time").trim();
            inviteActivityTime = inviteActivityTime.replace(" ", " ");
            inviteActivityTime = inviteActivityTime.replace("/", "-");
            Date date = TimeUtil.parseDate(inviteActivityTime, TimeUtil.YYYY_MM_DD_HH_MM_SS);

            List<Document> collect = actionHistory.stream().filter(temp -> {
                String superId1 = temp.getString("superId");
                return superId1.equals(superId);
            }).filter(temp -> {
                String retention_time = temp.getString("retention_time").trim();
                retention_time = retention_time.replace(" ", "");
                retention_time = retention_time.replace("/", "-");
                Date retentionTime = TimeUtil.parseDate(retention_time, TimeUtil.YYYY_MM_DD_HH_MM_SS);
                return retentionTime.after(date);
            }).collect(Collectors.toList());
            if (collect.isEmpty()) {
                return null;
            }
            collect.sort((o1, o2) -> {
                String retentionTimeStr1 = o1.getString("retention_time").trim();
                retentionTimeStr1 = retentionTimeStr1.replace(" ", "");
                retentionTimeStr1 = retentionTimeStr1.replace("/", "-");
                Date retentionTime1 = TimeUtil.parseDate(retentionTimeStr1, TimeUtil.YYYY_MM_DD_HH_MM_SS);

                String retentionTimeStr2 = o2.getString("retention_time").trim();
                retentionTimeStr2 = retentionTimeStr2.replace(" ", "");
                retentionTimeStr2 = retentionTimeStr2.replace("/", "-");
                Date retentionTime2 = TimeUtil.parseDate(retentionTimeStr2, TimeUtil.YYYY_MM_DD_HH_MM_SS);
                boolean before = retentionTime1.before(retentionTime2);
                if (before) {
                    return -1;
                }
                return 1;
            });
            Document document = collect.get(0);
            String timeStr = document.getString("retention_time");
            Date time = TimeUtil.parseDate(timeStr, TimeUtil.YYYY_MM_DD_HH_MM_SS);
            Date start = TimeUtil.parseDate("2022-07-04 00:00:00", TimeUtil.YYYY_MM_DD_HH_MM_SS);
            Date end = TimeUtil.parseDate("2022-11-06 23:59:59", TimeUtil.YYYY_MM_DD_HH_MM_SS);
            if (TimeUtil.belongCalendar(time, start, end)) {
                ExecelDTO execelDTO = new ExecelDTO();
                execelDTO.setTime(timeStr);
                execelDTO.setMainReferrerSuperId(mainReferrerSuperId);
                execelDTO.setSuperId(superId);
                return execelDTO;
            }
            return null;
        }).filter(item -> !Objects.isNull(item)).collect(Collectors.toList());

        export(list);
        System.out.println("-----------------------------------------------------end");
    }

    @GetMapping("/execel1")
    public void test1() throws IOException {
        Query query = new Query();
        query.addCriteria(Criteria.where("main_referrer_super_id").exists(true));
        query.fields().exclude("_id").include("superId").include("main_referrer_super_id").include("invite_activity_time");
        List<Document> basicDBObjects = mongoTemplate.find(query, Document.class, "t_user_profile");

        List<String> superIds = basicDBObjects.stream().map(item -> item.getString("superId")).collect(Collectors.toList());

        Query q = new Query();
        q.addCriteria(Criteria.where("td_state_time").exists(true));
        q.addCriteria(Criteria.where("superId").in(superIds));
        q.addCriteria(Criteria.where("td_state").is("2"));
        q.fields().exclude("_id").include("superId").include("td_state_time");
        List<Document> tActionHistory = mongoTemplate.find(q, Document.class, "t_action_history");

        List<Document> actionHistory = tActionHistory.stream().filter(item -> {
            String superId = item.getString("superId");
            return superIds.contains(superId);
        }).collect(Collectors.toList());

        List<ExecelDTO> list = basicDBObjects.stream().map(item -> {
            String superId = item.getString("superId");
            String mainReferrerSuperId = item.getString("main_referrer_super_id");
            String inviteActivityTime = item.getString("invite_activity_time").trim();
            inviteActivityTime = inviteActivityTime.replace(" ", " ");
            inviteActivityTime = inviteActivityTime.replace("/", "-");
            Date date = TimeUtil.parseDate(inviteActivityTime, TimeUtil.YYYY_MM_DD_HH_MM_SS);

            List<Document> collect = actionHistory.stream().filter(temp -> {
                String superId1 = temp.getString("superId");
                return superId1.equals(superId);
            }).filter(temp -> {
                String retention_time = temp.getString("td_state_time").trim();
                retention_time = retention_time.replace(" ", "");
                retention_time = retention_time.replace("/", "-");
                Date retentionTime = TimeUtil.parseDate(retention_time, TimeUtil.YYYY_MM_DD_HH_MM_SS);
                return retentionTime.after(date);
            }).collect(Collectors.toList());
            if (collect.isEmpty()) {
                return null;
            }
            collect.sort((o1, o2) -> {
                String retentionTimeStr1 = o1.getString("td_state_time").trim();
                retentionTimeStr1 = retentionTimeStr1.replace(" ", "");
                retentionTimeStr1 = retentionTimeStr1.replace("/", "-");
                Date retentionTime1 = TimeUtil.parseDate(retentionTimeStr1, TimeUtil.YYYY_MM_DD_HH_MM_SS);

                String retentionTimeStr2 = o2.getString("td_state_time").trim();
                retentionTimeStr2 = retentionTimeStr2.replace(" ", "");
                retentionTimeStr2 = retentionTimeStr2.replace("/", "-");
                Date retentionTime2 = TimeUtil.parseDate(retentionTimeStr2, TimeUtil.YYYY_MM_DD_HH_MM_SS);
                boolean before = retentionTime1.before(retentionTime2);
                if (before) {
                    return -1;
                }
                return 1;
            });
            Document document = collect.get(0);
            String timeStr = document.getString("td_state_time");
            Date time = TimeUtil.parseDate(timeStr, TimeUtil.YYYY_MM_DD_HH_MM_SS);
            Date start = TimeUtil.parseDate("2022-07-04 00:00:00", TimeUtil.YYYY_MM_DD_HH_MM_SS);
            Date end = TimeUtil.parseDate("2022-11-06 23:59:59", TimeUtil.YYYY_MM_DD_HH_MM_SS);
            if (TimeUtil.belongCalendar(time, start, end)) {
                ExecelDTO execelDTO = new ExecelDTO();
                execelDTO.setTime(timeStr);
                execelDTO.setMainReferrerSuperId(mainReferrerSuperId);
                execelDTO.setSuperId(superId);
                return execelDTO;
            }
            return null;
        }).filter(item -> !Objects.isNull(item)).collect(Collectors.toList());

        export(list);
        System.out.println("-----------------------------------------------------end");
    }

    private void export(List<ExecelDTO> list) throws IOException {
        Workbook sheets = ExcelExportUtil.exportExcel(new ExportParams("邀请关系后首次留资", "sheet"),
                ExecelDTO.class, list);
        FileOutputStream stream = new FileOutputStream("/aa111.xls");
        sheets.write(stream);
        stream.close();
        sheets.close();
    }
}
