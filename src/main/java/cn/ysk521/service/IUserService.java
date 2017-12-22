package cn.ysk521.service;

import cn.ysk521.dto.EUDataGridResult;
import cn.ysk521.entity.SendRecord;
import cn.ysk521.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Y.S.K on 2017/10/24 in wx_bot_new.
 */

/**
 * 用户操作-
 * 登录
 * 查询发送记录
 * 修改密码
 *
 */
@Component("iUserService")
public interface IUserService {
    User dologin(String name, String password);
    void updateMyPassword(User user);
//    Long  getMyTime(String username);
    List<SendRecord> getMySendRecord(String username);
    String  useCDK(String username,String cdkvalue);
    void setSendRecord(String username,String content);
    EUDataGridResult getSendRecord(int pageNum, int pageSize, String username);
    User queryUserByName(String username);
}
