<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    request.setAttribute("basePath", basePath);
    %>
        <section class="content-header">
            <h1>
                <span>用户管理</span>
                <small>新增</small>
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
        <section class="content">
            <div class="row">
                <div class="col-xs-12">
                    <div class="box box-info">
                        <form id="user-form" name="user-form" class="form-horizontal">
                            <input type="hidden" name="id">
                            <input type="hidden" id="adminIs" name="adminIs" value="0">
                            <input type="hidden" name="deleted">
                            <div class="box-header">
                                <div class="col-xs-12 text-center">
                                    <div class="avatar-container text-center">
                                        <img src="${basePath}/common/images/avatar.jpg" id="avatarImg" class="avatar-img" />
                                    </div>
                                    <div>
                                        <button type="button" class="btn btn-sm btn-camera" data-btn-type="upload">
                                            <i class="fa fa-camera">&nbsp;上传/更改头像</i>
                                        </button>
                                    </div>
                                </div>
                            </div>
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
                                </div>

                                <div class="col-md-6">
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
                                        <label for="orgName" class="col-sm-3 control-label">组织单位</label>
                                        <div class="col-sm-6">
                                            <input type="hidden" name="orgId" id="orgId">
                                            <input type="text" readonly="readonly" class="form-control" id="orgName" name="orgName" placeholder="组织单位">
                                        </div>
                                        <div class="col-sm-2">
                                            <button type="button" class="btn btn-info" data-flag="org">选择</button>
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
                            <div class="box-footer text-right">
                                <!--以下两种方式提交验证,根据所需选择-->
                                <button type="button" class="btn btn-primary" data-btn-type="cancel">取消</button>
                                <button type="submit" class="btn btn-primary" data-btn-type="save">提交</button>
                            </div>
                            <!-- /.box-footer -->
                        </form>
                    </div>
                </div>
            </div>
        </section>
        <script>
            //tableId,queryId,conditionContainer
            var form = null;
            var id = "${id}";
            //console.log(id);
            if (id == null || id == "") {
                id = 0;
            }
            $(function () {
                //初始化控件
                form = $("#user-form").form();
                //数据校验
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
                            ajaxPost(basePath + '/userAction_save.do', params, function (
                                data, status) {
                                if (data.success) {
                                    toastr.success("保存成功");
                                    if (id != "0") { //更新
                                        gotolist(id);
                                    } else { //新增
                                        //modals.info("数据保存成功");
                                        gotolist();
                                    }
                                }
                            });
                        });
                    },
                    fields: {
                        name: {
                            validators: {
                                notEmpty: {
                                    message: '请输入姓名'
                                },
                                remote: {
                                    url: basePath + "/userAction_checkName.do", //验证的服务器地址
                                    //自定义提交数据，默认值提交当前input value
                                    data: function (validator) {
                                        return {
                                            userName: $('#name').val(),
                                            userId: id
                                        };
                                    },
                                    message: '该姓名已被使用', //提示消息
                                    //每输入一个字符，就发ajax请求，服务器压力还是太大，
                                    //设置2秒发送一次ajax（默认输入一个字符，提交一次，服务器压力太大）
                                    delay: 2000,
                                    type: 'POST' //请求方式
                                }
                            }
                        },
                        nickName: {
                            validators: {
                                notEmpty: {
                                    message: '请输入昵称'
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
                        orgName: {
                            trigger:"change",//当value值发生变化时也能校验
                            validators: {
                                notEmpty: {
                                    message: '请选择组织单位',
                                },
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

                //动态加载加载角色下拉框
                ajaxPost(basePath + "/userAction_getApUserRole.do", null, function (
                    data) {
                    var content = "";
                    for (var i = 0; i < data.length; i++) {
                        content += "<option value='" + data[i].code + "'>" + data[i].name + "</option>"
                    }
                    $("#roleName").append(content);
                });

                form.initComponent();
                //初始化组织单位选择器
                $("button[data-flag='org']").org({
                    idField: $("#orgId"),
                    nameField: $("#orgName"),
                    title: '选择组织',
                    levels: 1
                })
                //回填id
                if (id != "0") {
                    ajaxPost(basePath + "/userAction_getById.do", {
                        id: id
                    }, function (data) {
                        form.initFormData(data);
                        $(".content-header h1 small").html("编辑用户【" + data.name + "】");
                        //头像回填
                        $("#avatarImg").attr("src", basePath + data.userPic);
                    })
                }

                //取消
                $("[data-btn-type='cancel']").click(function () {
                    gotolist();
                })

                //上传头像
                $("[data-btn-type='upload']").click(function () {
                    uploadAvatar();
                })
            });

            function gotolist(id) {
                window.loadPage(basePath + "/userAction_pagelist.do?id=" + id);
            }

            var avatarWin = "avatarWin";

            function uploadAvatar() {
                modals.openWin({
                    winId: avatarWin,
                    title: '上传头像',
                    width: '700px',
                    url: basePath + "/userAction_avatar.do?userId=" + id
                });
            }

            function resetForm() {
                form.clearForm();
                $("#user-form").data('bootstrapValidator').resetForm();
            }
        </script>