<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    request.setAttribute("basePath", basePath);
    %>
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                <li class="fa fa-remove"></li>
            </button>
            <h5 class="modal-title">新增角色</h5>
        </div>

        <div class="modal-body">

            <form id="role-form" name="role-form" class="form-horizontal">
                <input type="hidden" name="id" id="id">
                <input type="hidden" name="deleted">
                <input type="hidden" name="auths" value="" />
                <div class="box-body">
                    <div class="col-md-12">
                        <div class="form-group">
                            <label for="name" class="col-sm-2 control-label">名称</label>
                            <div class="col-sm-9">
                                <input type="text" class="form-control" id="name" name="name" placeholder="名称">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="code" class="col-sm-2 control-label">编码</label>
                            <div class="col-sm-9">
                                <input type="text" class="form-control" id="code" name="code" placeholder="编码">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="sort" class="col-sm-2 control-label">排序</label>
                            <div class="col-sm-9">
                                <input type="text" class="form-control" id="sort" name="sort" placeholder="排序">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="auth" class="control-label col-xs-12 col-sm-2">权限:</label>
                            <div class="col-xs-12 col-sm-8">
                                <span class="text-muted">
                                    <input type="checkbox" name="" id="checkall" />
                                    <label for="checkall">
                                        <small>选中全部</small>
                                    </label>
                                </span>
                                <span class="text-muted">
                                    <input type="checkbox" name="" id="expandall" />
                                    <label for="expandall">
                                        <small>展开全部</small>
                                    </label>
                                </span>
                                <div id="treeview"></div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="deleted" class="col-sm-2 control-label">是否可用</label>
                            <div class="col-sm-9">
                                <label class="control-label">
                                    <input type="radio" name="deleted" class="square-green" checked="checked" value="0"> 启用
                                </label> &nbsp;&nbsp;&nbsp;
                                <label class="control-label">
                                    <input type="radio" name="deleted" class="square-green" value="1"> 禁用
                                </label>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="remark" class="col-sm-2 control-label">说明</label>
                            <div class="col-sm-9">
                                <textarea class="form-control" id="remark" name="remark" placeholder="说明"></textarea>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- /.box-body -->
                <div class="box-footer text-right">
                    <button type="button" class="btn btn-default" data-btn-type="cancel" data-dismiss="modal">取消</button>
                    <button type="submit" class="btn btn-primary" data-btn-type="save">提交</button>
                </div>
                <!-- /.box-footer -->
            </form>

        </div>
        <script>
            //tableId,queryId,conditionContainer
            var form = null;
            var id = "${id}";
            console.log(id);
            if (id == null || id == "") {
                id = 0;
            }
            $(function () {
                //初始化控件
                form = $("#role-form").form();
                //数据校验
                $("#role-form").bootstrapValidator({
                    message: '请输入有效值',
                    feedbackIcons: {
                        valid: 'glyphicon glyphicon-ok',
                        invalid: 'glyphicon glyphicon-remove',
                        validating: 'glyphicon glyphicon-refresh'
                    },
                    submitHandler: function (validator, roleform, submitButton) {
                        modals.confirm('确认保存？', function () {
                            //Save Data，对应'submit-提交'
                            refreshrules(); //将jstree选择的节点id赋值给auths，在组织表单参数之前
                            console.log($("input[name='auths']").val());
                            var params = form.getFormSimpleData();//组织表单参数
                            ajaxPost(basePath + '/roleAction_save.do', params, function (
                                data,
                                status) {
                                if (data.success) {
                                    toastr.success("保存成功");
                                    if (id != "0") { //更新
                                        modals.closeWin(winId);
                                        roleTable.reloadRowData(id);
                                    } else { //新增 
                                        modals.closeWin(winId);
                                        roleTable.reloadData();
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
                                }
                            }
                        },
                        code: {
                            validators: {
                                notEmpty: {
                                    message: '请输入编码'
                                },
                                remote: {
                                    url: basePath + "/roleAction_checkCode.do",
                                    data: function (validator) {
                                        return {
                                            id: $('#id').val(),
                                            code: $('#code').val()
                                        };
                                    },
                                    message: '该编码已被使用',
                                    delay: 2000,
                                    type: 'POST'//请求方式
                                }
                            }
                        },
                        sort: {
                            validators: {
                                notEmpty: {
                                    message: '请输入排序'
                                },
                                integer: {
                                    message: '请输入整数'
                                },
                                greaterThan: {
                                    value: 0,
                                    inclusive: true,
                                    message: '请输入大于0的整数'
                                }
                            }
                        }
                    }
                });
                form.initComponent();
                //修改数据时回填表单
                if (id != "0") {
                    ajaxPost(basePath + "/roleAction_getById.do", {
                        id: id
                    }, function (data) {
                        form.initFormData(data);
                        $(".modal-title").html("编辑角色【" + data.name + "】");
                    })
                }
            });

            function resetForm() {
                form.clearForm();
                $("#role-form").data('bootstrapValidator').resetForm();
            }

            //读取选中的条目
            $.jstree.core.prototype.get_all_checked = function (full) {
                var obj = this.get_selected(),
                    i, j;
                for (i = 0, j = obj.length; i < j; i++) {
                    obj = obj.concat(this.get_node(obj[i]).parents);
                }
                obj = $.grep(obj, function (v, i, a) {
                    return v != '#';
                });
                obj = obj.filter(function (itm, i, a) {
                    return i == a.indexOf(itm);
                });
                return full ? $.map(obj, $.proxy(function (i) {
                    return this.get_node(i);
                }, this)) : obj;

            };

            //获取所有选中的权限，并将id值以“，”隔开的方式添加到name=auths的input隐藏框里
            function refreshrules() {
                if ($("#treeview").size() > 0) {
                    var r = $("#treeview").jstree("get_all_checked");
                    $("input[name='auths']").val(r.join(','));
                }
                return true;
            }

            //构建jrtee
            function rendertree(content) {
                $("#treeview")
                    .on('redraw.jstree', function (e) {
                        $(".layer-footer").attr("domrefresh", Math.random());
                    })
                    .jstree({
                        "themes": {
                            "stripes": true
                        },
                        "checkbox": {
                            "keep_selected_style": false,
                        },
                        "types": {
                            "root": {
                                "icon": "fa fa-folder-open",
                            },
                            "menu": {
                                "icon": "fa fa-folder-open",
                            },
                            "file": {
                                "icon": "fa fa-file-o",
                            }
                        },
                        "plugins": ["checkbox", "types"],
                        "core": {
                            'check_callback': true,
                            "data": content
                        }
                    });
            }

            //通过ajax方式获取节点，渲染权限节点树(根据当前选择的角色动态生成)
            ajaxPost(basePath + "/roleAction_authTree.do?id=" + id, null, function (data) {
                //销毁已有的节点树
                $("#treeview").jstree("destroy");
                rendertree(data); //构建权限树
            });

            //全选和展开
            $(document).on("click", "#checkall", function () {
                $("#treeview").jstree($(this).prop("checked") ? "check_all" : "uncheck_all");
            });
            $(document).on("click", "#expandall", function () {
                $("#treeview").jstree($(this).prop("checked") ? "open_all" : "close_all");
            });
            $("select[name='row[pid]']").trigger("change");
        </script>