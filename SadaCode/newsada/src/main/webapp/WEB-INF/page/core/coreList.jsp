<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    request.setAttribute("basePath", basePath);
    %>
        <script src="${basePath}/common/adminlte/plugins/bootstrap-treeview/bootstrap-treeview.min.js"></script>
        <section class="content-header">
            <h1>鉴权中心</h1>
            <ol class="breadcrumb">
                <li>
                    <a href="${basePath}">
                        <i class="fa fa-dashboard"></i>首页</a>
                </li>
                <li class="active">鉴权中心</li>
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
                            <input placeholder="请输入姓名" name="name" class="form-control" type="search" likeOption="true" />
                            <input type="hidden" id="orgId" name="orgId" value="${user.orgId}" />
                            <div class="btn-group">
                                <button type="button" class="btn btn-primary" data-btn-type="search">查询</button>
                            </div>
                        </div>
                        <div class="box-body">
                            <table id="orgUser_table" class="table table-bordered table-striped table-hover">
                            </table>
                        </div>
                        <!-- /.box-body -->
                    </div>
                </div>
                <div class="col-md-4">
                    <!-- 组织用户资产树-->
                    <div class="box box-primary">
                        <!-- /.box-header -->
                        <div class="dataTables_filter" style="margin:10px 10px 0 0" id="searchDiv_orgUserFunc">
                            <input type="hidden" name="id" value="-1" id="orgUserId" />
                            <input placeholder="请输入资产名" name="v" id="assetName" class="form-control" type="search" likeOption="true" />
                            <div class="btn-group">
                                <button type="button" class="btn btn-default" data-btn-type="searchAsset">查询</button>
                                <button type="button" class="btn btn-primary" data-btn-type="authentication">鉴权</button>
                            </div>
                        </div>
                        <div class="box-body">
                            <div id="rftree"></div>
                        </div>
                        <!-- /.box-body -->
                    </div>
                    <!-- /.box -->
                </div>
            </div>
            <!-- /.row -->


        </section>

        <script>
            var orgUserTable;
            var winId = "orgUserWin";
            //通过组织用户id加载该用户下的所有资产树
            function reloadTreeData() {
                var selectNodeId = 0;
                var treeData = null;
                var rowId = orgUserTable.getSelectedRowId();
                if (!rowId)
                    return;
                ajaxPost(basePath + "/assetAction_listTreeByUser.do?id=" + rowId, null, function (data) {
                    treeData = data;
                });
                $("#rftree").treeview({
                    data: treeData,
                    showBorder: true,
                    expandIcon: "glyphicon glyphicon-chevron-right",
                    collapseIcon: "glyphicon glyphicon-chevron-down",
                    levels: 2,
                    showCheckbox: false,
                    showTags: true,
                    showIcon: true
                });
                if (treeData.length == 0)
                    return;
                //默认选中第一个节点
                selectNodeId = selectNodeId || 0;
                $("#rftree").data('treeview').selectNode(selectNodeId); //选中第一个节点
                $("#rftree").data('treeview').expandNode(selectNodeId); //展开第一个节点
                $("#rftree").data('treeview').revealNode(selectNodeId);
            }

            //搜索组织用户
            function searchAsset() {
                $('#rftree').treeview('search', [$("#assetName").val(), {
                    ignoreCase: true, // case insensitive
                    exactMatch: false, // like or equals
                    revealResults: true, // reveal matching nodes
                }], 'name');
            }

            $(function () {
                //初始化数据
                var orgUser_config = {
                    //数据加载后，默认选中第一行
                    loadComplete: function () {
                        orgUserTable.selectFirstRow(true);
                    },
                    //定义选中行事件
                    rowClick: function (row, isSelected) {
                        $("#orgUserId").val(isSelected ? row.id : "-1");
                        $("#orgUserName").remove();
                        if (isSelected) {
                            $("#searchDiv_orgUserFunc").prepend(
                                "<h5 id='orgUserName' class='pull-left'>&nbsp;&nbsp;&nbsp;[" +
                                row.nickName + "]</h5>");
                            //初始化组织用户资产树
                            reloadTreeData();
                        }
                    }
                }

                //初始化组织用户表
                orgUserTable = new CommonTable("orgUser_table", "orgUser_list", "searchDiv", orgUser_config);
                //按钮点击事件
                $('button[data-btn-type]').click(function () {
                    var action = $(this).attr('data-btn-type');
                    var selectedArr = $("#rftree").data("treeview").getSelected();
                    var selectedNode = selectedArr.length > 0 ? selectedArr[0] : null;
                    switch (action) {
                        case 'authentication':
                            if (!selectedNode) {
                                toastr.warning('请先选择要鉴权的资产');
                                return false;
                            }
                            if (selectedNode.parentId == undefined) {
                                toastr.warning('请继续选择' + selectedNode.text + '类别下的资产');
                                return false;
                            }
                            var coreAuthWin = "coreAuthWin";
                            modals.openWin({
                                winId: coreAuthWin,
                                title: '资产鉴权',
                                width: '600px',
                                url: basePath + "/assetAction_auth.do?id=" + selectedNode.id
                            });
                            break;
                        case 'searchAsset':
                            searchAsset();
                            break;
                        default:
                            break;
                    }
                });
            })
        </script>