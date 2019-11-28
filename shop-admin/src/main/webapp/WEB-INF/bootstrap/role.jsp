<%--
  Created by IntelliJ IDEA.
  User: zhang
  Date: 2019/11/10
  Time: 11:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>角色维护</title>
    <jsp:include page="/common/script.jsp"></jsp:include>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/ztree/js/jquery.ztree.core.min.js"></script>
    <script type="text/javascript" src="/js/ztree/js/jquery.ztree.excheck.min.js"></script>
    <link href="<%=request.getContextPath()%>/js/ztree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet" type="text/css">
</head>
<script type="text/javascript">
    var zTreeObj;
    var setting = {
        data: {
            key: {
                name:"resourceName",
                url:"rurl"
            },
            simpleData: {
                //enable:true   采用简单数据模式 (Array)
                enable: true,
                idKey: "id",
                pIdKey: "pid",
                rootPId: 0
            }
        },check: {
            enable: true,   //true / false 分别表示 显示 / 不显示 复选框或单选框
            autoCheckTrigger: true,   //true / false 分别表示 触发 / 不触发 事件回调函数
            chkStyle: "checkbox",   //勾选框类型(checkbox 或 radio）
            chkboxType: { "Y": "p", "N": "s" }   //勾选 checkbox 对于父子节点的关联关系
        },
        view: {
            dblClickExpand: true,
            selectedMulti : true,//可以多选
            showLine: true
        },
    };

    $(function () {
        initDateTable();
    })

    //pId:父节点的id
   function initztree(){
       //zTree 初始化方法
       //zTreeNodes  zTree 的节点数据，
       $.post(
           "<%=request.getContextPath()%>/role/queryAllResource.do",
           function(data){
               zTreeObj = $.fn.zTree.init($("#treeAdd"), setting, data.data);
               zTreeObj.expandAll(true);
           }
       )
   }

    //pId:父节点的id
   function initUpdateztree(id){
       //zTree 初始化方法
       //zTreeNodes  zTree 的节点数据，
       $.post(
           "/role/queryMyResource.do",
           {"id":id},
           function(data){
               zTreeObj = $.fn.zTree.init($("#treeAdd"), setting, data.data);
               zTreeObj.expandAll(true);
           }
       )
   }


    function toAddJsp() {

        bootbox.dialog({
            message: $("#showAddRoleDiv").html(),
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
                        //获取被选中的节点集合
                        var nodes = zTreeObj.getChangeCheckedNodes(true);
                        //将 zTree 使用的标准 JSON 嵌套格式的数据转换为简单 Array 格式。
                        //拼接要删除的id
                        var ids = [];
                        for (var i = 0; i < nodes.length; i++) {
                            //alert(nodesArr[i].id)
                            ids.push(nodes[i].id);
                        }
                        //获取角色名
                   var roleName= $("#roleName").val();
                        if (ids.length>0 && roleName!=""){
                            $.post(
                                "<%=request.getContextPath()%>/role/addRole.do",
                                {"idList":ids,"roleName":roleName},
                                function(data){
                                    if(data.status==200){
                                        zTreeObj.removeNode(nodes[0]);
                                        //重新加载table
                                        $("#example").dataTable().fnDraw(false);
                                    }else{
                                        bootbox.alert("操作错误,请联系管理员！");
                                    }
                                }
                            )
                        }else {
                            alert("请填写角色名字/赋权限")
                        }
                    }
                }
            }
        });
        initztree();
    }
    function toUpdate(id,name) {
        bootbox.dialog({
            message: $("#showAddRoleDiv").html(),
            title: "修改",
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
                        var treeObj = $.fn.zTree.getZTreeObj("treeAdd");
                        var nodes = treeObj.getCheckedNodes(true);
                        //将 zTree 使用的标准 JSON 嵌套格式的数据转换为简单 Array 格式。
                        //拼接要删除的id
                        var ids = [];
                        for (var i = 0; i < nodes.length; i++) {
                            //alert(nodesArr[i].id)
                            ids.push(nodes[i].id);
                        }
                        //获取角色名
                   var roleName= $("#roleName").val();
                        if (ids.length>0 && roleName!=""){
                            $.post(
                                "/role/updateRole.do",
                                {"idList":ids,"roleName":roleName,"id":id},
                                function(data){
                                    if(data.status==200){
                                        zTreeObj.removeNode(nodes[0]);
                                        //重新加载table
                                        $("#example").dataTable().fnDraw(false);
                                    }else{
                                        bootbox.alert("操作错误,请联系管理员！");
                                    }
                                }
                            )
                        }else {
                            alert("请填写角色名字/赋权限")
                        }

                    }
                }
            }
        });
        $("#roleName").val(name);
        initUpdateztree(id);
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
                            class="glyphicon glyphicon-plus"></i>添加角色
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
                            <th>角色Id</th>
                            <th>角色名称</th>
                            <th>操作</th>
                        </tr>
                        </thead>

                        <tfoot>
                        <tr>
                            <th><input type="checkbox" onclick="xuan()">选择</th>
                            <th>角色Id</th>
                            <th>角色名称</th>
                            <th>操作</th>
                        </tr>
                        </tfoot>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/html" id="showAddRoleDiv">
    <form class="form-horizontal" id="formUser">
        <div class="form-group">
            <label class="col-sm-4 control-label">角色名称</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" name="userName" id="roleName" placeholder="角色名称">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">分配权限</label>
            <div class="col-sm-4">
                <ul id="treeAdd" class="ztree" ></ul>
            </div>
        </div>
    </form>
</script>
</body>
<script>
    function initDateTable() {
        myTable = $('#example').DataTable({
            "serverSide": true,
            // 是否允许检索
            "searching": false,
            "lengthMenu": [5, 10, 20, 50],
            "ajax": {
                url: '/role/queryRoleList.do',
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
                {"data": "id"},
                {"data": "roleName"},
                {
                    "data": "id", render: function (data, type, row, meta) {

                        return ' <div class="btn-group" role="group" aria-label="...">' +
                            '<button type="button" class="btn btn-info" onclick="toUpdate('+data+',\''+row.roleName+'\')"><i class="glyphicon glyphicon-wrench"></i>修改</button>' +
                            '</div>';
                    }
                }

            ],
            "initComplete": function (setting, json) {

            },
            "drawCallback": function (settings) {

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
</script>
</html>
