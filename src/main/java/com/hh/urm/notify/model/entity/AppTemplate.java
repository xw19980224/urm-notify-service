package com.hh.urm.notify.model.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

/**
 * @ClassName: AppTemplate
 * @Author: MaxWell
 * @Description:
 * @Date: 2022/11/16 16:41
 * @Version: 1.0
 */
@Entity
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
@Table(name = "t_app_template", schema = "notify", catalog = "notify")
public class AppTemplate {
    private String code;
    private Integer businessType;
    private String pushTypes;
    private Integer jPushType;
    private String imageUrl;
    private String content;
    private String title;
    private String linkUrl;
    private Integer fullType;
    private String businessExt;
    private String popWindowExt;

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(name = "code", nullable = false, length = 255)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
    @Column(name = "push_types", nullable = false, length = 255)
    public String getPushTypes() {
        return pushTypes;
    }

    public void setPushTypes(String pushTypes) {
        this.pushTypes = pushTypes;
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
    @Column(name = "image_url", nullable = true, length = 255)
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Basic
    @Column(name = "notify_content", nullable = true, length = -1)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
    @Column(name = "link_url", nullable = true, length = 255)
    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
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
    @Column(name = "business_ext", nullable = true, length = -1)
    public String getBusinessExt() {
        return businessExt;
    }

    public void setBusinessExt(String businessExt) {
        this.businessExt = businessExt;
    }

    @Basic
    @Column(name = "pop_window_ext", nullable = true, length = -1)
    public String getPopWindowExt() {
        return popWindowExt;
    }

    public void setPopWindowExt(String popWindowExt) {
        this.popWindowExt = popWindowExt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AppTemplate that = (AppTemplate) o;
        return Objects.equals(code, that.code) && Objects.equals(businessType, that.businessType) && Objects.equals(pushTypes, that.pushTypes) && Objects.equals(jPushType, that.jPushType) && Objects.equals(imageUrl, that.imageUrl) && Objects.equals(content, that.content) && Objects.equals(title, that.title) && Objects.equals(linkUrl, that.linkUrl) && Objects.equals(fullType, that.fullType) && Objects.equals(businessExt, that.businessExt) && Objects.equals(popWindowExt, that.popWindowExt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, businessType, pushTypes, jPushType, imageUrl, content, title, linkUrl, fullType, businessExt, popWindowExt);
    }
}
