<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    request.setAttribute("basePath", basePath);
    %>
    <script src="${basePath}/common/adminlte/plugins/bootstrap-treeview/bootstrap-treeview.min.js"></script>
    <section class="content-header">
        <h1>角色管理</h1>
        <ol class="breadcrumb">
            <li>
                <a href="${basePath}">
                    <i class="fa fa-dashboard"></i> 首页</a>
            </li>
            <li>
                <a href="#">系统管理</a>
            </li>
            <li class="active">角色管理</li>
        </ol>
    </section>

    <!-- Main content -->
    <section class="content">

        <div class="row">
            <!-- /.col -->
            <div class="col-md-8">
                <div class="box box-primary">
                    <!-- /.box-header -->
                    <div class="dataTables_filter" id="searchDiv">
                        <input placeholder="请输入名称" name="name" class="form-control" type="search" likeOption="true" />
                        <div class="btn-group">
                            <button type="button" class="btn btn-primary" data-btn-type="search">查询</button>
                        </div>
                        <div class="btn-group">
                            <button type="button" class="btn btn-default" data-btn-type="add">新增</button>
                            <button type="button" class="btn btn-default" data-btn-type="edit">编辑</button>
                            <button type="button" class="btn btn-default" data-btn-type="delete">删除</button>
                        </div>
                        <div class="btn-group">
                            <button type="button" class="btn btn-primary" data-btn-type="change">视角</button>
                        </div>
                    </div>
                    <div class="box-body">
                        <table id="role_table" class="table table-bordered table-striped table-hover">
                        </table>
                    </div>
                    <!-- /.box-body -->
                </div>
            </div>
            <div class="col-md-4">
                <!-- Profile Image -->
                <!-- 角色权限对应树-->
                <div class="box box-primary" id="functree" style="display: none">
                    <!-- /.box-header -->
                    <div class="dataTables_filter" style="margin:10px 10px 0 0" id="searchDiv_roleFunc">
                        <input type="hidden" name="id" value="-1" id="roleId1" />
                        <input placeholder="请输入权限名" name="v" id="authName" class="form-control" type="search" likeOption="true" />
                        <div class="btn-group">
                            <button type="button" class="btn btn-primary" data-btn-type="searchRoleAuth">查询</button>
                        </div>
                    </div>
                    <div class="box-body">
                        <div id="rftree"></div>
                    </div>
                    <!-- /.box-body -->
                </div>
                <!-- 用户角色对应表-->
                <div class="box box-primary" id="usertable">
                    <!-- /.box-header -->
                    <div class="dataTables_filter" id="searchDiv_userRole">
                        <input type="hidden" name="id" value="-1" id="roleId2" />
                    </div>
                    <div class="box-body">
                        <table id="userRole_table" class="table table-bordered table-striped table-hover">
                        </table>
                    </div>
                    <!-- /.box-body -->
                </div>
                <!-- /.box -->
            </div>
        </div>
        <!-- /.row -->

    </section>

    <script>
        var roleTable;
        var userRoleTable;
        var winId = "roleWin";

        //通过角色id加载角色权限树
        function reloadTreeData() {
            var selectNodeId = 0;
            var treeData = null;
            var rowId = roleTable.getSelectedRowId();
            if (!rowId)
                return;
            ajaxPost(basePath + "/roleAction_listTree.do?id=" + rowId, null, function (data) {
                treeData = data;
            });
            $("#rftree").treeview({
                data: treeData,
                showBorder: true,
                levels: 2,
                showCheckbox: false,
                showIcon: true
            });
            if (treeData.length == 0)
                return;
            //默认选中第一个节点
            selectNodeId = selectNodeId || 0;
            $("#rftree").data('treeview').selectNode(selectNodeId);
            $("#rftree").data('treeview').expandNode(selectNodeId);
            $("#rftree").data('treeview').revealNode(selectNodeId);
        }

        //搜索角色权限
        function searchRoleAuth() {
            $('#rftree').treeview('search', [$("#authName").val(), {
                ignoreCase: true, // case insensitive
                exactMatch: false, // like or equals
                revealResults: true, // reveal matching nodes
            }], 'name');
        }

        $(function () {
            //初始化数据
            var role_config = {
                //定义选中行事件
                rowClick: function (row, isSelected) {
                    $("#roleId1").val(isSelected ? row.id : "-1");
                    $("#roleName1").remove();
                    if (isSelected) {
                        $("#searchDiv_roleFunc").prepend("<h5 id='roleName1' class='pull-left'>【" + row
                            .name + "】</h5>");
                        //初始化权限树
                        reloadTreeData();
                    }

                    $("#roleId2").val(isSelected ? row.id : "-1");
                    $("#roleName2").remove();
                    if (isSelected) {
                        $("#searchDiv_userRole").prepend("<h5 id='roleName2' class='pull-left'>【" + row
                            .name + "】</h5>");
                        userRoleTable.reloadData();
                    }
                },
                
            }
            //初始化角色表
            roleTable = new CommonTable("role_table", "role_list", "searchDiv", role_config);
            var config = {
                lengthChange: false,
                pagingType: 'simple_numbers'
            }

            //初始化用户角色表
            userRoleTable = new CommonTable("userRole_table", "userRole_selected_list", "searchDiv_userRole",config);
            //默认选中第一行 
            setTimeout(function () {
                roleTable.selectFirstRow(true)
            }, 15);

            //初始化权限树
            reloadTreeData();

            //button event
            $('button[data-btn-type]').click(function () {
                var action = $(this).attr('data-btn-type');
                var rowId = roleTable.getSelectedRowId();
                //根据当前登录管理员角色，判断是否有操作选中角色的权限
                function compRole() {
                    var comp = null;
                    ajaxPost(basePath + "/roleAction_compRole.do", {
                        id: rowId
                    }, function (data) {
                        comp = data;
                    });
                    return comp;
                }
                //判断当前选中的角色下是否有用户
                function hasUser(){
                    var has = null;
                    ajaxPost(basePath + "/roleAction_hasUser.do", {
                        id: rowId
                    }, function (data) {
                        has = data;
                    });
                    return has;
                }

                switch (action) {
                    case 'add':
                        modals.openWin({
                            winId: winId,
                            title: '新增角色',
                            width: '600px',
                            url: basePath + "/roleAction_edit.do"
                        });
                        break;
                    case 'edit':
                        if (!rowId) {
                            toastr.warning("请选择要编辑的行");
                            return false;
                        }
                        if (!compRole().success) {
                            toastr.warning("权限不够");
                            return false;
                        }
                        modals.openWin({
                            winId: winId,
                            title: '编辑角色【' + roleTable.getSelectedRowData().name + '】',
                            width: '600px',
                            url: basePath + "/roleAction_edit.do?id=" + rowId
                        });
                        break;
                    case 'delete':
                        if (!rowId) {
                            toastr.warning("请选择要删除的行");
                            return false;
                        }
                        if (!compRole().success) {
                            toastr.warning("权限不够");
                            return false;
                        }
                        //判断该角色下是否存在用户
                        if(hasUser().success){
                            toastr.warning("该角色下存在用户，不可删除");
                            return false;
                        }

                        modals.confirm("是否要删除该行数据？", function () {
                            ajaxPost(basePath + "/roleAction_del.do?id=" + rowId, null, function (
                                data) {
                                if (data.success) {
                                    //TODO 判断该角色下是否存在用户
                                    toastr.success("已删除该数据");
                                    roleTable.reloadData();
                                } else {
                                    //setTimeout(function(){modals.info(data.message)},2000);
                                    modals.info(data.message);
                                    //toastr.error("该角色下存在用户，不可删除！");
                                }
                            });
                        })
                        break;
                    case 'change': //切换视角
                        var display = $('#functree').css('display');
                        if (display == 'none') {
                            $("#functree").show();
                            $("#usertable").hide();
                        } else {
                            $("#functree").hide();
                            $("#usertable").show();
                        }
                        break;
                    case 'deleteRoleFunc':
                        deleteRoleFunc(rowId);
                        break;
                    case 'searchRoleAuth':
                        searchRoleAuth(rowId);
                        break;
                    default:
                        break;
                }

            });
        })
    </script>