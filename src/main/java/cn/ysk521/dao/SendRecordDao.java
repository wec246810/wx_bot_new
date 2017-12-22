package cn.ysk521.dao;


import cn.ysk521.entity.SendRecord;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Y.S.K on 2017/8/2 in wx_bot.
 */
@Component("sendRecordDao")
public interface SendRecordDao {
        void addSendRecord(SendRecord sendRecord);
        List<SendRecord> querySendRecordByUserName(String userName);
        List<SendRecord> queryAllSendRecord();
        List<SendRecord> querySendRecordByPage(int start, int size);
}
