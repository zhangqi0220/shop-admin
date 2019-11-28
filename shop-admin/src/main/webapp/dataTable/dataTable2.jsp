<%--
  Created by IntelliJ IDEA.
  User: gy
  Date: 2019/10/14
  Time: 8:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <menu http-equiv="Content-Type" content="text/html; charset=UTF-8"></menu>
    <title>Title</title>


    <link href="<%=request.getContextPath() %>/js/bootstrap3/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath() %>/js/DataTables/DataTables-1.10.18/css/dataTables.bootstrap.min.css" rel="stylesheet" type="text/css">
    <script src="<%=request.getContextPath() %>/js/jquery-3.3.1.min.js"></script>
    <script src="<%=request.getContextPath() %>/js/DataTables/DataTables-1.10.18/js/jquery.dataTables.min.js"></script>

    <script src="<%=request.getContextPath() %>/js/DataTables/DataTables-1.10.18/js/dataTables.bootstrap.min.js"></script>


</head>
<script type="text/javascript">
    $(document).ready(function() {
        $('#example').DataTable({
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
                { "data": "name" },
                { "data": "price" },
                { "data": "createDate" ,
                   "render":function (aa,type,row,meta) {
                       //return new Date(aa).getFullYear()+"-"+new Date(aa).getMonth()+"-"+new Date(aa).getDate();
                       return new Date(aa).toString();
                   }},
                { "data": "brandId" },
                { "data": "id" ,"render":function (data,type,row,meta) {
                    return '<div class="btn-group" role="group" aria-label="...">'+
                    '<button type="button" class="btn btn-info"><i class="glyphicon glyphicon-wrench"></i>修改</button>'+
                        '<button type="button" class="btn btn-danger"><i class="glyphicon glyphicon-remove"></i>删除</button>'+
                        '</div>';
                }}
            ]

        });
    } );


</script>
<body>
<table id="example" class="table table-striped table-bordered" style="width:100%">
    <thead>
    <tr>
        <th>商品名称</th>
        <th>商品价格</th>
        <th>生产日期</th>
        <th>商品品牌</th>
        <th>操作</th>
    </tr>
    </thead>

    <tfoot>
    <tr>
        <th>商品名称</th>
        <th>商品价格</th>
        <th>生产日期</th>
        <th>商品品牌</th>
        <th>操作</th>
    </tr>
    </tfoot>
</table>
</body>
</html>
