<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    request.setAttribute("basePath", basePath);
    %>
        <script type="text/javascript" src="${basePath}/common/libs/webuploader/dist/webuploader.js"></script>
        <script type="text/javascript" src="${basePath}/common/js/addAssetUpload.js"></script>
        <script type="text/javascript" src="${basePath}/static/js/loading.js"></script>
        <link rel="stylesheet" type="text/css" href="${basePath}/common/libs/webuploader/css/webuploader.css" />
        <link rel="stylesheet" type="text/css" href="${basePath}/common/libs/webuploader/examples/image-upload/style.css" />
        <link rel="stylesheet" type="text/css" href="${basePath}/static/css/loading.css" />
        <section class="content-header">
            <h1>
                <span>我的资产</span>
                <small>新增</small>
            </h1>
            <ol class="breadcrumb">
                <li>
                    <a href="${basePath}">
                        <i class="fa fa-dashboard"></i> 首页</a>
                </li>
                <li>
                    <a href="#">我的资产</a>
                </li>
                <li class="active">新增</li>
            </ol>
        </section>
        <section class="content">
            <div class="row">
                <div class="col-xs-12">
                    <div class="box box-info">
                        <form class="form form-horizontal" id="assetForm" action="#" method="post">
                            <input type="hidden" name="id">
                            <!-- <input type="hidden" name="isAdmin"> -->
                            <input type="hidden" name="deleted">
                            <div class="form-group" style="text-align: left;">
                                <div class="col-xs-12">
                                    <div id="container">
                                        <!--头部，相册选择和格式选择-->
                                        <div id="uploader">
                                            <div class="queueList">
                                                <div id="dndArea" class="placeholder">
                                                    <div id="filePicker"></div>
                                                    <p>或将照片/文件拖到这里</p>
                                                </div>
                                            </div>
                                            <div class="statusBar" style="display:none;">
                                                <div class="progress">
                                                    <span class="text">0%</span>
                                                    <span class="percentage"></span>
                                                </div>
                                                <div class="info"></div>
                                                <div class="btns">
                                                    <div id="filePicker2"></div>
                                                    <div class="uploadBtn">开始上传</div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="box-body">
                                <div class="col-xs-12" style="text-align: right;">
                                    <div class="form-group">
                                        <label class="col-xs-2 form-control-label" for="name">资产名称</label>
                                        <div class="col-xs-10">
                                            <input class="form-control" type="text" name="name" id="name" placeholder="资产名称" />
                                        </div>
                                    </div>
                                    <div class="form-group" id="usernameParent">
                                        <label class="col-xs-2 form-control-label" for="username">资产类型</label>
                                        <div class="col-xs-10">
                                            <select class="form-control" id="assetType">

                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-xs-2 form-control-label" for="tag">标签</label>
                                        <div class="col-xs-10">
                                            <input class="form-control" type="text" name="tag" id="tag" placeholder="多个标签用空格分隔" />
                                        </div>
                                    </div>
                                    <div id="keyInfoDiv">
                                    </div>
                                </div>
                            </div>

                            <div class="box-footer text-right">
                                <!--以下两种方式提交验证,根据所需选择-->
                                <button type="button" class="btn btn-primary" onclick="cancel(this)">取消</button>
                                <button type="button" class="btn btn-primary" id="reIdentifyBtn" onclick="getKeyInfo()" disabled="disabled">重新识别</button>
                                <button type="button" class="btn btn-primary" id="submitBtn" onclick="save()" disabled="disabled">提交</button>
                            </div>

                        </form>
                    </div>
                </div>
            </div>
        </section>
        