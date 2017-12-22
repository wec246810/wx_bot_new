package cn.ysk521.dao;


import cn.ysk521.entity.User;

/**
 * Created by Y.S.K on 2017/8/1 in wx_bot.
 */
public interface UserDao {
    void updateUserPassWord(User user);
    void addUser(User user);
    User queryUserByName(String userName);
    void updateUserExpireTime(User user);
}
