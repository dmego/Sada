<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    request.setAttribute("basePath", basePath);
    %>
        <script type="text/javascript" src="${basePath}/common/libs/webuploader/dist/webuploader.js"></script>
        <script type="text/javascript" src="${basePath}/common/js/validateAssetUpload.js"></script>
        <script type="text/javascript" src="${basePath}/static/js/loading.js"></script>
        <link rel="stylesheet" type="text/css" href="${basePath}/common/libs/webuploader/css/webuploader.css" />
        <link rel="stylesheet" type="text/css" href="${basePath}/common/libs/webuploader/examples/image-upload/style.css" />
        <link rel="stylesheet" type="text/css" href="${basePath}/static/css/loading.css" />

        <style type="text/css">
            .collapse1 {
              display: none;
            }
            .collapse1.in1 {
              display: block;
            }
            .alert1 {
              padding: 15px;
              margin-bottom: 17px;
              border: 1px solid transparent;
              border-radius: 3px;
            }
            .alert-success1 {
              background-color: #dff0d8;
              border-color: #d6e9c6;
              color: #18bc9c;
            }
            .alert-success1 hr {
              border-top-color: #c9e2b3;
            }
            .alert-success1 .alert-link1 {
              color: #128f76;
            }
           
            .alert-warning1 {
              background-color: #fcf8e3;
              border-color: #faebcc;
              color: #f39c12;
            }
            .alert-warning1 hr {
              border-top-color: #f7e1b5;
            }
            .alert-warning1 .alert-link1 {
              color: #c87f0a;
            }
        </style>
        <input type="hidden" name="id" id="id" value="${id}">
        <section class="content-header">
            <h1>
                <span>资产鉴权</span>
                <!-- <small>新增</small> -->
            </h1>
            <ol class="breadcrumb">
                <li>
                    <a href="${basePath}">
                        <i class="fa fa-dashboard"></i> 首页</a>
                </li>
                <li class="active">资产鉴权</li>
            </ol>
        </section>
        <section class="content">
            <div id="wrapDiv">
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
                                                    <div class="uploadBtn" id="uploadBtn">开始鉴权</div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="box-body">
                                <div class="col-xs-12" style="text-align: right;">
                                   
                                    <div id="keyInfoDiv">

                                    </div>
                                </div>
                            </div>
                            
                            
                                
                                
                                    <!--以下两种方式提交验证,根据所需选择-->
                                    <div class="box-footer text-right">
                                        <button type="button" class="btn btn-primary" onclick="cancel(this)">取消</button>
                                        <button type="button" class="btn btn-primary" id="submitBtn" onclick="toCompareMd5()" disabled="disabled">比对MD5</button>    
                                    </div>
                                    
                                
                          

                            

                        </form>
                    </div>
                </div>
            </div>
        </div>
        </section>
        <script>   
            //取消
            function cancel() {
                modals.hideWin("coreAuthWin");
                $('body').addClass('modal-open');
            }
        </script>