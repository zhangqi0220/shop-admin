<%--
  Created by IntelliJ IDEA.
  User: gy
  Date: 2019/10/18
  Time: 9:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<script>
    var butList =[];
    $(function(){
        $.post({
            url:"<%=request.getContextPath()%>/user/queryUserResourceByType.do",
            dataType:"json",
            async:false,
            success:function (data) {
                if (data.status==200){
                    var li="";
                    $(data.data).each(function(){
                        if (this.type==1){
                            li+="<li id=\"item"+this.id+"\" ><a href=\""+this.url+"#"+this.id+"\">"+this.resourceName+"</a></li>";
                        }else if (this.type==2){
                            butList.push(this);
                        }
                    })
                    $("#item-ul").html(li)
                }
            }
        })
    })

    var add_status="hidden";
    var upl_status="hidden";
    var del_status="hidden";

    function getResource() {
        var hash =  window.location.hash
        var id=  hash.substring(1);
        for (var i = 0; i < butList.length; i++) {
            if (butList[i].pid==id){
                if (i.resourceName=="增加"){
                    add_status="";
                }
                if (butList[i].resourceName=="修改"){
                    upl_status="";
                }
                if (butList[i].resourceName=="删除"){
                    del_status="";
                }
            }
        }
    }

</script>
<body>
<nav class="navbar navbar-inverse">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">飞狐教育</a>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav" id="item-ul">
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li><a href="javascript:void(0)" onclick="refresh()">刷新</a></li>
                <li><a href="javascript:void(0)" onclick="logout()">注销</a></li>
            </ul>
        </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
</nav>
</body>
<script>
    function refresh() {
        $.post(
            "/user/refresh.do",
            function (data) {
                if (data.status==200){
                    location.reload();
                }
            }
        )
    }
    function logout() {
        $.post(
            "/user/logout.do",
            function (data) {
                if (data.status==200){
                    location.href="/";
                }
            }
        )
    }



    var imgUrl="http://192.168.216.136:8000/";
    $.ajaxSetup({
        complete:function(data,TS){
        var time =     data.getResponseHeader("ajaxTime");
        if(time=="timeOut"){
            window.location.href="/login.jsp"
        }
    //对返回的数据data做判断，
    //session过期的话，就location到一个页面
        }

    });



    $(function () {
       getResource();
        //获取请求路径#的值
       var hash =  window.location.hash
       if(hash.length>0){
           //清除所有的样式
           $("#item-ul >li").removeClass("active");
           //去除#
         var id=  hash.substring(1);
         //给指定的标签加样式
           $("#item"+id).addClass("active");
       }
    })

</script>
</html>
