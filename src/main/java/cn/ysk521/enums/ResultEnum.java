package cn.ysk521.enums;

/**
 * Created by Y.S.K on 2017/10/26 in wx_bot_new.
 */
public enum  ResultEnum {
    PASSWORD_UPDATE_FAIL("1","密码修改失败"),

    ;
    private  String code;
    private String title;

    ResultEnum(String code, String title) {
        this.code = code;
        this.title = title;
    }
}
