<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    request.setAttribute("basePath", basePath);
    %>
    <section class="content-header">
        <h1>
            管理员管理
            <small>列表</small>
        </h1>
        <ol class="breadcrumb">
            <li>
                <a href="${basePath}">
                    <i class="fa fa-dashboard"></i>控制台</a>
            </li>
            <li>
                <a href="#">系统管理</a>
            </li>
            <li class="active">管理员管理</li>
        </ol>
    </section>

    <!-- Main content -->
    <section class="content">
        <div class="row">
            <div class="col-xs-12">
                <div class="nav-tabs-custom">
                    <ul class="nav nav-tabs pull-right">
                        <li>
                            <a href="#tab-content-edit" data-toggle="tab" id="nav-tab-edit">
                                <i class="fa fa-edit"></i>
                            </a>
                        </li>
                        <li class="active">
                            <a href="#tab-content-list" data-toggle="tab" id="nav-tab-list">
                                <i class="fa fa-list-ul"></i>
                            </a>
                        </li>
                        <li class="pull-left header">
                            <i class="fa fa-user"></i>
                            <small>管理员列表</small>
                        </li>
                    </ul>
                    <div class="tab-content">
                        <div class="tab-pane active" id="tab-content-list">
                            <div class="box">
                                <!-- /.box-header -->
                                <div class="dataTables_filter" id="searchDiv">
                                    <input placeholder="请输入姓名" name="name" class="form-control" type="search" likeOption="true" />
                                    <input placeholder="请输入昵称" name="nickName" class="form-control" type="search" likeOption="true" />
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
                                    <table id="user_table" class="table table-border">
                                    </table>
                                </div>
                                <!-- /.box-body -->
                            </div>
                            <!-- /.box -->
                        </div>
                        <!-- /.tab-pane -->
                        <div class="tab-pane" id="tab-content-edit">
                            <div class="box box-info">
                                <form id="user-form" name="user-form" class="form-horizontal">
                                    <input type="hidden" name="id">
                                    <input type="hidden" id="adminIs" name="adminIs" value="1"> 
                                    <input type="hidden" name="deleted">
                                    <div class="box-body">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label for="name" class="col-sm-3 control-label">姓名</label>
                                                <div class="col-sm-8">
                                                    <input type="text" class="form-control" id="name" name="name" placeholder="姓名">
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label for="name" class="col-sm-3 control-label">昵称</label>
                                                <div class="col-sm-8">
                                                    <input type="text" class="form-control" id="nickName" name="nickName" placeholder="昵称">
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label for="name" class="col-sm-3 control-label">密码</label>
                                                <div class="col-sm-8">
                                                    <input type="password" class="form-control" id="password" name="password" placeholder="密码">
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label for="birthday" class="col-sm-3 control-label">出生日期</label>
                                                <div class="input-group date col-sm-8">
                                                    <div class="input-group-addon">
                                                        <i class="fa fa-calendar"></i>
                                                    </div>
                                                    <input type="text" class="form-control" data-flag="datepicker" data-format="YYYY-MM-DD" id="dbirthday" name="dbirthday" placeholder="出生日期">
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-sm-3 control-label">性别</label>
                                                <div class="col-sm-8">
                                                    <label class="control-label">
                                                        <input type="radio" name="sexCode" data-flag="icheck" class="square-green" value="MALE"> 男
                                                    </label> &nbsp;
                                                    <label class="control-label">
                                                        <input type="radio" name="sexCode" data-flag="icheck" class="square-green" value="FEMALE"> 女
                                                    </label>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label for="telphone" class="col-sm-3 control-label">手机</label>
                                                <div class="col-sm-8">
                                                    <input type="text" class="form-control" id="mobile" name="mobile" placeholder="手机">
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label for="email" class="col-sm-3 control-label">邮箱</label>
                                                <div class="col-sm-8">
                                                    <input type="text" class="form-control" id="email" name="email" placeholder="邮箱">
                                                </div>
                                            </div> 
                                            <div class="form-group">
                                                <label for="roleName" class="col-sm-3 control-label">角色</label>
                                                <div class="col-sm-8">
                                                    <select name="roleName" id="roleName" data-placeholder="请选择角色" class="form-control fromselect">

                                                    </select>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-sm-3 control-label">状态</label>
                                                <div class="col-sm-8">
                                                    <label class="control-label">
                                                        <input type="radio" name="deleted" data-flag="icheck" class="square-green" value="0">启用
                                                    </label> &nbsp;
                                                    <label class="control-label">
                                                        <input type="radio" name="deleted" data-flag="icheck" class="square-green" value="1">禁用
                                                    </label>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- /.box-body -->
                                    <div class="box-footer text-right" style="height:50px;">
                                        <!--以下两种方式提交验证,根据所需选择-->
                                        <button type="button" class="btn btn-default" data-btn-type="cancel">取消</button>
                                        <button type="submit" class="btn btn-primary" data-btn-type="save">提交</button>
                                    </div>
                                    <!-- /.box-footer -->
                                </form>
                            </div>
                            <!-- /.box -->
                        </div>
                        <!-- /.tab-pane -->
                    </div>
                    <!-- /.tab-content -->
                </div>
                <!-- nav-tabs-custom -->
            </div>
            <!-- /.col -->
        </div>
        <!-- /.row -->
    </section>
    <script type="text/javascript" src="${basePath}/common/js/base-form.js"></script>
     <!-- dataTable -->
     <script src="${basePath}/common/adminlte/plugins/datatables/jquery.dataTables.js"></script>
     <script src="${basePath}/common/adminlte/plugins/datatables/dataTables.bootstrap.min.js"></script>
     <script src="${basePath}/common/js/dataTables.js"></script>
     <!-- form -->
     <script src="${basePath}/common/adminlte/plugins/bootstrap-validator/dist/js/bootstrap-validator.js"></script>
     <script src="${basePath}/common/adminlte/plugins/iCheck/icheck.min.js"></script>

     <script src="${basePath}/common/adminlte/plugins/datepicker/bootstrap-datepicker.js"></script>
     <script src="${basePath}/common/adminlte/plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js"></script>
     <!-- AdminLTE for demo purposes -->
     <script src="${basePath}/common/adminlte/dist/js/demo.js"></script>
     <!--select2-->
     <script src="${basePath}/common/adminlte/plugins/select2/select2.full.min.js"></script>
     <!--toastr-->
     <script src="${basePath}/common/libs/toastr/toastr.min.js"></script>
    <script type="text/javascript">
        var userTable;
        var winId = "userWin";
        var form = null;
        $(function () {

            form = $("#user-form").form();
            //init table and fill data
            userTable = new CommonTable("user_table", "admin_list", "searchDiv");

            $("#user-form").bootstrapValidator({
                message: '请输入有效值',
                feedbackIcons: {
                    valid: 'glyphicon glyphicon-ok',
                    invalid: 'glyphicon glyphicon-remove',
                    validating: 'glyphicon glyphicon-refresh'
                },
                submitHandler: function (validator, userform, submitButton) {
                    modals.confirm('确认保存？', function () {
                        //Save Data，对应'submit-提交'
                        var params = form.getFormSimpleData();
                        ajaxPost(basePath + '/userAction_save.do', params, function (data,
                            status) {
                            if (data.success) {
                                toastr.success("保存成功");
                                if (userTable.getSelectedRowId()) { //更新
                                    userTable.reloadRowData(userTable.getSelectedRowId());
                                } else { //新增
                                    userTable.reloadData();
                                }
                                //清除表单
                                form.clearForm();
                                $("#nav-tab-list").click();
                            }
                        });
                    });
                },
                fields: {
                    name: {
                        validators: {
                            notEmpty: {
                                message: '请输入姓名'
                            }
                        }
                    },
                    nickName: {
                        validators: {
                            notEmpty: {
                                message: '请输入昵称'
                            },
                            remote: {
                                url: basePath + "/userAction_checkNick.do", //验证的服务器地址
                                //自定义提交数据，默认值提交当前input value
                                data: function (validator) {
                                    return {
                                        nickName: $('#nickName').val(),
                                        userId: userTable.getSelectedRowId()
                                    };
                                },
                                message: '该昵称已被使用', //提示消息
                                //每输入一个字符，就发ajax请求，服务器压力还是太大，
                                //设置2秒发送一次ajax（默认输入一个字符，提交一次，服务器压力太大）
                                delay: 2000,
                                type: 'POST' //请求方式
                            }

                        }
                    },
                    password: {
                        validators: {
                            notEmpty: {
                                message: '请输入密码'
                            }
                        }
                    },
                    dbirthday: {
                        validators: {
                            notEmpty: {
                                message: '请输入出生日期'
                            },
                            date: {
                                format: $(this).data("format"),
                                message: '请输入有效日期'
                            }
                        }
                    },
                    sexCode: {
                        validators: {
                            notEmpty: {
                                message: '请选择性别'
                            }
                        }
                    },
                    moblie: {
                        validators: {
                            notEmpty: {
                                message: '请输入手机号'
                            }
                        }
                    },
                    role: {
                        validators: {
                            notEmpty: {
                                message: '请选择角色'
                            }
                        }
                    },
                    email: {
                        validators: {
                            notEmpty: {
                                message: '请输入邮件',
                            },
                            emailAddress: {
                                message: '非法的邮件格式',
                            }

                        }
                    },
                    deleted: {
                        validators: {
                            notEmpty: {
                                message: '请选择状态'
                            }
                        }
                    }

                }
            });

            //when user click nav-tab-list tab, then set default title
            $("#nav-tab-list").on("click", function () {
                setTitle("管理员列表");
            })

            //根据当前用户的角色动态加载角色下拉框
            ajaxPost(basePath + "/userAction_getUserRole.do", null, function (
                data) {
                var content = "";
                for (var i = 0; i < data.length; i++) {
                    content += "<option value='" + data[i].code + "'>" + data[i].name + "</option>"
                }
                $("#roleName").append(content);
            });

            form.initComponent();

            $('button[data-btn-type]').click(function () {
                var action = $(this).attr('data-btn-type');
                var rowId = userTable.getSelectedRowId();
                //根据当前登录管理员角色，判断是否有操作选中用户的权限
                var comp = null;
                ajaxPost(basePath + "/userAction_compRole.do", {
                    id: rowId
                }, function (data) {
                    comp = data;
                });
                switch (action) {
                    case 'add':
                        form.clearForm();
                        setTitle("新增管理员");
                        $("#nav-tab-edit").click();
                        break;
                    case 'edit':
                        if (!rowId) {
                            toastr.warning("请选择要编辑的行");
                            return false;
                        }
                        if (!comp.success) {
                            toastr.warning("权限不够");
                            return false;
                        }

                        form.clearForm();
                        setTitle("编辑管理员【" + userTable.getSelectedRowData().name + "】")
                        $("#nav-tab-edit").click();
                        ajaxPost(basePath + "/userAction_getById.do", {
                            id: rowId
                        }, function (data) {
                            form.initFormData(data);
                        });
                        break;
                    case 'delete':
                        if (!rowId) {
                            toastr.warning("请选择要删除的行");
                            return false;
                        }
                        if (!comp.success) {
                            toastr.warning("权限不够");
                            return false;
                        }
                        modals.confirm("是否要删除该行数据？", function () {
                            ajaxPost(basePath + "/userAction_del.do?id=" + rowId, null,
                                function (
                                    data) {
                                    if (data.success) {
                                        toastr.success("已删除该数据");
                                        userTable.reloadRowData();
                                    } else {
                                        toastr.error("用户数据被引用，不可删除！");
                                    }
                                });
                        })
                        break;
                    case 'cancel':
                        //清除表单
                        form.clearForm();
                        $("#nav-tab-list").click();
                        break;
                }

            });
        });

        //set title for current tab
        function setTitle(title) {
            $("ul.nav-tabs li.header small").text(title);
        }
    </script>