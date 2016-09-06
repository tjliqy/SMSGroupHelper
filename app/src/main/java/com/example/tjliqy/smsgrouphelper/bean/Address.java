package com.example.tjliqy.smsgrouphelper.bean;

/**
 * Created by tjliqy on 2016/9/5.
 */
public class Address {

    /**
     * id : 1
     * user_id : 0
     * detail : 【天外天工作室】同学你好，欢迎你报名产品组。由于报名人数较多，我们将于本周日18时在45楼B201举行笔试，请【提前十分钟】到场。考试为闭卷，以主观题为主，主要考察对天外天产品的理解，期待你的精彩表现。如时间冲突请提前联系。收到请回复，谢谢~
     * realname : null
     * phone : null
     */

    private String id;
    private String user_id;
    private String detail;
    private String realname;
    private String phone;
    private boolean isSend;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }

}
