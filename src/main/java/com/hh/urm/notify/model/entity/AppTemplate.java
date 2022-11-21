package com.hh.urm.notify.model.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

/**
 * @ClassName: AppTemplate
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/11/20 13:59
 * @Version: 1.0
 */
@Entity
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
@Table(name = "t_app_template", schema = "notify", catalog = "notify")
public class AppTemplate {
    private String code;
    private String businessExt;
    private Integer businessType;
    private String notifyContent;
    private Integer fullType;
    private String imageUrl;
    private Integer jPushType;
    private String linkUrl;
    private String popWindowExt;
    private String pushTypes;
    private String title;
    private String name;

    @Id
    @Column(name = "code", nullable = false, length = 255)
    @GeneratedValue(generator = "jpa-uuid")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Basic
    @Column(name = "business_ext", nullable = true, length = -1)
    public String getBusinessExt() {
        return businessExt;
    }

    public void setBusinessExt(String businessExt) {
        this.businessExt = businessExt;
    }

    @Basic
    @Column(name = "business_type", nullable = false)
    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    @Basic
    @Column(name = "notify_content", nullable = true, length = -1)
    public String getNotifyContent() {
        return notifyContent;
    }

    public void setNotifyContent(String notifyContent) {
        this.notifyContent = notifyContent;
    }

    @Basic
    @Column(name = "full_type", nullable = true)
    public Integer getFullType() {
        return fullType;
    }

    public void setFullType(Integer fullType) {
        this.fullType = fullType;
    }

    @Basic
    @Column(name = "image_url", nullable = true, length = 255)
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Basic
    @Column(name = "j_push_type", nullable = true)
    public Integer getjPushType() {
        return jPushType;
    }

    public void setjPushType(Integer jPushType) {
        this.jPushType = jPushType;
    }

    @Basic
    @Column(name = "link_url", nullable = true, length = 255)
    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    @Basic
    @Column(name = "pop_window_ext", nullable = true, length = -1)
    public String getPopWindowExt() {
        return popWindowExt;
    }

    public void setPopWindowExt(String popWindowExt) {
        this.popWindowExt = popWindowExt;
    }

    @Basic
    @Column(name = "push_types", nullable = false, length = 255)
    public String getPushTypes() {
        return pushTypes;
    }

    public void setPushTypes(String pushTypes) {
        this.pushTypes = pushTypes;
    }

    @Basic
    @Column(name = "title", nullable = false, length = 255)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppTemplate that = (AppTemplate) o;
        return Objects.equals(code, that.code) && Objects.equals(businessExt, that.businessExt) && Objects.equals(businessType, that.businessType) && Objects.equals(notifyContent, that.notifyContent) && Objects.equals(fullType, that.fullType) && Objects.equals(imageUrl, that.imageUrl) && Objects.equals(jPushType, that.jPushType) && Objects.equals(linkUrl, that.linkUrl) && Objects.equals(popWindowExt, that.popWindowExt) && Objects.equals(pushTypes, that.pushTypes) && Objects.equals(title, that.title) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, businessExt, businessType, notifyContent, fullType, imageUrl, jPushType, linkUrl, popWindowExt, pushTypes, title, name);
    }
}
