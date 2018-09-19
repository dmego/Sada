<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
    <head>
        <meta charset="utf-8">
        <title>系统日志</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="renderer" content="webkit">
        <link rel="shortcut icon" href="${pageContext.request.contextPath }/assets/img/favicon.ico" />
        <!-- Loading Bootstrap -->
        <link href="${pageContext.request.contextPath }/assets/css/backend.min.css" rel="stylesheet">  
    </head>

    <body class="inside-header inside-aside ">
        <div id="main" role="main">
            <div class="tab-content tab-addtabs">
                <div id="content">
                    <div class="row">
                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                            <section class="content-header hide">
                                <h1>
                                    控制台<small>Control panel</small>
                                </h1>
                            </section>
                            <!-- RIBBON -->
                            <div id="ribbon">
                                <ol class="breadcrumb pull-left">
                                    <li><a href="dashboard" class="addtabsit"><i class="fa fa-dashboard"></i> 控制台</a></li>
                                </ol>
                                <ol class="breadcrumb pull-right">
                                    <li><a href="javascript:;" data-url="auth">权限管理</a></li>
                                    <li><a href="javascript:;" data-url="auth/adminlog">系统日志</a></li>
                                </ol>
                            </div>
                            <!-- END RIBBON -->
                            <div class="content">
                                <div class="panel panel-default panel-intro">

                                    <div class="panel-body">
                                        <div id="myTabContent" class="tab-content">
                                            <div class="tab-pane fade active in" id="one">
                                                <div class="widget-body no-padding">
                                                    <div id="toolbar" class="toolbar">
                                                        <a href="javascript:;" class="btn btn-primary btn-refresh" ><i class="fa fa-refresh"></i> </a> <a href="javascript:;" class="btn btn-danger btn-del btn-disabled disabled" ><i class="fa fa-trash"></i> 删除</a>                    </div>
                                                    <table id="table" class="table table-striped table-bordered table-hover" width="100%">

                                                    </table>


                                                </div>
                                            </div>

                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script src="${pageContext.request.contextPath }/assets/js/require.min.js" data-main="${pageContext.request.contextPath }/assets/js/require-backend.min.js"></script>
    </body>
</html>