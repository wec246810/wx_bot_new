package cn.ysk521.dao;
import cn.ysk521.entity.CDK;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Y.S.K on 2017/8/3 in wx_bot.
 */
@Component("cdkDao")
public interface CDKDao {
    void  addCDK(CDK cdk);
    void  deleteCDKByValue(String value);
    List<CDK> queryAllCDK();
    CDK  queryCDKByValue(String value);
    void upadteCDK(CDK cdk);
}
