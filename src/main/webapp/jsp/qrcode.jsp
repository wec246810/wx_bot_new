<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <script src="js/jquery.js"></script>
    <script src="js/jquery.cookie.js"></script>
</head>
<head>
    <title>二维码页面</title>
    <meta http-equiv="refresh" content="3">
    <script>
        var type =$.cookie("type");
        var content=decodeURI(decodeURI($.cookie("content")));
        window.onbeforeunload = function(event) {
            $.ajax({
                url: "/closewindow",    //请求的url地址
                data: {},
                type: "POST",   //请求方式
                success: function (req) {
                },
                error: function () {
                    alert("errbe");
                }
            });

        }
        $(document).ready(function () {
            $.ajax({
                url: "sendmessage",    //请求的url地址
//                dataType: "json",   //返回格式为json
                data: { "type": type,"content":content},    //参数值
                type: "POST",   //请求方式
                success: function (req) {
                    //请求成功时处理
                    if (req.error == "0") {
                        self.location = "/qrcode";
                    } else {

                    }
                },
                error: function () {
                    //请求出错处理
                    alert("err");
                }
            });
    });
    </script>
</head>
<body>
<img src="${qrcode}">
</body>
</html>
