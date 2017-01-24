package com.test.bean;

import java.util.Date;

public class Comment {
    private Integer id;

    private Integer blogId;

    private String username;

    private String email;

    private Date createTime;

    private Integer agreeWithTimes;

    private String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBlogId() {
        return blogId;
    }

    public void setBlogId(Integer blogId) {
        this.blogId = blogId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getAgreeWithTimes() {
        return agreeWithTimes;
    }

    public void setAgreeWithTimes(Integer agreeWithTimes) {
        this.agreeWithTimes = agreeWithTimes;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}