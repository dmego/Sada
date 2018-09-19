<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    request.setAttribute("basePath", basePath);
    %>
    <script src="${basePath}/common/adminlte/plugins/bootstrap-treeview/bootstrap-treeview.min.js"></script>
    <section class="content-header">
        <h1>
            用户管理
            <small>列表</small>
        </h1>
        <ol class="breadcrumb">
            <li>
                <a href="${basePath}">
                    <i class="fa fa-dashboard"></i> 首页</a>
            </li>
            <li>
                <a href="#">系统管理</a>
            </li>
            <li class="active">用户管理</li>
        </ol>
    </section>
    
    <!-- Main content -->
    <section class="content">
        <div class="row">
            <div class="col-xs-12">
                <div class="box">
                    <!-- /.box-header -->
                    <div class="dataTables_filter" id="searchDiv">
                        <input placeholder="请输入姓名" name="name" class="form-control" type="search" likeOption="true" />
                        <input placeholder="请输入昵称" name="nickName" class="form-control" type="search" likeOption="true"
                        />
                        <div class="btn-group">
                            <button type="button" class="btn btn-primary" data-btn-type="search">查询</button>
                            <button type="button" class="btn btn-default" data-btn-type="reset">重置</button>
                        </div>
                        <div class="btn-group">
                            <button type="button" class="btn btn-default" data-btn-type="add">新增</button>
                            <button type="button" class="btn btn-default" data-btn-type="edit">编辑</button>
                            <button type="button" class="btn btn-default" data-btn-type="delete">删除</button>
                        </div>
                    </div>
                    <div class="box-body">
                        <table id="user_table" class="table table-bordered table-striped table-hover">
                        </table>
                    </div>
                    <!-- /.box-body -->
                </div>
            </div>
            <!-- /.col -->
        </div>
        <!-- /.row -->
    </section>



    <script>
        //tableId,queryId,conditionContainer
        var userTable;
        var winId = "userWin";
        $(function () {
            //init table and fill data 
            userTable = new CommonTable("user_table","user_list", "searchDiv");

            //button event
            $('button[data-btn-type]').click(function () {
                var action = $(this).attr('data-btn-type');
                var rowId = userTable.getSelectedRowId();
                switch (action) {
                    case 'add':   
                        window.loadPage(basePath + "/userAction_edit.do");
                        break;
                    case 'edit':
                        if (!rowId) {
                            toastr.warning("请选择要编辑的行");
                            return false;
                        }
                        window.loadPage(basePath + "/userAction_edit.do?id=" + rowId);
                        break;
                    case 'delete':
                        if (!rowId) {
                            toastr.warning("请选择要删除的行");
                            return false;
                        }
                        modals.confirm("是否要删除该行数据？", function () {
                            ajaxPost(basePath + "/userAction_del.do?id=" + rowId, null,
                                function (data) {
                                    if (data.success) {
                                        toastr.success("已删除该数据");
                                        userTable.reloadRowData();
                                    } else {
                                        toastr.error("用户数据被引用，不可删除！");
                                    }
                                });
                        })
                        break;
                }

            });
            //form_init();
        })
        
    </script>