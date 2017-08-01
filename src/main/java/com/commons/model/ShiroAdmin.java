package com.commons.model;

import java.io.Serializable;
import java.util.Set;

/**
 * @author 钟启辉
 * @company www.jiweitech.com
 * @date 2017/3/1 13:45
 * @description 封装这admin的角色信息和可访问的资源信息
 */
public class ShiroAdmin implements Serializable {

    private String adminSn; //admin的账号

    private String adminName; //admin的名字

    private Set<String> urls; //拥有权限的集合

    private Set<String> roles; //用户角色的集合

    public ShiroAdmin() {
    }

    public ShiroAdmin(String adminSn, String adminName,Set<String> urls, Set<String> roles) {
        this.adminSn = adminSn;
        this.adminName = adminName;
        this.urls = urls;
        this.roles = roles;
    }

    public String getAdminSn() {

        return adminSn;
    }

    public void setAdminId(String adminSn) {
        this.adminSn = adminSn;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public Set<String> getUrls() {
        return urls;
    }

    public void setUrls(Set<String> urls) {
        this.urls = urls;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
