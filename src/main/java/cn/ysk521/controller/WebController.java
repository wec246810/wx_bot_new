package cn.ysk521.controller;

import cn.ysk521.dao.CDKDao;
import cn.ysk521.dao.UserDao;
import cn.ysk521.dto.EUDataGridResult;
import cn.ysk521.dto.Result;
import cn.ysk521.entity.SendRecord;
import cn.ysk521.entity.User;
import cn.ysk521.service.IUserService;
import cn.ysk521.service.impl.WXServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Y.S.K on 2017/10/24 in wx_bot_new.
 */
@Controller("/webController")
@Scope("session")
public class WebController {
    UserDao userDao;
    @Autowired
    IUserService userServiceImpl;
    @Autowired
    CDKDao cdkDao;
    @Autowired
    WXServiceImpl wxServiceImpl;

    private boolean cw=false;

  private   volatile String content="";
    @PostMapping(value = "check", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public Result check(@RequestParam("username") String username, @RequestParam("password") String password,
                        HttpServletRequest request, HttpServletResponse response) {
        Result result;
        //check表明没有cookies
        //假如登录成功
        User user = userServiceImpl.dologin(username, password);
        if (user != null && password.equals(user.getPassword())) {
            //存入cookies
            Cookie cusername = new Cookie("username", username);
            Cookie cpassword = new Cookie("password", password);
            response.addCookie(cusername);
            response.addCookie(cpassword);
            result = new Result("0", "登录成功");
            return result;
        } else {
            //登录失败
            result = new Result("1", "用户名或密码错误！");
            return result;
        }
    }
    @PostMapping("/closewindow")
    public boolean closeWindow(){
        cw=true;
        return cw;
    }

    @GetMapping("login")
    public String login(
            HttpServletRequest httpServletRequest) {
        System.out.println(httpServletRequest);
        if (httpServletRequest == null) {
            return "login";
        }
        Cookie[] cookies = httpServletRequest.getCookies();
        String username = "";
        String password = "";
        if (cookies != null && cookies.length > 0) {
            //取出cookies进行验证
            for (Cookie c : cookies) {
                if ("username".equals(c.getName())) {
                    username = c.getValue();
                }

            }
        }
        if (username != "") {
            userServiceImpl.dologin(username, password);
            return "redirect: touse";
        } else {
            System.out.println("return login");
            return "login";
        }
    }
    @PostMapping(value = "MyTime", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result getMyTime(@RequestParam("username") String username, @RequestParam("password") String password) {

      User user = userServiceImpl.queryUserByName(username);
        if (username != null && user.getExpirationTime().getTime() > 0) {
            return new Result("0", new Timestamp(user.getExpirationTime().getTime()).toString());
        } else {
            return new Result("1", "已过期");
        }
    }

    @PostMapping(value = "/UCDK", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result UCDK(@RequestParam("cdkValue") String cdkValue, @RequestParam("username") String userName) {
        String resu = userServiceImpl.useCDK(userName, cdkValue);
        if (resu.contains("0")) {
            return new Result("0", "充值成功");
        } else if (resu.contains("1")) {
            return new Result("1", "cdk已被使用");
        } else {
            return new Result("2", "cdk无效");
        }
    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies.length > 0) {
            for (Cookie c : cookies) {
                c.setMaxAge(0);
                response.addCookie(c);
            }
        }
        return "redirect:/login";
    }


    @PostMapping(value = "sendmessage", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Result sendMessage( @RequestParam("type") String type,@RequestParam("content")  String content1,HttpServletRequest request,HttpServletResponse response) {
        //这个页面会被不断刷新,存在二维码失效问题。
        if(userServiceImpl.queryUserByName(getuser(request)[0]).getExpirationTime().getTime()>System.currentTimeMillis()) {
            System.out.println("未过期");
            content = content1;
            synchronized (this) {
                try {
                    if(wxServiceImpl.BeforeWXInit()==null){
                        if(cw==false){
                        request.setAttribute("qrcode",  wxServiceImpl.getQrCode());
                        return new Result("0", "succ");}
                        else {
                            return new Result("2", "cw");


                    }
                    }
                    wxServiceImpl.wxInit();
                    wxServiceImpl.getMyFriendList();
                    if (type.contains("1")) {
                        //给男的发送消息
//                    wxServiceImpl.sendMessage(content, wxServiceImpl.getMyFriends(1));
                        System.out.println(wxServiceImpl.getMyFriends(1));
                    }
                    if (type.contains("2")) {
                        //给女的发送消息
//                    wxServiceImpl.sendMessage(content, wxServiceImpl.getMyFriends(2));
                        System.out.println(wxServiceImpl.getMyFriends(2));
                    }
                    if (type.contains("3")) {
                        //给群聊发送消息
//                    wxServiceImpl.sendMessage(content, wxServiceImpl.getWxInfo().getGroupList());
                        System.out.println(wxServiceImpl.getWxInfo().getGroupList());
                    }
                    if (type.contains("4")) {
                        List l = new ArrayList();
                        l.add("filehelper");
                        System.out.println("我正在发送的内容是：" + content);
                        wxServiceImpl.sendMessage(content, l);
                    }
                    wxServiceImpl.getQrCode();
                    System.out.println(wxServiceImpl.getWxInfo().getQrcodeurl());
                    request.setAttribute("qrcode", wxServiceImpl.getWxInfo().getQrcodeurl());
//                new WebController().getqrcodeurl(request,response);
                    System.out.println("-------------------完成");
                    return new Result("0", "succ");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return new Result("1","error");
    }

    @GetMapping("/sendRecord/{pageNum}/{pageSize}")
    public String sendRecord(@PathVariable("pageNum") String pageNum, @PathVariable("pageSize") String pageSize, HttpSession httpSession,HttpServletRequest request) {
        int pageNumi = Integer.parseInt(pageNum);
        int pageSizei = Integer.parseInt(pageSize);
//        Cookie cpageNum=new Cookie("pageNum",String.valueOf(pageNumi));
//        Cookie cpageSize=new Cookie("pageSize",String.valueOf(pageSizei));
        System.out.println(httpSession);
        System.out.println(userServiceImpl);

        httpSession.setAttribute("pageNum",pageNumi);
        httpSession.setAttribute("pageSize",pageSizei);
        String username=getuser(request)[0];
        EUDataGridResult euDataGridResult=userServiceImpl.getSendRecord(pageNumi,pageSizei,username);
        System.out.println(euDataGridResult);
        if(euDataGridResult!=null){
            int totalPage=(int)euDataGridResult.getTotal();
            httpSession.setAttribute("totalPage",totalPage);
        }
        return "sendrecord";
    }
    boolean first=true;
    @GetMapping("qrcode")
    public String getqrcodeurl(HttpServletRequest request,HttpServletResponse response) {
        if(first) {
            wxServiceImpl.getQrCode();
            System.out.println(wxServiceImpl.getWxInfo().getQrcodeurl());
            first=false;
        }
        request.setAttribute("qrcode", wxServiceImpl.getWxInfo().getQrcodeurl());
        return "qrcode";
    }


    @PostMapping(value = "/updatepassword", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result updatepassoword(@RequestParam("oldpassword") String oldpassword, @RequestParam("newpassword") String newpassword, HttpServletRequest request, HttpServletResponse response) {
        //如果cookies
        Cookie[] cookies = request.getCookies();
        String username = getuser(request)[0];
        String password = getuser(request)[1];
//        if (cookies.length > 0) {
//            //取出cookies进行验证
//            for (Cookie c : cookies) {
//                if ("username".equals(c.getName())) {
//                    username = c.getValue();
//                } else if ("password".equals(c.getName())) {
//                    password = c.getValue();
//                }
//            }
//        }
        System.out.println(username + " " + password);
        if (username != "" && oldpassword.equals(password)) {
            //更改密码
            User user = userServiceImpl.queryUserByName(username);
            System.out.println(user.getExpirationTime());
            if (user != null) {
                System.out.println("-------");
                user.setPassword(newpassword);
                userServiceImpl.updateMyPassword(user);
                for (Cookie c : cookies) {
                    c.setMaxAge(0);
                    response.addCookie(c);
                }
                return new Result("0", "密码修改成功");
            }
        }
        return new Result("1", "密码修改失败!");
    }
    //保存发送记录
    @PostMapping(value = "saveSendRecord", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public void getContent(@RequestParam("content")String content2, HttpServletRequest request) {
        System.out.println("saveSendRecord");
        System.out.println(getuser(request)[0]);
        System.out.println(content2);
        userServiceImpl.setSendRecord(getuser(request)[0], content2);
    }

    @PostMapping(value = "/getSendRecord", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<SendRecord> getSendRecord(@RequestParam("pageNum")String pageNum, @RequestParam("pageSize") String pageSize, HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        String username=getuser(request)[0];
        int pageNumi=Integer.parseInt(pageNum);
        int pageSizei=Integer.parseInt(pageSize);
        EUDataGridResult euDataGridResult=userServiceImpl.getSendRecord(pageNumi,pageSizei,username);
        if(euDataGridResult!=null){
            List<SendRecord>  sendRecords=(List<SendRecord>) euDataGridResult.getRows();
            return sendRecords;
        }
        return  null;

    }


    @GetMapping("/")
    public String index() {
        return "touse";
    }

    @GetMapping("touse")
    public String touse(HttpServletRequest request) {
        if(userServiceImpl.queryUserByName(getuser(request)[0]).getExpirationTime().getTime()>System.currentTimeMillis()) {
            return "touse";
        }else {
            return "useCDK";
        }
    }

    @GetMapping("mycount")
    public String myCount() {
        return "mycount";
    }

    @GetMapping("useCDK")
    public String useCDK() {
        return "useCDK";
    }

    @GetMapping("topay")
    public String topay() {
        return "toPay";
    }


    @GetMapping("updatepwd")
    public String updatepwd() {
        return "updatepwd";
    }

  public static String[]  getuser(HttpServletRequest request){
      Cookie[] cookies = request.getCookies();
    String[] re=new String[2];
      if (cookies.length > 0) {
          //取出cookies进行验证
          for (Cookie c : cookies) {
              if ("username".equals(c.getName())) {
                 re[0] = c.getValue();
              } else if ("password".equals(c.getName())) {
                  re[1] = c.getValue();
              }
          }
      }
      return  re;
  }
}
