<%--
  Created by IntelliJ IDEA.
  User: gy
  Date: 2019/10/24
  Time: 15:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <jsp:include page="/common/script.jsp"></jsp:include>
</head>
<script>
    $(function () {
        initDateTable();
    })

    //查询地区
    function queryArea(pid, obj, a) {
        //清除当前节点的父节点 之后的节点
        $(obj).parent().nextAll().remove();
        $.post({
            url: "/user/queryArea.do",
            data: {"id": pid},
            async:false,
            dataType: "json",
            success: function (data) {
                if (data.status == 200 && data.data.length > 0) {
                    /*定义一个字符串*/
                    var str = " <div class=\"col-sm-2\" >\n" +
                        " <select class=\"form-control\"  id=\"area" + (a++) + "\" onchange=\"queryArea(this.value,this," + a + ")\">\n" +
                        "<option value=\"-1\">==请选择==</option>\n";
                    /*循环数据拼接*/
                    $(data.data).each(function () {
                        str += "<option  value='" + this.id + "'>" + this.name + "</option>";
                    })
                    str += "</select></div>";
                    /*拼接到指定div*/
                    $("#areas").append(str)
                }
            }
        });
    }

    function initDateTable() {
        myTable = $('#example').DataTable({
            "serverSide": true,
            // 是否允许检索
            "searching": false,
            "lengthMenu": [5, 10, 20, 50],
            "ajax": {
                url: '<%=request.getContextPath()%>/user/queryList.do',
                type: 'POST',
                //用于处理服务器端返回的数据。 dataSrc是DataTable特有的
                dataSrc: function (result) {
                    if (result.status == 200) {
                        result.draw = result.data.draw;
                        result.recordsTotal = result.data.recordsTotal;
                        result.recordsFiltered = result.data.recordsFiltered;
                        return result.data.data;
                    } else {
                        return "";
                    }
                }
            },
            "columns": [
                {
                    "data": "id", render: function (data, type, row, meta) {
                        return '<input type="checkbox" value="' + data + '" name="ids">';
                    }
                },
                {"data": "userName"},
                //{"data": "passWord"},
                {"data": "realName"},
                {"data": "sex", render: function (data, type, row, meta) {
                        return data==1?"男":data==2?"女":"";
                    }},
                {"data": "age"},
                {"data": "areaName"},
                {"data": "phoneNum"},
                {"data": "salary"},
                {
                    "data": "joinTime", render: function (data, type, row, meta) {
                        return new Date(data).toLocaleString();
                    }
                },
                {
                    "data": "id", render: function (data, type, row, meta) {
                        var ro = JSON.stringify(row).replace(/\"/g, "'")
                        return '<div class="btn-group" role="group" aria-label="...">' +
                            '<button type="button" class="btn btn-info" onclick="toUpdate(' + ro + ')" ><i class="glyphicon glyphicon-wrench"></i>修改</button>' +
                            '<button type="button" class="btn btn-danger "  onclick="deleteProduct(' + data + ')"><i class="glyphicon glyphicon-remove"></i>删除</button>' +
                            '</div>';
                    }
                }

            ],
            "initComplete": function (setting, json) {

            },
            "drawCallback": function (settings) {
                /* console.log(idList);
                 if(idList.length>0){
                     $("[name='ids']").each(function () {
                         if(idList.indexOf(this.value)!=-1){
                             this.checked=true;
                             $(this).parent().parent().css("background-color","#66afe9");
                         }
                     })
                 }*/
            },
            "language": {
                "sProcessing": "处理中...",
                "sLengthMenu": "_MENU_ 记录/页",
                "sZeroRecords": "没有匹配的记录",
                "sInfo": "显示第 _START_ 至 _END_ 项记录，共 _TOTAL_ 项",
                "sInfoEmpty": "显示第 0 至 0 项记录，共 0 项",
                "sInfoFiltered": "(由 _MAX_ 项记录过滤)",
                "sInfoPostFix": "",
                "sSearch": "过滤:",
                "sUrl": "",
                "oPaginate": {
                    "sFirst": "首页",
                    "sPrevious": "上页",
                    "sNext": "下页",
                    "sLast": "末页"
                }
            }
        });
    }

    function toAddJsp() {
        bootbox.dialog({
            message: $("#showAddUserDiv").html(),
            title: "添加",
            size: "large",
            buttons: {
                Cancel: {
                    label: "取消",
                    className: "btn-default",
                    callback: function () {
                    }
                }
                , OK: {
                    label: "确认",
                    className: "btn-danger",
                    callback: function () {
                        //获取当前表单验证状态
                        var flag = $("#formUser").data("bootstrapValidator").isValid();
                        var data;
                        if (flag) {
                            var area1 = $("#area1").val() == -1 ? null : $("#area1").val();
                            var area2 = $("#area2").val() == -1 ? null : $("#area2").val();
                            var area3 = $("#area3").val() == -1 ? null : $("#area3").val();
                            var ids = getRole();
                            var sex = $('input:radio:checked').val()
                            data = {
                                "userName": $("#userName").val(),
                                "passWord": $("#passWord").val(),
                                "realName": $("#roleName").val(),
                                "sex": sex,
                                "age": $("#age").val(),
                                "area1": area1,
                                "area2": area2,
                                "area3": area3,
                                "salary": $("#salary").val(),
                                "phoneNum": $("#phone").val(),
                                "idList": ids
                            }
                            $.post(
                                "/user/addUser.do",
                                data,
                                function (data) {
                                    if (data.status == 200) {
                                        $("#example").dataTable().fnDraw(false);
                                    } else {
                                        bootbox.alert("操作失败！,请联系管理员", function () {
                                        })
                                    }
                                })
                        }
                    }
                }
            }
        });
        queryArea(0, null, 1);
        initUserNameValidator();
        queryUserRole();
    }

    function toUpdate(row) {
        bootbox.dialog({
            message: $("#showAddUserDiv").html(),
            title: "添加",
            size: "large",
            buttons: {
                Cancel: {
                    label: "取消",
                    className: "btn-default",
                    callback: function () {
                    }
                }
                , OK: {
                    label: "确认",
                    className: "btn-danger",
                    callback: function () {
                        //获取当前表单验证状态
                            var area1 = $("#area1").val() == -1 ? null : $("#area1").val();
                            var area2 = $("#area2").val() == -1 ? null : $("#area2").val();
                            var area3 = $("#area3").val() == -1 ? null : $("#area3").val();
                            var ids = getRole();
                            var sex = $('input:radio:checked').val()
                        var data = {
                                "id":$("#userId").val(),
                                "userName": $("#userName").val(),
                                "realName": $("#roleName").val(),
                                "sex": sex,
                                "age": $("#age").val(),
                                "area1": area1,
                                "area2": area2,
                                "area3": area3,
                                "salary": $("#salary").val(),
                                "phoneNum": $("#phone").val(),
                                "idList": ids
                            }
                            $.post(
                                "/user/updateUser.do",
                                data,
                                function (data) {
                                    if (data.status == 200) {
                                        $("#example").dataTable().fnDraw(false);
                                    } else {
                                        bootbox.alert("操作失败！,请联系管理员", function () {
                                        })
                                    }
                                })
                        }
                    }
            }
        });

        queryArea(0, null, 1);
       // initUserNameValidator();
        queryUserRole();
        toUpdateDiv(row);
    }

    function toUpdateDiv(row) { //修改 给输入框赋值回显

        $("[name='pass']").css("display","none")
        $("#userName").val(row.userName);
        $("#userId").val(row.id);
        $("#roleName").val(row.realName);
        $('.sex').each(function () {
            if ($(this).val()==row.sex) {
                this.checked=true;
           }
        });
        $("#age").val(row.age);
        $("#salary").val(row.salary);
        $("#phone").val(row.phoneNum);
        //下拉框回显
        if(row.area1!=null){
            $("#area1").val(row.area1);
            queryArea(row.area1, $("#area1"), 2);
        }
       if (row.area2!=null){
           $("#area2").val(row.area2);
           queryArea(row.area2, $("#area2"), 3);
       }
        if (row.area3!=null) {
            $("#area3").val(row.area3);
        }
        $.post({
            url:"/role/selectUserRole.do",
            data:{"id":row.id},
            async:false,
            success:function (data) {
                $(data.data).each(function () {
                    var id=this.id;
                    $(".resourceCheck").each(function () {
                    if (id == $(this).val()){
                        this.checked=true;
                    }
                })
            })
        }
        })

    }


    function queryUserRole() {
        $.ajaxSettings.async = false;
        $.post(
            "/role/queryUserRole.do",
            function (data) {
                if (data.status == 200) {
                    var str = "";
                    $(data.data).each(function () {
                        str += "<input type='checkbox' class='resourceCheck' value='" + this.id + "'><b>" + this.roleName +"&nbsp;&nbsp;</b>";
                    })
                    $("#resource").html(str)
                }
            }
        )
        $.ajaxSettings.async = true;
    }

    function getRole() {
        var ids = [];
        $(".resourceCheck").each(function () {
            if (this.checked) {
                ids.push($(this).val())
            }
        })
        return ids;
    }
</script>
<body>
<jsp:include page="/common/nav.jsp"></jsp:include>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <div class="panel panel-primary">
                <!-- Default panel contents -->
                <div class="panel-heading" style="text-align: left">
                    <button type="button" class="btn btn-success" onclick="toAddJsp()"><i
                            class="glyphicon glyphicon-plus"></i>添加用户
                    </button>
                    <button type="button" class="btn btn-danger" onclick="deleteBatch()"><i
                            class="glyphicon glyphicon-trash"></i>批量删除
                    </button>

                </div>

                <div class="panel-body">
                    <!-- Table -->
                    <table id="example" class="table table-striped table-bordered" style="width:100%">
                        <thead>
                        <tr>
                            <th><input type="checkbox" onclick="xuan()">选择</th>
                            <th>用户名</th>
                            <th>真实姓名</th>
                            <th>性别</th>
                            <th>年龄</th>
                            <th>地区</th>
                            <th>手机号</th>
                            <th>薪水</th>
                            <th>创建时间</th>
                            <th>操作</th>
                        </tr>
                        </thead>

                        <tfoot>
                        <tr>
                            <th><input type="checkbox" onclick="xuan()">选择</th>
                            <th>用户名</th>
                            <th>真实姓名</th>
                            <th>性别</th>
                            <th>年龄</th>
                            <th>地区</th>
                            <th>手机号</th>
                            <th>薪水</th>
                            <th>创建时间</th>
                            <th>操作</th>
                        </tr>
                        </tfoot>
                    </table>
                </div>
            </div>
        </div>

    </div>
</div>
<script type="text/html" id="showAddUserDiv">
    <form class="form-horizontal" id="formUser">
        <div class="form-group">
            <label class="col-sm-4 control-label">用户名</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" name="userName" id="userName" placeholder="用户名">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">真实姓名</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" name="roleName" id="roleName" placeholder="真实姓名">
            </div>
        </div>
        <div class="form-group" name="pass">
            <label class="col-sm-4 control-label">密码</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" name="passWord" id="passWord" placeholder="密码">
            </div>
        </div>
        <div class="form-group" name="pass">
            <label class="col-sm-4 control-label">确认密码</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" name="rePassWord" placeholder="确认密码">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">性别</label>
            <div class="col-sm-4">
                <input type="radio" class="sex" name="sex" value="1">男
                <input type="radio" class="sex" name="sex" value="2">女
            </div>
        </div>
        <div class="form-group" id="areas">
            <label class="col-sm-4 control-label">地区选择</label>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">年龄</label>
            <div class="col-sm-4">
                <input class="form-control" name="age" id="age">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">薪水</label>
            <div class="col-sm-4">
                <input class="form-control" name="salary" id="salary">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">手机号</label>
            <div class="col-sm-4">
                <input class="form-control" name="phoneNum" id="phone">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">分配权限</label>
            <div class="col-sm-4" id="resource">

            </div>
        </div>
        <input type="hidden" id="userId">
        <div class="form-group">
            <div class="col-sm-offset-4 col-sm-8">
                <button type="reset" class="btn btn-default"><i class="glyphicon glyphicon-repeat"></i>重置</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            </div>
        </div>
    </form>
</script>
<script>
    function initUserNameValidator() {
        $("#formUser").bootstrapValidator({
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
                        notEmpty: {message: '用户名不能为空'},
                        remote: {   //ajax验证。  result:{"valid",true or false}
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
                        notEmpty: {message: '密码不能为空'}
                    }
                }, phoneNum: {
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
                        notEmpty: {message: '密码不能为空'},
                        identical: {
                            field: 'passWord',
                            message: '两次密码输入不一致'
                        }
                    }
                }, roleName: {
                    validators: {
                        regexp: {
                            regexp: /^[\u4e00-\u9fa5]+$/,
                            message: '真实姓名为汉字'
                        },
                        notEmpty: {message: '真实姓名不能为空'},
                    }
                }, age: {
                    validators: {
                        regexp: {
                            regexp: /^(?:[1-9][0-9]?|1[01][0-9]|120)$/,
                            message: '年龄在1-120有效'
                        },
                        notEmpty: {message: '年龄不能为空'}
                    }
                },
            }
        });
    }
</script>
</body>
</html>
