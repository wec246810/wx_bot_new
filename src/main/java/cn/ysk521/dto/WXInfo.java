package cn.ysk521.dto;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Y.S.K on 2017/10/26 in wx_bot_new.
 */
@Component("wxInfo")
@Scope("prototype")
public class WXInfo {
    private String qrcodeurl;
    private String uuid;
    private String myUsername;
    private List myFriends = new ArrayList<myFriend>();
    public List groupList = new ArrayList<String>();


    public String getQrcodeurl() {
        return qrcodeurl;
    }

    public void setQrcodeurl(String qrcodeurl) {
        this.qrcodeurl = qrcodeurl;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMyUsername() {
        return myUsername;
    }

    public void setMyUsername(String myUsername) {
        this.myUsername = myUsername;
    }

    public List getMyFriends() {
        return myFriends;
    }

    public void setMyFriends(List myFriends) {
        this.myFriends = myFriends;
    }

    public List getGroupList() {
        return groupList;
    }

    public void setGroupList(List groupList) {
        this.groupList = groupList;
    }

    @Override
    public String toString() {
        return "WXInfo{" +
                "qrcodeurl='" + qrcodeurl + '\'' +
                ", uuid='" + uuid + '\'' +
                ", myUsername='" + myUsername + '\'' +
                ", myFriends=" + myFriends +
                ", groupList=" + groupList +
                '}';
    }
}
