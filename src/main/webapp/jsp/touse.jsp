<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <c:import url="header.jsp"/>
    <c:import url="left.jsp"/>
</head>
<head>
    <title>使用页面</title>
    <script>
        $(document).ready(function () {
            var url = "qrcode";
            var type="";
            $("#but").click(function () {
                type="";
                <%--<c:set value= var="aaa" scope="session"/>--%>
                if ($("input[name='man']").is(':checked')) {
                    type = type + "1";
                }
                if ($("input[name='woman']").is(':checked')) {
                    type = type + "2";
                }
                if ($("input[name='group']").is(':checked')) {
                    type = type + "3";
                }
                if ($("input[name='filehelper']").is(':checked')) {
                    type = type + "4";
                }

                if ( $.trim($("#content").val()).length > 0&$("input[name='man']").is(':checked') | $("input[name='woman']").is(':checked') | $("input[name='group']").is(':checked') ) {
                    var content = $.trim($("#content").val());
                    //将内容存入cookie中
                    $.cookie('content',encodeURI(content), {expires: 1});
//                    document.cookie="content="+escape("珊珊");
                    $.cookie('type', type, {expires: 1});
                    $.ajax({
                        url: "saveSendRecord",    //请求的url地址
//                        dataType: "html",   //返回格式为json
                        data: {'content':content},    //参数值
                        type: "POST",   //请求方式
                        success: function (req) {
                        },
                        error: function () {
                            //请求出错处理
                            alert("err");
                        }
                    });
                    window.open(url);
                } else {
                    alert("请至少选择一个发送对象并输入发送内容");
                }
            });
        });
    </script>
</head>

<body>
<section class="rt_wrap content mCustomScrollbar">
    <div class="rt_content">
        <div>
            <input type="text" id="content" name="content" class="textbox textbox_295" placeholder="输入需要发送的内容..."/>
            <%--<c:set value="$"--%>
            <input type="button" id="but" value="生成二维码" class="link_btn"/>
        </div>
        <div class="checkbox checkbox-success">
            <input id="man" type="checkbox" name="man" value="man"/>
            <label for="man">
                男
            </label>
        </div>
        <div class="checkbox checkbox-success">
            <input id="woman" type="checkbox" name="woman" value="woman"/>
            <label for="woman">
                女
            </label>
        </div>
        <div class="checkbox checkbox-success">
            <input id="group" type="checkbox" name="group" value="group"/>
            <label for="group">
                群聊
            </label>
        </div>
        <div class="checkbox checkbox-success">
            <input id="filehelper" type="checkbox" name="filehelper" value="filehelper"/>
            <label for="filehelper">
               文件助手
            </label>
        </div>

    </div>
</section>
</body>
</html>
