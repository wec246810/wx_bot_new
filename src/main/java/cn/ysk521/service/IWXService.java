package cn.ysk521.service;

import cn.ysk521.dto.myFriend;

import java.util.List;
import java.util.Map;

/**
 * Created by Y.S.K on 2017/10/24 in wx_bot_new.
 */
public interface IWXService {
    //微信登陆前
    String getQrCode();

    //用户扫描二维码后,获得初始化需要的数据存入map
    Map BeforeWXInit();

    //微信初始化操作
    void wxInit();

    //解析获得的数据
    List<myFriend> getMyFriendList();

    //发送信息
    void sendMessage(String content, List type);

    List getMyFriends(int sex);

}
