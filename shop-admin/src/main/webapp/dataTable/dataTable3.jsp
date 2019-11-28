<%--
  Created by IntelliJ IDEA.
  User: gy
  Date: 2019/10/13
  Time: 14:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>

    <!-- jQuery (Bootstrap 的所有 JavaScript 插件都依赖 jQuery，所以必须放在前边) -->
    <script src="<%=request.getContextPath() %>/js/jquery-3.3.1.min.js"></script>
    <!-- 加载 Bootstrap 的所有 JavaScript 插件。你也可以根据需要只加载单个插件。 -->
    <script src="<%=request.getContextPath() %>/js/bootstrap3/js/bootstrap.min.js"></script>
    <script src="<%=request.getContextPath() %>/js/DataTables/DataTables-1.10.18/js/jquery.dataTables.min.js"></script>

    <script src="<%=request.getContextPath() %>/js/DataTables/DataTables-1.10.18/js/dataTables.bootstrap.min.js"></script>
    <script src="<%=request.getContextPath() %>/js/bootstrap-datetimepicker/js/moment-with-locales.js"></script>

    <script src="<%=request.getContextPath() %>/js/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
    <script src="<%=request.getContextPath() %>/js/bootbox/bootbox.min.js"></script>

    <link href="<%=request.getContextPath() %>/js/bootstrap3/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%=request.getContextPath() %>/js/DataTables/DataTables-1.10.18/css/dataTables.bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath() %>/js/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css" rel="stylesheet" type="text/css">

</head>
<script type="text/javascript">
    $(function () {
        initBrandList();
        initDate();
        initDateTable();
        initAddBrandList();
        initAddDate();
    })
function initDate() {
        $('#minDate').datetimepicker({
           format:"YYYY-MM-DD",
            locale: 'zh-CN',
            showClear: true


        });
        $('#maxDate').datetimepicker({
            format:"YYYY-MM-DD",
            locale: 'zh-CN',
            showClear: true


        });
    }
function  initBrandList() {
        $.post(
            "<%=request.getContextPath()%>/getBrandList.do",
            function (data) {
                if(data.length>0){
                    for (var i = 0; i < data.length; i++) {
                       $("#brandId").append(
                           "<option value='"+data[i].id+"'>"+data[i].name+"</option>"
                       );

                    }
                }
            }
        )
    }
function deleteProduct(id){
        bootbox.confirm({
            title:"提示信息",
            message:"您确认要删除吗？",
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
                    $.post(
                        "<%=request.getContextPath() %>/deleteProduct.do",
                        {"id":id},
                         function(data) {
                            if(data.status==200){
                                search();
                            }
                         }
                    );
                }
            }
        })
}
function search() {
        var param={};
        var productName = $("#productName").val();
        var brandId = $("#brandId").val();
        var minPrice =  $("#minPrice").val();
        var maxPrice =  $("#maxPrice").val();
        var minDate =  $("#minDate").val();
        var maxDate =  $("#maxDate").val();
        param.productName = productName;
        param.brandId = brandId;
        param.minPrice = minPrice;
        param.maxPrice = maxPrice;
        param.minDate = minDate;
        param.maxDate = maxDate;
    productTable.settings()[0].ajax.data=param;
    productTable.ajax.reload();

}
    var productTable;
function initDateTable(){
    productTable =$('#example').DataTable({
            // 是否允许检索
            "searching": false,
            "lengthMenu": [5, 10, 50],
            language: {
                "sProcessing": "处理中...",
                "sLengthMenu": "显示 _MENU_ 项结果",
                "sZeroRecords": "没有匹配结果",
                "sInfo": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
                "sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",
                "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
                "sInfoPostFix": "",
                "sSearch": "搜索:",
                "sUrl": "",
                "sEmptyTable": "表中数据为空",
                "sLoadingRecords": "载入中...",
                "sInfoThousands": ",",
                "oPaginate": {
                    "sFirst": "首页",
                    "sPrevious": "上页",
                    "sNext": "下页",
                    "sLast": "末页"
                },
                "oAria": {
                    "sSortAscending": ": 以升序排列此列",
                    "sSortDescending": ": 以降序排列此列"
                }
            },
            serverSide: true,
            ajax: {
                url: '/queryList.do',
                type: 'POST'
            },
            "columns": [
                { "data": "id",
                   "render":function (data,type,row,meta) {
                    return "<input type='checkbox' name='ids' value='"+data+"'>";
                } },
                { "data": "name" },
                { "data": "price" },
                { "data": "createDate" ,
                    "render":function (aa,type,row,meta) {
                        //return new Date(aa).getFullYear()+"-"+new Date(aa).getMonth()+"-"+new Date(aa).getDate();
                        return new Date(aa).toLocaleDateString();
                        // return  new Date(aa).Format("yyyy-MM-dd")
                    }},
                { "data": "brandId" },
                { "data": "id" ,"render":function (data,type,row,meta) {
                    return '<div class="btn-group" role="group" aria-label="...">'+
                        '<button type="button" class="btn btn-info" ><i class="glyphicon glyphicon-wrench"></i>修改</button>'+
                        '<button type="button" class="btn btn-danger" onclick="deleteProduct('+data+')"><i class="glyphicon glyphicon-remove"></i>删除</button>'+
                        '</div>';
                }}
            ]

        });

}
function initAddBrandList() {
    $.post(
        "<%=request.getContextPath()%>/getBrandList.do",
        function (data) {
            if(data.length>0){
                for (var i = 0; i < data.length; i++) {
                    $("#add_brand").append(
                        "<option value='"+data[i].id+"'>"+data[i].name+"</option>"
                    );

                }
            }
        }
    )
}
function initAddDate(){
    $('#add_createDate').datetimepicker({
        format:"YYYY-MM-DD",
        showClear: true


    });
}
function addProduct() {
    bootbox.dialog({
         title:"添加商品",
         message: $("#addProdcut form"),
         buttons: {
             confirm: {
                 label: '确认',
                 className: 'btn-success',
                 callback:function(){
                  var v_add_productName=$("#add_productName").val();
                  var v_add_price=$("#add_price").val();
                  var v_add_brandId=$("#add_brand").val();
                  var v_add_createDate=$("#add_createDate").val();
                  var product={};
                  product.name=v_add_productName;
                  product.price=v_add_price;
                  product.brandId=v_add_brandId;
                  product.createDate=v_add_createDate;
                  $.post(
                      "<%=request.getContextPath() %>/addProduct.do",
                      product,
                      function(data){
                          if(data.status==200){
                              search();
                          }else{
                              bootbox.alert({
                                  title:"提示信息",
                                  message:"操作错误"
                              })
                          }
                      }
                  )
                 }
            },
             cancel: {
                 label: '取消',
                 className: 'btn-danger'
             }
         }
     });



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
            <ul class="nav navbar-nav">
                <li class="active"><a href="#">万元高薪 <span class="sr-only">dgrsr</span></a></li>
                <li><a href="#">实战教学</a></li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">java课程 <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="#">大数据</a></li>
                        <li><a href="#">云计算</a></li>
                        <li><a href="#">云服务</a></li>
                        <li role="separator" class="divider"></li>
                        <li><a href="#">测试</a></li>
                        <li role="separator" class="divider"></li>
                        <li><a href="#">人工智能</a></li>
                    </ul>
                </li>
            </ul>


        </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
</nav>
<div class="container-fluid">
    <div class="row">
        <div class="col-md-3">
            <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                <div class="panel panel-primary">
                    <div class="panel-heading" role="tab" id="headingOne">
                        <h4 class="panel-title">
                            <a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo" aria-expanded="true" aria-controls="collapseOne">
                               产品管理
                            </a>
                        </h4>
                    </div>
                    <div id="collapseOne" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingOne2343245">
                        <div class="panel-body">
                            <ul class="nav nav-pills nav-stacked">
                                <li role="presentation" ><a href="#"><span class="glyphicon glyphicon-th-list"></span>产品列表</a></li>
                                <li role="presentation"><a href="#"><i class="glyphicon glyphicon-plus"></i>增加产品</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="panel panel-primary">
                    <div class="panel-heading" role="tab" id="headingTwo">
                        <h4 class="panel-title">
                            <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
                               用户管理
                            </a>
                        </h4>
                    </div>
                    <div id="collapseTwo" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingTwo">
                        <div class="panel-body">
                            <ul class="nav nav-pills nav-stacked">
                                <li role="presentation" ><a href="#"><span class="glyphicon glyphicon-user"></span>用户列表</a></li>
                                <li role="presentation"><a href="#"><i class="glyphicon glyphicon-plus"></i>增加用户</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="panel panel-default">
                    <div class="panel-heading" role="tab" id="headingThree">
                        <h4 class="panel-title">
                            <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseThree" aria-expanded="false" aria-controls="collapseThree">
                               菜单管理
                            </a>
                        </h4>
                    </div>
                    <div id="collapseThree" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingThree">
                        <div class="panel-body">
                            <ul class="nav nav-pills nav-stacked">
                                <li role="presentation" ><a href="#"><span class="glyphicon glyphicon-tree-deciduous"></span>权限树</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>


        </div>
        <div class="col-md-9">
            <form class="form-horizontal">
                <div class="form-group">
                    <label for="productName" class="col-sm-2 control-label">商品名称</label>
                    <div class="col-sm-3">
                        <input type="email" class="form-control" id="productName" placeholder="商品名称">
                    </div>
                    <label for="brandId" class="col-sm-2 control-label">商品品牌</label>
                    <div class="col-sm-3">
                        <select class="form-control" id="brandId">
                            <option value="-1">==请选择==</option>

                        </select>
                    </div>
                    <div class="col-md-2"></div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">价格</label>
                    <div class="col-sm-3">
                        <div class="input-group">
                            <input type="text" class="form-control" id="minPrice" placeholder="0.00" aria-describedby="basic-addon1">
                            <span class="input-group-addon" id="basic-addon1"><i class="glyphicon glyphicon-resize-horizontal"></i></span>
                            <input type="text" class="form-control" id="maxPrice" placeholder="0.00" aria-describedby="basic-addon1">
                        </div>
                    </div>
                    <label  class="col-sm-2 control-label">创建日期</label>
                    <div class="col-sm-3">
                        <div class="input-group">
                            <input type="text" id="minDate" class="form-control" placeholder="" aria-describedby="basic-addon1">
                            <span class="input-group-addon" id="basic-addon2"><i class="glyphicon glyphicon-calendar"></i></span>
                            <input type="text" id="maxDate" class="form-control" placeholder="" aria-describedby="basic-addon1">
                        </div>
                    </div>
                    <div class="col-md-2"></div>
                </div>


                    <div style="text-align: center">
                        <button type="button" class="btn btn-primary " onclick="search()"><i class="glyphicon glyphicon-ok"></i>Submit</button>&nbsp;&nbsp;&nbsp;
                        <button type="reset" class="btn btn-default "><i class="glyphicon glyphicon-repeat"></i>Reset</button>
                    </div>

            </form>


            <div class="panel panel-primary" >
                <!-- Default panel contents -->
                <div class="panel-heading" style="text-align: left">
                    <button type="button" class="btn btn-success" onclick="addProduct()"><i class="glyphicon glyphicon-plus"></i>增加商品</button>
                    <button type="button" class="btn btn-danger" ><i class="glyphicon glyphicon-trash"></i>批量删除</button>
                </div>
               <div  class="panel-body">
                <!-- Table -->
                   <table id="example" class="table table-striped table-bordered" style="width:100%">
                       <thead>
                       <tr>
                           <th><input type="checkbox" >全选</th>
                           <th>商品名称</th>
                           <th>商品价格</th>
                           <th>生产日期</th>
                           <th>商品品牌</th>
                           <th>操作</th>
                       </tr>
                       </thead>

                       <tfoot>
                       <tr>
                           <th><input type="checkbox" >全选</th>
                           <th>商品名称</th>
                           <th>商品价格</th>
                           <th>生产日期</th>
                           <th>商品品牌</th>
                           <th>操作</th>
                       </tr>
                       </tfoot>
                   </table>
               </div>
            </div>

        </div>

    </div>


</div>
<div id="addProdcut" style="display: none">
    <form class="form-horizontal">
        <div class="form-group ">
            <label for="add_productName" class="col-sm-2 control-label">商品名称</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="add_productName" placeholder="请输入商品名称">
            </div>
        </div>
        <div class="form-group">
            <label for="add_price" class="col-sm-2 control-label">商品价格</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="add_price" placeholder="请输入商品价格">
            </div>
        </div>
        <div class="form-group">
            <label for="add_brand" class="col-sm-2 control-label">商品品牌</label>
            <div class="col-sm-4">
                <select class="form-control" id="add_brand">
                    <option value="-1">==请选择==</option>

                </select>
            </div>
        </div>
        <div class="form-group">
            <label for="add_createDate" class="col-sm-2 control-label">生产日期</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="add_createDate" placeholder="请输入生产日期" aria-describedby="basic-addon1">
            </div>
        </div>


    </form>
</div>
</body>
</html>
