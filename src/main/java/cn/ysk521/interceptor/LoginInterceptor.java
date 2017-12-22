package cn.ysk521.interceptor;

import cn.ysk521.controller.WebController;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Y.S.K on 2017/8/2 in wx_bot.
 */
@Component("loginInterceptor")
public class LoginInterceptor implements HandlerInterceptor {
//    @Resource
//    private UserDao userDao;
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        System.out.println("进入拦截器。。");
        String username = "";
        String password = "";
//        Cookie[]  cookies=httpServletRequest.getCookies();
//        if(httpServletRequest.getSession().getAttribute("username")!=null&&httpServletRequest.getSession().getAttribute("password")!=null){
//            username=httpServletRequest.getSession().getAttribute("username").toString();
//            password=httpServletRequest.getSession().getAttribute("password").toString();
//        }
//        if(!"".equals(username)&&!"".equals(password)&&password.equals(userDao.queryUserByName(username).getPassword())){
//            return true;
//        }else {
//            httpServletResponse.sendRedirect("/login");
//            return false;
//        }
        //如果cookies不为空
        if(httpServletRequest.getCookies()!=null){
            username= WebController.getuser(httpServletRequest)[0];
            password= WebController.getuser(httpServletRequest)[1];
            System.out.println(username);
            System.out.println(password);
            if(!"".equals(username)&&!"".equals(password)&&username!=null&&password!=null){
                System.out.println("不进行拦截");
                return  true;
            }
                System.out.println("发生错误");
                httpServletResponse.sendRedirect("/login");
                return false;
        }
//        if(!"".equals(username)&&!"".equals(password)&&password.equals(userDao.queryUserByName(username).getPassword())){
//            return true;
//        }else {
//            httpServletResponse.sendRedirect("/login");
//            return false;
//        }
        httpServletResponse.sendRedirect("/login");
               return false;

    }

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
