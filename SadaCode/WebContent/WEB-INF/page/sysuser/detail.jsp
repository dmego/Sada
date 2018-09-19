<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    request.setAttribute("basePath", basePath);
    String userId = (String)session.getAttribute("userId");
    request.setAttribute("id", userId);
    %>

        <section class="content-header">
            <h1>
                <span>个人资料</span>
                <small></small>
            </h1>
            <ol class="breadcrumb">
                <li>
                    <a href="${basePath}">
                        <i class="fa fa-dashboard"></i>首页</a>
                </li>
                <!-- <li>
                <a href="#">系统管理</a>
            </li> -->
                <li class="active">个人资料</li>
            </ol>
        </section>
        <section class="content">
            <div class="row">
                <div class="col-xs-12">
                    <div class="box box-info">
                        <form id="user-form" name="user-form" class="form-horizontal">
                            <input type="hidden" name="id">
                            <input type="hidden" id="adminIs" name="adminIs" value="0">
                            <input type="hidden" name="orgId" id="orgId">
                            <input type="hidden" id="roleName" name="roleName">
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
                                    <div id="isLeader" style="display: none">
                                        <div class="form-group">
                                            <label for="orgName1" class="col-sm-3 control-label">组织单位</label>
                                            <div class="col-sm-8">
                                                <input type="text" readonly="readonly" class="form-control" id="orgName1" name="orgName1" placeholder="组织单位">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label for="invitation" class="col-sm-3 control-label">邀请码</label>
                                            <div class="col-sm-6">
                                                <input type="text" readonly="readonly" class="form-control" id="invitation" name="invitation" placeholder="邀请码">
                                            </div>
                                            <div class="col-sm-2">
                                                <button type="button" class="btn btn-info" data-btn-type="invitation">重新生成</button>
                                            </div>
                                        </div>
                                    </div>
                                    <div id="isUser" style="display: none">
                                        <div class="form-group">
                                            <label for="orgName2" class="col-sm-3 control-label">组织单位</label>
                                            <div class="col-sm-6">
                                                
                                                <input type="text" readonly="readonly" class="form-control" id="orgName2" name="orgName2" placeholder="暂无组织">
                                            </div>
                                            <div class="col-sm-2">
                                                <button type="button" class="btn btn-info" data-btn-type="joinOrg">加入组织</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- /.box-body -->
                            <div class="box-footer text-right">
                                <!--以下两种方式提交验证,根据所需选择-->
                                <button type="submit" class="btn btn-primary" data-btn-type="update">修改</button>
                            </div>
                            <!-- /.box-footer -->
                        </form>
                    </div>
                </div>
            </div>
        </section>
        <script type="text/javascript" src="${basePath}/common/js/base-form.js"></script>
        <script src="${basePath}/common/adminlte/plugins/datepicker/bootstrap-datepicker.js"></script>
        <script src="${basePath}/common/adminlte/plugins/bootstrap-validator/dist/js/bootstrap-validator.js"></script>
        <script src="${basePath}/common/adminlte/plugins/iCheck/icheck.min.js"></script>
        <!-- AdminLTE for demo purposes -->
        <script src="${basePath}/common/adminlte/dist/js/demo.js"></script>
        <!--select2-->
        <script src="${basePath}/common/adminlte/plugins/select2/select2.full.min.js"></script>
        <!--toastr-->
        <script src="${basePath}/common/libs/toastr/toastr.min.js"></script>
        <script type="text/javascript" src="${basePath}/static/js/bootbox.js"></script>
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
                        modals.confirm('确认更新？', function () {
                            //Save Data，对应'submit-提交'
                            var params = form.getFormSimpleData();
                            ajaxPost(basePath + '/userAction_save.do', params, function (
                                data, status) {
                                if (data.success) {
                                    toastr.success("更新成功");
                                    //还原校验框
                                    if ($("#user-form").data('bootstrapValidator'))
                                        $("#user-form").data('bootstrapValidator').resetForm();
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
                        }
                    }
                });

                form.initComponent();

                //回填id
                if (id != "0") {
                    ajaxPost(basePath + "/userAction_getById.do", {
                        id: id
                    }, function (data) {
                        if (data.userIs == 1) { //如果是用户
                            $("#orgName2").val(data.orgName);
                            $("#isLeader").hide();
                            $("#isUser").show();
                        } else if (data.userIs == 0) {
                            $("#orgName1").val(data.orgName);
                            $("#isLeader").show();
                            $("#isUser").hide();
                        }
                        form.initFormData(data);

                        $(".content-header h1 small").html("用户【" + data.name + "】");
                        //头像回填
                        $("#avatarImg").attr("src", basePath + data.userPic);
                    })
                }

                //上传头像
                $("[data-btn-type='upload']").click(function () {
                    uploadAvatar();
                })

                //随机生成邀请码
                $("[data-btn-type='invitation']").click(function () {
                    invitation();
                })

                //加入组织
                $("[data-btn-type='joinOrg']").click(function () {
                    joinOrg();
                })
            });

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

            //随机生成6位邀请码
            function invitation() {
                var str = "",
                    arr = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B',
                        'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
                        'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
                    ];
                for (var i = 0; i < 6; i++) {
                    pos = Math.round(Math.random() * (arr.length - 1));
                    str += arr[pos];
                }
                $("#invitation").val(str);
            }

            function joinOrg(params) {
                bootbox.setLocale("zh_CN");
                bootbox.prompt({
                    size: "small",
                    title: "请输入组织邀请码",
                    callback: function (result) {
                        if (result != null && result.length > 0) {
                            ajaxPost(basePath + '/userAction_joinOrg.do', {
                                InviteCode: result,
                                id: id
                            }, function (data, status) {
                                if (data.success) {
                                    console.log(data.data);
                                    $("#orgId").val(data.message);
                                    $("#orgName2").val(data.data);
                                    toastr.success("加入成功");
                                } else if (!data.success) {
                                    toastr.warning("无效邀请码");
                                }
                            });
                        }
                    }
                })
            }
        </script>