<%--
  Created by IntelliJ IDEA.
  User: zhang
  Date: 2019/11/12
  Time: 14:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>忘记密码</title>
    <jsp:include page="common/script.jsp"></jsp:include>
</head>
<body>
<div style="margin-top: 80px;margin-left: 500px">
    <div style="margin-left: 180px;margin-bottom: 70px"><h1>忘记密码</h1></div>
    <form class="form-horizontal" id="formApp">
        <div class="form-group">
            <label class="col-sm-2 control-label">用户手机号</label>
            <div class="col-sm-2">
                <input type="text" class="form-control" name="phoneNum" id="phoneNum" placeholder="用户手机号">
            </div>
            <div class="col-sm-2">
                <button type="button" class="btn btn-default" id="codeBut" onclick="getPhoneCode()">获取验证码</button>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">验证码</label>
            <div class="col-sm-2">
                <input type="text" class="form-control" name="code" id="code" placeholder="验证码">
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
            <div class="col-sm-offset-2 col-sm-8">
                <button type="reset" class="btn btn-default"><i class="glyphicon glyphicon-repeat"></i>重置</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <button type="button" class="btn btn-primary" onclick="updatePassWord()"><i
                        class="glyphicon glyphicon-ok"></i>确定
                </button>
            </div>
        </div>
    </form>
</div>
</body>
<script>
    $(function () {
        initFormValidator();
    })

    function getPhoneCode() {
        var flag = $("#formApp").data("bootstrapValidator").isValidField("phoneNum");
        alert(flag)
        if (flag) {
            $.post({
                url: "/user/getPhoneCode.do",
                data: {"phoneNum": $("#phoneNum").val()},
                success: function (data) {
                    if (data.status == 200) {
                        $("#codeBut").attr("disabled", true)
                    }
                }
            });
        }else {
            $("#formApp").data("bootstrapValidator").validateField("phoneNum")
        }
    }

    function updatePassWord() {
        var flag = $("#formApp").data("bootstrapValidator").isValid();
        if (flag) {
            var data = {
                "passWord": $("#passWord").val(),
                "phoneNum": $("#phoneNum").val(),
                "code": $("#code").val()
            }
            $.post(
                "/user/updatePassWord.do",
                data,
                function (data) {
                    alert(data.msg);
                }
            )
        }else {
            $("#formApp").data("bootstrapValidator").validate();
        }
    }

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
                code: {
                    validators: {
                        notEmpty: {
                            message: '密码不能为空'
                        }
                    }
                },
                passWord: {
                    validators: {
                        notEmpty: {
                            message: '密码不能为空'
                        }
                    }
                },
                phoneNum: {
                    validators: {
                        regexp: {
                            regexp: /^1(?:3\d|4[4-9]|5[0-35-9]|6[67]|7[013-8]|8\d|9\d)\d{8}$/,
                            message: '请输入正确手机格式'
                        },
                        notEmpty: {message: '手机号码不能为空'}
                    }
                },
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
