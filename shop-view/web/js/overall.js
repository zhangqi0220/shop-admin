var Config = {
    "HEADER_NAME":"x-auth",
    "COOKIE_NAME":"user_token",
    "TOKEN_NAME":"repetitive" /*重复提交*/
}
var loginStatus=false;
var mtoken;
$.ajaxSetup({
    beforeSend:function(xhr) {

        var v_fhToken =window.localStorage.getItem(Config.COOKIE_NAME);
        if (v_fhToken) {
            xhr.setRequestHeader(Config.HEADER_NAME, v_fhToken);
        }
    },
    complete : function(event,XMLHttpRequest, textStatus) {


        var responseText = event.responseText;
        var parse = JSON.parse(responseText);
        if (parse) {
            if (parse.status == 1007 || parse.status == 1006){
                console.log(parse.msg);
                alert("请登录后操作")
            }
        }
        // 通过XMLHttpRequest取得响应头，REDIRECT
        //若HEADER中含有REDIRECT说明后端想重定向
        var redirect = event.getResponseHeader("REDIRECT");
        if (redirect) {
            if (redirect == "REDIRECT") {
                location.href="/login.html";
            }
        }
    }
});

$(function () {



    var v_navHtml = " <nav class=\"nav navbar-inverse navbar-fixed-top\" role=\"navigation\">\n" +
        "        <div class=\"container-fluid\">\n" +
        "            <div class=\"navbar-header\">\n" +
        "                    <ul class=\"nav navbar-nav\">\n" +
        "                        <li><a href=\"/index.html\">首页</a></li>\n" +
        "                    </ul>\n" +
        "            </div>\n" +
        "            <div class=\"collapse navbar-collapse\">\n" +
        "                <div class=\"navbar-form navbar-right\">\n" +
        "\n" +
        "                </div>\n" +
        "                <div class=\"navbar-form navbar-right\">\n" +
        "                    <ul class=\"nav navbar-nav\">\n" +
        "                        <li class=\"dropdown\">\n" +
        "                            <a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\">服务<span class=\"caret\"></span></a>\n" +
        "                            <ul class=\"dropdown-menu\">\n" +
        "                                <li><a href=\"#\">客服在线</a></li>\n" +
        "                                <li class=\"divider\"></li>\n" +
        "                                <li><a href=\"#\">常见问答</a></li>\n" +
        "                                <li class=\"divider\"></li>\n" +
        "                                <li><a href=\"#\">地址电话</a></li>\n" +
        "                            </ul>\n" +
        "                        </li>\n" +
        "                        <li id='userStatus'><a href=\"/login.html\">登录</a></li>\n" +
        "                        <li><a href='/shopCart.html'>购物车</a></li>\n" +
        "                        <li><a href=\"#\">注销</a></li>\n" +
        "                    </ul>\n" +
        "\n" +
        "                </div>\n" +
        "            </div>\n" +
        "        </div>\n" +
        "    </nav><!--导航栏-->";

    $("#navDiv").html(v_navHtml);

    $.post({
        url:"http://localhost:8085/user/queryUser",
        dataType:"json",
        async:false,
        success:function (data) {
            if (data.status == 200) {
                loginStatus=true;
                $("#userStatus").html("<a>用户："+data.data.userName+"已登录</a>")
            }
        },error:function () {

        }

    })
    getRedoInfo();
})

function buy(productId,count,fig) {
    if (loginStatus){//全局变量 判断是否已经登录
        $.post({
            url:"http://localhost:8085/cart/buy",
            dataType:"json",
            beforeSend:function(xhr) {
                var v_fhToken =window.localStorage.getItem(Config.COOKIE_NAME);
                if (v_fhToken) {
                    xhr.setRequestHeader(Config.HEADER_NAME, v_fhToken);
                }
                xhr.setRequestHeader(Config.TOKEN_NAME, mtoken);
            },
            data:{"productId":productId,"count":count},
            success:function (data) {
                if (data.status == 200) {
                    if(fig==1){
                        location.href="/shopCart.html";
                    }else{
                        queryCart();
                    }
                }
                getRedoInfo();
            }
        })
    } else{
        location.href="/login.html";
    }


}
/*
调取重复提交的密文token
*哪个方法需要防止重复提交
* 就在方法上加上hander头
* 将密文传递
* */
function getRedoInfo() { /*获取重复提交的密文*/
    $.post({
        url:"http://localhost:8085/user/getRedoInfo",
        dataType:"json",
        success:function (data) {
            mtoken=data.data;
        }
    })
}