package cn.ysk521.entity;


import java.sql.Timestamp;

/**
 * Created by Y.S.K on 2017/8/2 in wx_bot.
 */
public class SendRecord {
    private  Integer id;
    private String content;
    private String userName;//谁发送的
    private Timestamp createTime;
    //暂时不用，扫描二维码人数的统计

    private int userNum;

    private User user;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public int getUserNum() {
        return userNum;
    }

    public void setUserNum(int userNum) {
        this.userNum = userNum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "SendRecord{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", userName='" + userName + '\'' +
                ", createTime=" + createTime +
                ", userNum=" + userNum +
                ", user=" + user +
                '}';
    }
}
