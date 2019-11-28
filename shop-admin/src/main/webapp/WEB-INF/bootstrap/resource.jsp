<%--
  Created by IntelliJ IDEA.
  User: zhang
  Date: 2019/11/10
  Time: 11:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>资源维护</title>
    <jsp:include page="/common/script.jsp"></jsp:include>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/ztree/js/jquery.ztree.core.min.js"></script>
    <link href="<%=request.getContextPath()%>/js/ztree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet" type="text/css">
    <script type="text/html" id="addResource">
        <form class="form-horizontal">
            <div class="form-group ">
                <label for="addFatherName" class="col-sm-2 control-label">父节点:</label>
                <div class="col-sm-4">
                    <input type="text" class="form-control"  id="addFatherName" readonly="readonly">
                </div>
            </div>
            <div class="form-group ">
                <label for="addFatherName" class="col-sm-2 control-label">新增节点:</label>
                <div class="col-sm-4">
                    <input type="text" class="form-control"  id="addName" >
                </div>
            </div>
            <div class="form-group ">
                <label for="addFatherName" class="col-sm-2 control-label">新增Url:</label>
                <div class="col-sm-4">
                    <input type="text" class="form-control"  id="addUrl" >
                </div>
            </div>
            <div class="form-group ">
                <label for="addFatherName" class="col-sm-2 control-label">类型:</label>
                <div class="col-sm-4">
                    <div id="cai">
                        <input  type="radio" name="type" value="1">菜单
                    </div>
                    <div id="an">
                        <input  type="radio" name="type" value="2">按钮
                    </div>
                </div>
            </div>
            <%--父节点：<input type="text" class="form-control" id="addFatherName" readonly="readonly"><br>--%>
            <%--  新增节点：<input type="text" id="addName" ><br>--%>
            <input type="hidden" id="addFatherId" >

        </form>

    </script>
    <script type="text/html" id="updateResource">
        <form class="form-horizontal">
            <div class="form-group">
                <label  class="col-sm-2 control-label">父节点</label>
                <div class="col-sm-4">
                    <input type="text" class="form-control" id="updateFatherName" readonly="readonly">
                </div>
            </div>
            <div class="form-group">
                <label  class="col-sm-2 control-label">当前节点</label>
                <div class="col-sm-4">
                    <input type="text" class="form-control" id="updateName" >
                </div>
            </div>
            <div class="form-group">
                <label  class="col-sm-2 control-label">当前Url</label>
                <div class="col-sm-4">
                    <input type="text" class="form-control" id="updateUrl" >
                </div>
            </div>
            <input type="hidden" id="updateId"  >
            <input type="hidden" id="updateFatherId" >
        </form>

    </script>
</head>
<script type="text/javascript">
    var zTreeObj;
    var setting = {
        callback: {
            onClick: zTreeOnClick
        },
        data: {
            key: {
                name:"resourceName",
                url:"durl"
            },
            simpleData: {
                //enable:true   采用简单数据模式 (Array)
                enable: true,
                idKey: "id",
                pIdKey: "pid",
                rootPId: 0
            }
        }
    };
    //pId:父节点的id
    $(document).ready(function(){
        //zTree 初始化方法
        //zTreeNodes  zTree 的节点数据，
        $.post(
            "<%=request.getContextPath()%>/role/queryAllResource.do",
            function(data){
                zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, data.data);
                zTreeObj.expandAll(true);
            }
        )
    });
    function zTreeOnClick(event, treeId, treeNode){
        $("#caozuo").css("display","");
        if(treeNode.type==null){
            $("#z_del").css("display","none")
        }else {$("#z_del").css("display","")}
        if (treeNode.type==2) {
            $("#z_add").css("display","none")
        }else {
            $("#z_add").css("display","")
        }
    }
    function toUpdate(){
        bootbox.dialog({
            title:"修改分类",
            message: $("#updateResource").html(),
            buttons: {
                confirm: {
                    label: '确认',
                    className: 'btn-success',
                    callback:function(){
                        update();
                    }
                },
                cancel: {
                    label: '取消',
                    className: 'btn-danger'
                }
            }
        });
        //获取被选中的节点集合
        var nodes = zTreeObj.getSelectedNodes();
        //获取当前被选中的name值 放在input框
        $("#updateName").val(nodes[0].resourceName);
        $("#updateUrl").val(nodes[0].url);
        //获取当前节点的id 放在隐藏域中  方便修改
        $("#updateId").val(nodes[0].id);
        //获取当前节点的父节点
        var parentNode = nodes[0].getParentNode();
        //给父节点赋值
        $("#updateFatherName").val(parentNode.resourceName);
        $("#updateFatherId").val(parentNode.id);
    }
    function toAdd(){
        bootbox.dialog({
            title:"添加分类",
            message: $("#addResource").html(),
            buttons: {
                confirm: {
                    label: '确认',
                    className: 'btn-success',
                    callback:function(){
                        add();
                    }
                },
                cancel: {
                    label: '取消',
                    className: 'btn-danger'
                }
            }

        });
        //获取被选中的节点集合
        var nodes = zTreeObj.getSelectedNodes();
        //重置单选按钮
        $("#an").css("display","");$("#cai").css("display","")

        if (nodes[0].type==null)
        {$("#an").css("display","none");$("input:radio[value='1']").attr("checked","true");}
        if (nodes[0].type==1)
        {$("#cai").css("display","none");$("input:radio[value='2']").attr("checked","true");}

        //当前节点 即 新增节点的父节点
        $("#addFatherName").val(nodes[0].resourceName);
        $("#addFatherId").val(nodes[0].id);
    }
    function deleteNode(){
        bootbox.confirm({
            title:"提示信息",
            message:"您确认要删除吗?",
            buttons: {
                confirm: {
                    label: '<span class="glyphicon glyphicon-ok"></span>确定',
                    className: 'btn-success'
                },
                cancel: {
                    label: '<span class="glyphicon glyphicon-remove"></span>取消',
                    className: 'btn-danger'
                }
            },
            callback:function (result) {
                if(result){
                    //获取被选中的节点集合
                    var nodes = zTreeObj.getSelectedNodes();
                    //将 zTree 使用的标准 JSON 嵌套格式的数据转换为简单 Array 格式。
                    //(免去用户自行编写递归遍历全部节点的麻烦)
                    var nodesArr = zTreeObj.transformToArray(nodes[0]);
                    //拼接要删除的id
                    var ids = [];
                    for (var i = 0; i < nodesArr.length; i++) {
                        //alert(nodesArr[i].id)
                        ids.push(nodesArr[i].id);
                    }
                    if(ids.length>0){
                        $.post(
                            "<%=request.getContextPath()%>/role/deleteResource.do",
                            {"idList":ids},
                            function(data){
                                if(data.status==200){
                                    zTreeObj.removeNode(nodes[0]);
                                }else{
                                    bootbox.alert("操作错误,请联系管理员！");
                                }

                            }
                        )
                    }
                }
            }
        })
    }
    function add(){
        var pid= $("#addFatherId").val();
        var name= $("#addName").val();
        var url= $("#addUrl").val();
        var type= $("input[name='type']:checked").val();
        $.post(
            "<%=request.getContextPath()%>/role/addResource.do",
            {"resourceName":name,"pid":pid,"type":type,"url":url},
            function(data){
                if(data.status==200){
                    //获取被选中的节点集合
                    var nodes = zTreeObj.getSelectedNodes();
                    var newNodes = {"resourceName":name,"pid":pid,"id":data.id};
                    zTreeObj.addNodes(nodes[0], newNodes);
                    // $("#addFatherId").val();
                    // $("#addName").val("");
                }else{
                    bootbox.alert("操作错误,请联系管理员！");
                }


            }
        )


    }
    function update(){
        //获取修改之后的名字
        var newName = $("#updateName").val();
        var id = $("#updateId").val();
        var pid = $("#updateFatherId").val();
        var url = $("#updateUrl").val();
        //先更新数据库  在更新页面节点
        $.post(
            "<%=request.getContextPath()%>/role/updateResource.do",
            {"id":id,"resourceName":newName,"pid":pid,"url":url},
            function(data){
                if(data.status==200){
                    //获取被选中的节点集合
                    var nodes = zTreeObj.getSelectedNodes();
                    //给节点的属性重新赋值
                    nodes[0].resourceName=newName;
                    //更新节点
                    zTreeObj.updateNode(nodes[0]);
                }else {
                    bootbox.alert("操作错误,请联系管理员！");
                }

            }


        )
    }
</script>
<body>
<jsp:include page="/common/nav.jsp"></jsp:include>
<div class="container">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <ul id="treeDemo" class="ztree" ></ul>
            <div id="caozuo" style="display:none">
                <input value="修改" type="button"  class="btn btn-primary" onclick="toUpdate()" >
                <input value="增加" type="button" id="z_add" style="display: none" class="btn btn-warning" onclick="toAdd()">
                <input value="删除" type="button" id="z_del" class="btn btn-danger"  onclick="deleteNode()">
            </div>
        </div>
    </div>
</div>
</body>
</html>
