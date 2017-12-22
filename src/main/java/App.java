import cn.ysk521.dao.CDKDao;
import cn.ysk521.dao.UserDao;
import cn.ysk521.entity.User;
import cn.ysk521.utils.ToolUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Y.S.K on 2017/10/24 in wx_bot_new.
 */
public class App {
    public static void main(String[] args) {

        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:spring/spring-dao.xml");
//         UserDao userDao=(UserDao) applicationContext.getBean("userDao");
//         User user=userDao.queryUserByName("1");
//        System.out.println(user);

        ToolUtils toolUtils=(ToolUtils) applicationContext.getBean("toolUtils");
        for (int i = 0; i <20; i++) {

        }
    }
}
