package cn.ysk521.service.impl;

import cn.ysk521.dto.WXInfo;
import cn.ysk521.dto.myFriend;
import cn.ysk521.service.IWXService;
import cn.ysk521.utils.HttpUtils;
import cn.ysk521.utils.WxUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by Y.S.K on 2017/10/24 in wx_bot_new.
 */
@Component("wxServiceImpl")
@Scope("prototype")
public class WXServiceImpl implements IWXService {
    private final String UUIDUrl = "https://login.weixin.qq.com/jslogin";
    private final String UUIDParam = "appid=wx782c26e4c19acffb&fun=new&lang=en_CH&_=";
    private final String EWMUrl = "https://login.weixin.qq.com/qrcode/";
    private final String getRedirectUrl = "https://login.wx2.qq.com/cgi-bin/mmwebwx-bin/login";
    private  String getCookiesUrl = "https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxnewloginpage";
    private  String wxInitUrl = "https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxinit?r=";
    private  String wxGetContact = "https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxgetcontact?r=";
    private  String wxSendTextMessage = "https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxsendmsg?lang=zh_CN&pass_ticket=";

    //通过下面链接获得skey等值
    // https://login.wx2.qq.com/cgi-bin/mmwebwx-bin/login?loginicon=true&uuid=wdDksTeRMw==&tip=0&r=1305988661&_=1501932499251
    @Autowired
    private HttpUtils httpUtils;
    @Autowired
    private WxUtils wxUtils;
    //我的单个朋友朋友
    @Autowired
    private myFriend myFriend;

    @Autowired
     private WXInfo wxInfo;
//    //UUID
//    private String uuid = "";
//    //初始化所需要的wxsid.wxuin，passticket等信息的Map
     private Map initMap = new HashMap<String, String>();
//    //全部的群聊，最近+通讯录
//    public List groupList = new ArrayList<String>();
//    //自己的UserName
//    private String myUserName = "";
//    //我的全部朋友列表(带性别)
//    List myFriends = new ArrayList<myFriend>();
//    //二维码链接
//    public static String qrcodeurl = "";
//    //判断是否轮询结束
//  //  static  boolean end=false;
//    //是否进行BeforeWXInit
//    public static boolean toBeforeWxInit=false;
    //用户是否登录成功
    //public static boolean wxloginsuccess=false;

    /**
     * 获取微信二维码链接
     *
     * @return
     */
    public String getQrCode() {
        //getUUID
        String sr = httpUtils.doPost(UUIDUrl,UUIDParam);
        System.out.println(sr);
        wxInfo.setUuid(sr.split("\"")[1].toString());
        System.out.println("获取UUID完毕");
        //获取二维码
        System.out.println(EWMUrl + wxInfo.getUuid());
        wxInfo.setQrcodeurl(EWMUrl + wxInfo.getUuid());
        return EWMUrl + wxInfo.getUuid();
    }

    /**
     * 获得微信初始化之前所需的参数，可能被的多次调用，先存到map
     *
     * @return
     */
    private int time=0;
    public Map BeforeWXInit () {
        String redirectUrl = "";
        String temp = "";
        System.out.println("等待用户扫描");
        //开辟一个新线程，如果用户扫描完成，那边应该更新二维码，每个用户占用一个线程
        //201为扫描完毕，200为登录完毕。
        while (!temp.contains("window.code=200")) {
            System.out.println("正在轮询..."+wxInfo.getUuid());
            try {
                Thread.sleep(3000);
                time++;
                System.out.println("已经等待"+time*3+"秒");
                if(time>=10){
                    time=0;
                    return  null;
                }
                temp = httpUtils.doGet(getRedirectUrl, "?tip=1&uuid=" + wxInfo.getUuid() + "&_=");
                if (temp.contains("window.code=200")) {
                    System.out.println("用户登陆完成");
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while (true){
           // if(end){
                redirectUrl = temp.split("\"")[1];
                System.out.println("得到重定向连接完毕");
                String results="";
                if(redirectUrl.contains("wx.qq")){
                    results = httpUtils.doGet(redirectUrl,"&fun=new&version=v2");
                    System.out.println(results);
                }else{
                    results=httpUtils.doGet(redirectUrl,"&fun=new&version=v2&lang=zh_CN");
                }
                System.out.println("获取sid,skey,uin等完毕");
                Document dom;
                System.out.println(results);
                try {
                    dom = DocumentHelper.parseText(results);
                    Element root = dom.getRootElement();
                    String skey = root.element("skey").getText();
                    String wxsid = root.element("wxsid").getText();
                    String wxuin = root.element("wxuin").getText();
                    String pass_ticket = root.element("pass_ticket").getText();
                    initMap.put("skey", skey);
                    initMap.put("wxsid", wxsid);
                    initMap.put("wxuin", wxuin);
                    initMap.put("pass_ticket", pass_ticket);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                System.out.println("获取sid,skey,uin等已放入map");
                System.out.println(redirectUrl);
                if(redirectUrl.contains("wx2.qq")){
                    getCookiesUrl=   getCookiesUrl.replaceAll("wx.qq","wx2.qq");
                    wxInitUrl= wxInitUrl.replaceAll("wx.qq","wx2.qq");
                    wxGetContact= wxGetContact.replaceAll("wx.qq","wx2.qq");
                    wxSendTextMessage= wxSendTextMessage.replaceAll("wx.qq","wx2.qq");
                }
                System.out.println(initMap);
                break;
            }

       // }
        return initMap;
    }

    public void wxInit() {
        System.out.println(wxInitUrl);
        //https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxinit?r=1305633400&lang=zh_CN&pass_ticket=DQOObfCJSeYIh2QikKHp%252FuFEmMFTlNhfY5R28dINJTs%252BlfYluI84lumEDSSIA5nZ
        //微信初始化
        String initResult = httpUtils.doPost(wxInitUrl + System.currentTimeMillis() + "&lang=ch_ZN&pass_ticket=" + initMap.get("pass_ticket"),
                "{\"BaseRequest\":{\"Uin\":\""
                        + initMap.get("wxuin")
                        + "\",\"Sid\":\""
                        + initMap.get("wxsid")
                        + "\",\"Skey\":\""
                        + initMap.get("skey")
                        + "\",\"DeviceID\":\""
                        + wxUtils.getDeviceID()
                        + "\"}}");

        System.out.println(initResult);
        //通过微信初始化获得最近朋友列表
        JSONArray tempJsonArray = new JSONArray();
//        System.out.println(initResult);
        tempJsonArray = JSONObject.fromObject(initResult).getJSONArray("ContactList");
        System.out.println("tempJsonArray:"+tempJsonArray);
        Iterator<Object> it = tempJsonArray.iterator();
        JSONObject tempJSONObject = new JSONObject();
         List groupList = new ArrayList<String>();
        while (it.hasNext()) {
            JSONObject ob = (JSONObject) it.next();
            if (ob.get("UserName").toString().contains("@@")) {
                groupList.add(ob.get("UserName").toString());
            }
        }
        wxInfo.setGroupList(groupList);
        //获得自己的UserName
        JSONObject json;
        json = JSONObject.fromObject(initResult);
        JSONObject json1 = JSONObject.fromObject(json.get("User"));
//        System.out.println(json1);
        wxInfo.setMyUsername((String) json1.get("UserName"));
        System.out.println("得到自己的UserName");
    }

    //获得好友列表
    public List<myFriend> getMyFriendList() {
        String myfriendsresult = httpUtils.doPost(wxGetContact + System.currentTimeMillis() + "&pass_ticket=" + initMap.get("pass_ticket"), "{\"BaseRequest\":{\"Uin\":\""
                + initMap.get("wxuin")
                + "\",\"Sid\":\""
                + initMap.get("wxsid")
                + "\",\"Skey\":\""
                + initMap.get("skey")
                + "\",\"DeviceID\":\""
                + wxUtils.getDeviceID()
                + "\"}}");
        JSONObject json = JSONObject.fromObject(myfriendsresult);
        List groupList = new ArrayList<String>();
        List myFriends = new ArrayList<myFriend>();
        for (int i = 0; i < Integer.parseInt(json.get("MemberCount").toString()); i++) {
            myFriend tempmyFriend = new myFriend();
            JSONObject tempJsonObject = JSONObject.fromObject(json.getJSONArray("MemberList").get(i));
            //获得好友中的群组
            if ((tempJsonObject.get("UserName").toString().contains("@@"))) {
                //好友列表中的群组添加到grouplist
                groupList.add(tempJsonObject.get("UserName").toString());
            }
            tempmyFriend.setUserName(tempJsonObject.get("UserName").toString());
            tempmyFriend.setSex(Integer.parseInt(tempJsonObject.get("Sex").toString()));
            myFriends.add(tempmyFriend);
            wxInfo.setMyFriends(myFriends);
        }
        //wxInfo.setGroupList(groupList);
        System.out.println(myFriends);
        System.out.println("得到所有的朋友的UserName完毕");
        return myFriends;
    }

    //获取分组性别
    public List getMyFriends(int sex) {
        List myFrineds = new ArrayList<String>();
        for (int i = 0; i < wxInfo.getMyFriends().size(); i++) {
            myFriend = (myFriend) wxInfo.getMyFriends().get(i);
            if (myFriend.getSex() == sex) {
                myFrineds.add(myFriend.getUserName().toString());
            }
        }
        return myFrineds;
    }

    public void sendMessage(String content, List To)  {
        //------------
        List<String> a=new ArrayList<>();
        a.add("filehelper");
        To=a;
        //------------------------
        System.out.println(content);
//        try {
//            content = new String(content.getBytes("UTF-8"), "GBK");
//            System.out.println("cont:"+content);
//        }catch(UnsupportedEncodingException e){
//            e.printStackTrace();
//        }
        long stamp = wxUtils.getMsgId();
        for (int i = 0; i < To.size(); i++) {
            httpUtils.doPost(wxSendTextMessage + initMap.get("pass_ticket"),
                    "{\"BaseRequest\":{\"DeviceID\" : \""
                            + wxUtils.getDeviceID()
                            + "\",\"Sid\" : \""
                            + initMap.get("wxsid")
                            + "\",\"Skey\" : \""
                            + initMap.get("skey")
                            + "\",\"Uin\" : "
                            + initMap.get("wxuin")
                            + "},\"Msg\" : {\"Type\" : "
                            + "1"
                            + ",\"Content\" : \""
                            + content
//                            + "测试哈"
                            + "\",\"FromUserName\" : \""
                            + wxInfo.getMyUsername()
                            + "\",\"ToUserName\" : \""
                            + To.get(i).toString()
//				+allUerName.get(i)
                            + "\",\"LocalID\" : \""
                            + stamp
                            + "\",\"ClientMsgId\" : "
                            + stamp
                            + "}}");
            System.out.println(wxSendTextMessage + initMap.get("pass_ticket"));
            System.out.println("{\"BaseRequest\":{\"DeviceID\" : \""
                    + wxUtils.getDeviceID()
                    + "\",\"Sid\" : \""
                    + initMap.get("wxsid")
                    + "\",\"Skey\" : \""
                    + initMap.get("skey")
                    + "\",\"Uin\" : "
                    + initMap.get("wxuin")
                    + "},\"Msg\" : {\"Type\" : "
                    + "1"
                    + ",\"Content\" : \""
                    + content
                    + "\",\"FromUserName\" : \""
                    + wxInfo.getMyUsername()
                    + "\",\"ToUserName\" : \""
                    + To.get(i).toString()
//				+allUerName.get(i)
                    + "\",\"LocalID\" : \""
                    + stamp
                    + "\",\"ClientMsgId\" : "
                    + stamp
                    + "}}");
        }
        System.out.println("发送信息完毕");
    }

    public WXInfo getWxInfo() {
        return wxInfo;
    }

    public void setWxInfo(WXInfo wxInfo) {
        this.wxInfo = wxInfo;
    }
}
