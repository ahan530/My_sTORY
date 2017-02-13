package qf.com.my_story.bean;

import java.util.List;

/**
 * Created by linke on 2017/2/3 0003.
 * email:linke0530@163.com.
 */

public class GetStory {
    private String uid;//发表人
    private String sid;//故事id
    private String portrait;//头像
    private String pics;//图片
    private String username;//用户名
    private long story_time;//时间
    private String city;//地址
    private String readcount;//阅读量
    private String comment;//评论
    private String story_info;//主题
    private List<String> data_pics;//图片集合

    public List<String> getData_pics() {
        return data_pics;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setData_pics(List<String> data_pics) {
        this.data_pics = data_pics;
    }

    public GetStory(String uid, String sid, String portrait, String pics, String username, long story_time, String city, String readcount, String comment, String story_info, List<String> data_pics) {
        this.uid = uid;
        this.sid = sid;
        this.portrait = portrait;
        this.pics = pics;
        this.username = username;
        this.story_time = story_time;
        this.city = city;
        this.readcount = readcount;
        this.comment = comment;
        this.story_info = story_info;
        this.data_pics = data_pics;
    }

    public GetStory(){

    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public long getStory_time() {
        return story_time;
    }

    public void setStory_time(long story_time) {
        this.story_time = story_time;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getReadcount() {
        return readcount;
    }

    public void setReadcount(String readcount) {
        this.readcount = readcount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStory_info() {
        return story_info;
    }

    public void setStory_info(String story_info) {
        this.story_info = story_info;
    }



}
