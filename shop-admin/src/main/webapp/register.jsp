<%--
  Created by IntelliJ IDEA.
  User: gy
  Date: 2019/10/24
  Time: 11:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>注册</title>
    <jsp:include page="common/script.jsp"></jsp:include>
</head>
<script>
    $(function () {
        queryArea(0,null,1);
    })

    function queryArea(pid, obj, a) {
        //清除当前节点的父节点 之后的节点
        $(obj).parent().nextAll().remove();
            $.post({
                url: "/user/queryArea.do",
                data: {"id": pid},
                dataType: "json",
                success: function (data) {
                    if (data.status == 200 && data.data.length > 0) {
                        /*定义一个字符串*/
                        var str = " <div class=\"col-sm-2\" >\n" +
                            " <select class=\"form-control\" id=\"area"+(a++)+"\" onchange=\"queryArea(this.value,this," + a + ")\">\n" +
                            "<option value=\"-1\">==请选择==</option>\n";
                        /*循环数据拼接*/
                        $(data.data).each(function () {
                            str += "<option value='" + this.id + "'>" + this.name + "</option>";
                        })
                        str += "</select></div>";
                        /*拼接到指定div*/
                        $("#area").append(str)
                    }
                }
            });
    }
</script>
<body>
<div style="margin-top: 80px;margin-left: 500px">
    <div style="margin-left: 180px;margin-bottom: 70px"><h1>注册</h1></div>
    <form class="form-horizontal" id="formApp" action="/user/addUser.do" method="post">
        <div class="form-group">
            <label class="col-sm-2 control-label">用户名</label>
            <div class="col-sm-2">
                <input type="text" class="form-control" name="userName" id="userName" placeholder="用户名">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">密码</label>
            <div class="col-sm-2">
                <input type="text" class="form-control" name="passWord" id="passWord" placeholder="密码">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">确认密码</label>
            <div class="col-sm-2">
                <input type="text" class="form-control" name="rePassWord" placeholder="确认密码">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">手机号码</label>
            <div class="col-sm-2">
                <input type="text" class="form-control" name="phoneNum" id="phoneNum" placeholder="手机号码">
            </div>
        </div>
        <div class="form-group"id="area">
            <label class="col-sm-2 control-label">地区选择</label>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-8">
                <button type="reset" class="btn btn-default"><i class="glyphicon glyphicon-repeat"></i>重置</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <button type="button" class="btn btn-primary" onclick="submitForm()"><i
                        class="glyphicon glyphicon-ok"></i>提交
                </button>
            </div>
        </div>
    </form>
</div>
</body>

<script>
    function submitForm() {
        var flag = $("#formApp").data("bootstrapValidator").isValid();
        if (flag){
            var data={
                "userName":$("#userName").val(),
                "passWord":$("#passWord").val(),
                "area1":$("#area1").val()==-1?null:$("#area1").val(),
                "area2":$("#area2").val()==-1?null:$("#area2").val(),
                "area3":$("#area3").val()==-1?null:$("#area3").val(),
                "phoneNum":$("#phoneNum").val()
            }
            //$("#formApp").serialize(),
            $.post(
                "/user/addUser.do",
                data,
                function (result) {
                    if (result.status == 200) {
                        location.href = "/login.jsp";
                    }
                }
            )
        }
    }


    $(function () {
        initFormValidator();
    })

    function initFormValidator() {
        $('#formApp').bootstrapValidator({
            // 默认的提示消息
            message: 'This value is not valid',
            // 表单框里右侧的icon
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
                userName: {
                    message: '用户名验证失败',
                    validators: {
                        notEmpty: {
                            message: '用户名不能为空'
                        },
                        remote: {//ajax验证。server result:{"valid",true or false}
                            url: "/user/checkUserByName.do",
                            message: '用户名已存在,请重新输入',
                            delay: 700,//ajax刷新的时间是0.7秒一次
                            type: 'POST',
                            //自定义提交数据，默认值提交当前input value
                            data: function (validator) {
                                return {
                                    userName: $("input[name=userName]").val(),
                                };
                            }
                        }
                    }
                },
                passWord: {
                    validators: {
                        notEmpty: {
                            message: '密码不能为空'
                        }
                    }
                } ,
                phoneNum: {
                    validators: {
                        regexp: {
                            regexp: /^1(?:3\d|4[4-9]|5[0-35-9]|6[67]|7[013-8]|8\d|9\d)\d{8}$/,
                            message: '请输入正确手机格式'
                        },
                        notEmpty: {message: '手机号码不能为空'}
                    }
                } ,
                rePassWord: {
                    validators: {
                        notEmpty: {
                            message: '密码不能为空'
                        },
                        identical: {
                            field: 'passWord',
                            message: '两次密码输入不一致'
                        }
                    }
                }
            }
        });
    }

</script>

</html>
