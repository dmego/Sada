<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    request.setAttribute("basePath", basePath);
    %>
    <script src="${basePath}/common/adminlte/plugins/bootstrap-treeview/bootstrap-treeview.min.js"></script>
    <section class="content-header">
        <h1>字典管理</h1>
        <ol class="breadcrumb">
            <li>
                <a href="${basePath}">
                    <i class="fa fa-dashboard"></i> 首页</a>
            </li>
            <li>
                <a href="#">系统管理</a>
            </li>
            <li class="active">字典管理</li>
        </ol>
    </section>

    <!-- Main content -->
    <section class="content">

        <div class="row">
            <div class="col-md-3">

                <!-- Profile Image -->
                <div class="box box-primary">
                    <div class="box-body box-profile">
                        <div id="tree"></div>
                    </div>
                    <!-- /.box-body -->
                </div>
                <!-- /.box -->
            </div>
            <!-- /.col -->
            <div class="col-md-9">
                <div class="box box-primary">
                    <div class="box-header with-border">
                        <div class="btn-group">
                            <button type="button" class="btn btn-default" data-btn-type="addRoot">
                                <li class="fa fa-plus">&nbsp;新增根字典</li>
                            </button>
                            <button type="button" class="btn btn-default" data-btn-type="add">
                                <li class="fa fa-plus">&nbsp;新增下级字典</li>
                            </button>
                            <button type="button" class="btn btn-default" data-btn-type="edit">
                                <li class="fa fa-edit">&nbsp;编辑当前字典</li>
                            </button>
                            <button type="button" class="btn btn-default" data-btn-type="delete">
                                <li class="fa fa-remove">&nbsp;删除当前字典</li>
                            </button>
                        </div>
                        <!-- /.box-tools -->
                    </div>
                    <!-- /.box-header -->
                    <div class="box-body">
                        <form class="form-horizontal" id="dict-form">
                            <input type="hidden" name="parentId" />
                            <div class="form-group">
                                <label for="parentName" class="col-sm-2 control-label">上级</label>

                                <div class="col-sm-9">
                                    <input type="text" class="form-control" disabled="disabled" id="parentName" name="parentName" placeholder="上级">
                                </div>
                            </div>

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
                                <label for="levelCode" class="col-sm-2 control-label">层级编码</label>

                                <div class="col-sm-9">
                                    <input type="text" class="form-control" id="levelCode" name="levelCode" placeholder="层级编码">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="value" class="col-sm-2 control-label">值</label>

                                <div class="col-sm-9">
                                    <input type="text" class="form-control" id="value" name="value" placeholder="值">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">是否可用</label>
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
                                <label for="remark" class="col-sm-2 control-label">备注</label>

                                <div class="col-sm-9">
                                    <textarea class="form-control" id="remark" name="remark" placeholder="备注"></textarea>
                                </div>
                            </div>
                            <div class="box-footer" style="display:none">
                                <div class="text-center">
                                    <button type="button" class="btn btn-default" data-btn-type="cancel">
                                        <i class="fa fa-reply">&nbsp;取消</i>
                                    </button>
                                    <button type="submit" class="btn btn-primary">
                                        <i class="fa fa-save">&nbsp;保存</i>
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                    <!-- /.box-body -->
                </div>
                <!-- /. box -->
            </div>
        </div>
        <!-- /.row -->

    </section>

    <script>
        var form = null; 
        $(function () {
            //初始化form表单
            form = $("#dict-form").form();

            initTree(0);
            //初始化校验
            $("#dict-form").bootstrapValidator({
                message: '请输入有效值',
                feedbackIcons: {
                    valid: 'glyphicon glyphicon-ok',
                    invalid: 'glyphicon glyphicon-remove',
                    validating: 'glyphicon glyphicon-refresh'
                },
                submitHandler: function (validator, dictform, submitButton) {
                    modals.confirm('确认保存？', function () {
                        //Save Data，对应'submit-提交'
                        var params = form.getFormSimpleData();
                        //console.log("表单数据为：")
                        //console.log(params);
                        ajaxPost(basePath + '/dictAction_save.do', params, function (data, status) {
                            if (data.success) {
                                //var id=$("input[name='id']").val();
                                var selectedArr = $("#tree").data("treeview").getSelected();
                                var selectedNodeId = selectedArr.length > 0 ?
                                    selectedArr[0].nodeId : 0;
                                initTree(selectedNodeId);
                                toastr.success("保存成功");
                            }else{                    
                                toastr.error("保存失败");
                            }
                        });
                    });
                },
                fields: {
                    name: {
                        validators: {
                            notEmpty: {
                                message: '请输入名称'
                            }
                        }
                    },
                    code: {
                        validators: {
                            notEmpty: {
                                message: '请输入编码'
                            },
                            /*BootstrapValidator的Remote远程验证
                              ajax验证编码的唯一性
                              server result:{"valid",true or false}
                              向服务发送当前input name值，获得一个json数据。
                              例表示正确：{"valid",true}
                             */
                            remote: {
                                url: basePath + "/dictAction_codeCheck.do", //验证的服务器地址
                                //自定义提交数据，默认值提交当前input value
                                data: function (validator) {
                                    return {
                                        id: $('#id').val(),
                                        code: $('#code').val()
                                    };
                                },
                                message: '该编码已被使用',//提示消息
                                //每输入一个字符，就发ajax请求，服务器压力还是太大，
                                //设置2秒发送一次ajax（默认输入一个字符，提交一次，服务器压力太大）
                                delay: 2000,
                                type: 'POST'//请求方式
                            }
                        }
                    },
                    levelCode: {
                        validators: {
                            notEmpty: {
                                message: '请输入层级编码'
                            }
                        }
                    },
                    deleted: {
                        validators: {
                            notEmpty: {
                                message: '请选择是否可用'
                            }
                        }
                    }
                }
            });

            form.initComponent();
            //按钮事件
            var btntype = null;
            $('button[data-btn-type]').click(function () {
                var action = $(this).attr('data-btn-type');
                var selectedArr = $("#tree").data("treeview").getSelected();
                var selectedNode = selectedArr.length > 0 ? selectedArr[0] : null;
                switch (action) {
                    case 'addRoot':
                        formWritable(action);
                        form.clearForm();
                        //填充上级字典和层级编码
                        fillParentAndLevelCode(null);
                        btntype = 'add';
                        break;
                    case 'add':
                        if (!selectedNode) {
                            toastr.warning('请先选择上级字典');
                            return false;
                        }
                        formWritable(action);
                        form.clearForm();
                        //填充上级字典和层级编码
                        fillParentAndLevelCode(selectedNode);
                        btntype = 'add';
                        break;
                    case 'edit':
                        if (!selectedNode) {
                            toastr.warning('请先选择要编辑的节点');
                            return false;
                        }
                        if (btntype == 'add') {
                            fillDictForm(selectedNode);
                        }
                        formWritable(action);
                        btntype = 'edit';
                        break;
                    case 'delete':
                        if (!selectedNode) {
                            toastr.warning('请先选择要删除的节点');
                            return false;
                        }
                        if (btntype == 'add')
                            fillDictForm(selectedNode);
                        formReadonly();
                        $(".box-header button[data-btn-type='delete']").removeClass("btn-default").addClass(
                            "btn-primary");
                        if (selectedNode.nodes) {
                            toastr.warning('该节点含有子节点，请先删除子节点');
                            return false;
                        }
                        modals.confirm('是否删除该节点', function () {
                            ajaxPost(basePath + "/dictAction_del.do?id=" + selectedNode.id, null,
                                function (data) {
                                    if (data.success) {
                                        toastr.success('删除成功');
                                    } else {
                                        modals.info(data.message);
                                    }
                                    //定位
                                    var brothers = $("#tree").data("treeview").getSiblings(
                                        selectedNode);
                                    if (brothers.length > 0)
                                        initTree(brothers[brothers.length - 1].nodeId);
                                    else {
                                        var parent = $("#tree").data("treeview").getParent(
                                            selectedNode);
                                        initTree(parent ? parent.nodeId : 0);
                                    }
                                });
                        });
                        break;
                    case 'cancel':
                        if (btntype == 'add')
                            fillDictForm(selectedNode);
                        formReadonly();
                        break;
                }
            });
        })
        
        //初始化节点树
        function initTree(selectNodeId) {
            var treeData = null;
            ajaxPost(basePath + "/dictAction_treeData.do", null, function (data) {
                treeData = data;
                //console.log(treeData);
            });
            $("#tree").treeview({
                data: treeData,
                showBorder: true,
                expandIcon: "glyphicon glyphicon-stop",
                collapseIcon: "glyphicon glyphicon-unchecked",
                levels: 1,
                onNodeSelected: function (event, data) {
                    fillDictForm(data);
                    formReadonly();
                    //console.log(JSON.stringify(data));
                }
            });
            if (treeData.length == 0)
                return;
            //默认选中第一个节点
            selectNodeId = selectNodeId || 0;
            $("#tree").data('treeview').selectNode(selectNodeId);
            $("#tree").data('treeview').expandNode(selectNodeId);
            $("#tree").data('treeview').revealNode(selectNodeId);
        }

        //新增时，带入父级字典名称id,自动生成levelcode
        function fillParentAndLevelCode(selectedNode) {
            $("input[name='parentName']").val(selectedNode ? selectedNode.text : '系统字典');
            $("input[name='deleted'][value='0']").prop("checked", "checked");
            if (selectedNode) {
                $("input[name='parentId']").val(selectedNode.id);
                var nodes = selectedNode.nodes;
                var levelCode = nodes ? nodes[nodes.length - 1].levelCode : null;
                $("input[name='levelCode']").val(getNextCode(selectedNode.levelCode, levelCode, 6));
            } else {
                var parentNode = $("#tree").data("treeview").getNode(0);
                var levelCode = "000000";
                if (parentNode) {
                    var brothers = $("#tree").data("treeview").getSiblings(0);
                    levelCode = brothers[brothers.length - 1].levelCode;
                }
                $("input[name='levelCode']").val(getNextCode("", levelCode, 6));
            }
        }

        //填充form
        function fillDictForm(node) {
            form.clearForm();
            ajaxPost(basePath + "/dictAction_getById.do?id="+node.id,null, function (data) {
                form.initFormData(data);
            })
        }

        //设置form为只读
        function formReadonly() {
            //所有文本框只读
            $("input[name],textarea[name]").attr("readonly", "readonly");
            //隐藏取消、保存按钮
            $("#dict-form .box-footer").hide();
            //还原新增、编辑、删除按钮样式
            $(".box-header button").removeClass("btn-primary").addClass("btn-default");
            //还原校验框
            if ($("#dict-form").data('bootstrapValidator'))
                $("#dict-form").data('bootstrapValidator').resetForm();
        }
        
        //将只读表单form还原为可读
        function formWritable(action) {
            $("input[name],textarea[name]").removeAttr("readonly");
            $("#dict-form .box-footer").show();
            $(".box-header button").removeClass("btn-primary").addClass("btn-default");
            if (action)
                $(".box-header button[data-btn-type='" + action + "']").removeClass("btn-default").addClass(
                    "btn-primary");
        }
    </script>