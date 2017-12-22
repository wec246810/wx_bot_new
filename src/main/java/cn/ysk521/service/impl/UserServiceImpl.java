package cn.ysk521.service.impl;

import cn.ysk521.dao.CDKDao;
import cn.ysk521.dao.SendRecordDao;
import cn.ysk521.dao.UserDao;
import cn.ysk521.dto.EUDataGridResult;
import cn.ysk521.entity.CDK;
import cn.ysk521.entity.SendRecord;
import cn.ysk521.entity.User;
import cn.ysk521.service.IUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by Y.S.K on 2017/10/24 in wx_bot_new.
 */
@Component("userServiceImpl")
@Scope("prototype")
public class UserServiceImpl implements IUserService{
    @Autowired
    UserDao userDao;
    @Autowired
    CDKDao cdkDao;
    @Autowired
    SendRecordDao sendRecordDao;

    @Override
    public User dologin(String name, String password) {
     User user=  userDao.queryUserByName(name);
     if (password.equals(user.getPassword())){
         return user;
     }else {
         return null;
     }
    }

    @Override
    public User queryUserByName(String username) {

        User user=userDao.queryUserByName(username);
        return user;
    }

    @Override
    public void updateMyPassword(User user) {
        userDao.updateUserPassWord(user);
    }

    @Override
    public List<SendRecord> getMySendRecord(String username) {
        return null;
    }

    @Override
    public String useCDK(String username,String cdkvalue) {
        //读取数据   cdk的延长时间，cdk的使用时间，
        //将数据更新到数据库 cdk的使用时间，使用者，使用者增加时间
        CDK cdk=cdkDao.queryCDKByValue(cdkvalue);
        User user=userDao.queryUserByName(username);
        Timestamp extimestamp;
        //cdk存在且未被使用
        if(cdk!=null&&cdk.getUseTime()==null){
            int delayDay=cdk.getDelayDay();
            Long delaytime=delayDay*60*60*24*1000L;
            if(user.getExpirationTime().getTime()<new Date().getTime()){
                //如果过期，则为现在时间加上延长时间
                System.out.println("如果过期，则为现在时间加上延长时间");
                extimestamp=new Timestamp(new Date().getTime()+delaytime);
            }else {
                //如果没过期，则为到期时间加上延长时间
                extimestamp=new Timestamp(user.getExpirationTime().getTime()+delaytime);
            }
            //更新cdk的使用时间，cdk的使用者，使用者的增加时间
            cdk.setUseTime(new Timestamp(new Date().getTime()));
            cdk.setWhoUse(username);
            cdkDao.upadteCDK(cdk);
            user.setExpirationTime(extimestamp);
            userDao.updateUserExpireTime(user);
            return "0";
        }else if(cdk!=null&&cdk.getUseTime()!=null){
            return "1";
        }else{
            return "2";
        }

    }

    @Override
    public void setSendRecord(String username, String content) {
        //用户名，内容，创建时间,
        System.out.println("开始存储发送记录");
        SendRecord sendRecord=new SendRecord();
        sendRecord.setUserName(username);
        sendRecord.setContent(content);
        sendRecord.setCreateTime(new Timestamp(new Date().getTime()));
        System.out.println("cunzhuzhong,,,");
        System.out.println(sendRecordDao);
        System.out.println(sendRecord);
        sendRecordDao.addSendRecord(sendRecord);
        System.out.println("存储完毕。。。");
    }


    @Override
    public EUDataGridResult getSendRecord(int pageNum, int pageSize, String username) {
        //设置分页的参数，第几页，每页多少个
        PageHelper.startPage(pageNum, pageSize);
        //返回的就是传入参数的页数
        List<SendRecord> sendRecords=sendRecordDao.querySendRecordByUserName(username);
        EUDataGridResult result=new EUDataGridResult();
        result.setRows(sendRecords);
        PageInfo pageInfo=new PageInfo(sendRecords);
        result.setTotal(pageInfo.getPages());
        System.out.println(result);
        return result;
    }
}
