<!--login_page_identity-->
<!--以上文件注释不可去掉，校验文件首页用,详见base.js的jquery.fn.load-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib uri="/struts-tags" prefix="s"%>
        <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
request.setAttribute("basePath", basePath);
%>
            <!DOCTYPE html>
            <html lang="zh-cn">

            <head>
                <title>Sada</title>
                <meta charset="utf-8">
                <meta http-equiv="X-UA-Compatible" content="IE=edge">
                <!-- 移动设备 viewport -->
                <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no,minimal-ui">
                <!-- 360浏览器默认使用Webkit内核 -->
                <meta name="renderer" content="webkit">
                <!-- 禁止百度SiteAPP转码 -->
                <meta http-equiv="Cache-Control" content="no-siteapp">

                <!-- 样式 -->
                <link rel="stylesheet" href="${basePath}/common/adminlte/bootstrap/css/bootstrap.min.css">
                <link rel="stylesheet" href="${basePath}/common/css/site.css" id="siteStyle">
                <!-- 自定义 -->
                <link rel="stylesheet" href="${basePath}/common/css/login.css">
                <!-- 插件 -->
                <link rel="stylesheet" href="${basePath}/common/css/animsition.css">
                <!-- 图标 -->
                <link rel="stylesheet" href="${basePath}/common/fonts/web-icons/web-icons.css">
                <link rel="stylesheet" href="${basePath}/common/libs/font-awesome/css/font-awesome.css">
                <!-- 插件 -->
                <link rel="stylesheet" href="${basePath}/common/libs/bootstrap-select/dist/css/bootstrap-select.css">
                <link rel="stylesheet" href="${basePath}/common/libs/formvalidation/formValidation.css">
                <link rel="stylesheet" href="${basePath}/common/adminlte/plugins/bootstrap-validator/dist/css/bootstrap-validator.css">

                <!-- 引入 gt.js，既可以使用其中提供的 initGeetest 初始化函数 -->
                <script src="${basePath}/static/js/gt.js"></script>
                <style>
                    #captcha1,
                    #captcha2 {
                        width: 100%;
                        display: inline-block;
                    }

                    .iblock {
                        display: block;
                    }

                    .inone {
                        display: none;
                    }

                    #notice1,
                    #notice2 {
                        color: red;
                    }

                    #wait1,
                    #wait2 {
                        text-align: left;
                        color: #666;
                        margin: 0;
                    }
                </style>
            </head>

            <body class="page-login layout-full page-dark" id="myId">

                <div style="position:absolute;;left:0;top:0;width:100%;height:100%" id="particles-js"></div>

                <div class="page height-full">
                    <div class="page-content height-full">
                        <div class="page-brand-info vertical-align animation-slide-left hidden-xs">
                            <div class="page-brand vertical-align-middle">

                                <div class="brand">
                                    <img class="brand-img" src="${basePath}/common/images/svg/gen.svg" height="50" alt="Sada">
                                </div>
                                <h2 class="hidden-sm">Sada 可信数字资产存证应用</h2>
                                <ul class="list-icons hidden-sm">
                                    <li>
                                        <i class="wb-check" aria-hidden="true"></i>
                                        Sada 是在云计算平台分配的虚拟机部署存证应用的区块链网络， 存证应用可将数字资产中关键信息及数字资产的md5值保存到区块链，同时将数字资产保存到存储服务器
                                    </li>
                                    <li>
                                        <i class="wb-check" aria-hidden="true"></i> Sada 可以有效地对数字资产的关键信息进行保存，防止信息被篡改</li>
                                    <li>
                                        <i class="wb-check" aria-hidden="true"></i> Sada 由于应用底层使用区块链技术，具备良好的容灾备份机制，从而可有效的防止单点故障。</li>
                                </ul>
                            </div>
                        </div>


                        <div class="page-login-main animation-fade">
                            <div class="vertical-align">
                                <!-- 用户登录  -->
                                <div class="vertical-align-middle" id="login_container">
                                    <div class="brand visible-xs text-center">
                                        <img class="brand-img" src="${basePath}/common/images/svg/logomobile.svg" height="50" alt="Sada">
                                    </div>
                                    <h3 class="hidden-xs">登录 Sada</h3>
                                    <p class="hidden-xs">Sada 可信数字资产存证应用</p>
                                    <form action="#" class="login-form" method="post" id="loginForm" action="${basePath}/menuAction_login.do">
                                        <div class="form-group">
                                            <label class="sr-only" for="loginname">用户名</label>
                                            <input type="text" class="form-control" id="username" name="username" value="${requestScope.username }" placeholder="请输入用户名">
                                        </div>
                                        <div class="form-group">
                                            <label class="sr-only" for="loginpass">密码</label>
                                            <input type="password" class="form-control" id="password" name="password" value="${requestScope.password }" placeholder="请输入密码">
                                        </div>
                                        <div class="form-group">
                                            <div>
                                                <div id="captcha1">
                                                    <p id="wait1" class="iblock">正在加载验证码......</p>
                                                </div>
                                            </div>
                                            <br>
                                        </div>
                                        <div class="form-group clearfix">
                                            <div class="checkbox-custom checkbox-inline checkbox-primary pull-left">
                                                <input type="checkbox" id="rememberMe" name="rememberMe" value="yes" ${requestScope.checked}>
                                                <label for="rememberMe">记住我</label>
                                            </div>
                                            <div class="pull-right">
                                                <a href="#" id="toRegist" onclick="geeRegist()">注册账号</a>
                                                ·
                                                <a class="pull-right collapsed" data-toggle="collapse" href="#forgetPassword1" aria-expanded="false" aria-controls="forgetPassword1">找回密码</a>
                                            </div>
                                        </div>
                                        <div class="collapse" id="forgetPassword1" aria-expanded="false" style="height: 0px;">
                                            <div class="alert alert-warning alert-dismissible" role="alert">
                                                请联系管理员重置密码
                                            </div>
                                        </div>
                                        <div class="collapse inone" id="notice1">
                                            <div class="alert alert-warning alert-dismissible" id="msg1" role="alert">

                                            </div>
                                        </div>
                                        <button type="submit" id="submit1" onclick="return handler1()" class="btn btn-primary btn-block margin-top-30">立即登录</button>
                                    </form>
                                </div>

                                <!-- 用户注册  -->
                                <div class="vertical-align-middle" id="regist_container" style="display: none;">
                                    <div class="brand visible-xs text-center">
                                        <img class="brand-img" src="${basePath}/common/images/svg/logomobile.svg" height="50" alt="Sada">
                                    </div>
                                    <h3 class="hidden-xs">注册 Sada</h3>
                                    <p class="hidden-xs">Sada 可信数字资产存证应用</p>
                                    <form action="#" class="login-form" method="post" id="registForm">
                                        <div class="form-group">
                                            <label class="sr-only" for="registname">用户名</label>
                                            <input type="text" class="form-control" id="registname" name="registname" placeholder="请输入用户名">
                                        </div>
                                        <div class="form-group">
                                            <label class="sr-only" for="registpass1">密码</label>
                                            <input type="password" class="form-control" id="registpass1" name="registpass1" placeholder="请输入密码">
                                        </div>
                                        <div class="form-group">
                                            <label class="sr-only" for="registpass2">确认密码</label>
                                            <input type="password" class="form-control" id="registpass2" name="registpass2" placeholder="请输入确认密码">
                                        </div>
                                        <div class="form-group">
                                            <div>
                                                <div id="captcha2">
                                                    <p id="wait2" class="iblock">正在加载验证码......</p>
                                                </div>
                                            </div>
                                            <br>
                                        </div>
                                        <div class="form-group clearfix">
                                            <div class="checkbox-custom checkbox-inline checkbox-primary pull-left">
                                                <input type="checkbox" id="agree" name="agree" value="yes">
                                                <label for="agree">我同意
                                                    <a href="#">《SADA相关条例》</a>
                                                </label>
                                            </div>
                                            <div class="pull-right">
                                                <a href="#" id="toLogin">登录账号</a>
                                                ·
                                                <a class="pull-right collapsed" data-toggle="collapse" href="#forgetPassword2" aria-expanded="false" aria-controls="forgetPassword2">找回密码</a>
                                            </div>
                                        </div>
                                        <div class="collapse" id="forgetPassword2" aria-expanded="false" style="height: 0px;">
                                            <div class="alert alert-warning alert-dismissible" role="alert">
                                                请联系管理员重置密码
                                            </div>
                                        </div>
                                        <div class="collapse" id="notice2">
                                            <div class="alert alert-warning alert-dismissible" role="alert">
                                                请先完成验证
                                            </div>
                                        </div>
                                        <button type="submit" id="submit2" onclick="return handler2()" class="btn btn-primary btn-block margin-top-30">立即注册</button>
                                    </form>
                                </div>

                            </div>
                            <footer class="page-copyright">
                                <p>可信数字资产存证应用 &copy;
                                    <a href="#" target="_blank">SADA</a>
                                </p>
                            </footer>
                        </div>

                    </div>
                </div>

                <!-- JS -->
                <script src="${basePath}/common/adminlte/plugins/jQuery/jQuery-2.2.0.js"></script>
                <script src="${basePath}/common/adminlte/bootstrap/js/bootstrap.min.js"></script>
                <script src="${basePath}/common/libs/bootstrap-select/dist/js/bootstrap-select.min.js"></script>
                <script src="${basePath}/common/libs/formvalidation/formValidation.min.js"></script>
                <script src="${basePath}/common/libs/formvalidation/framework/bootstrap.min.js"></script>
                <script src="${basePath}/common/adminlte/plugins/bootstrap-validator/dist/js/bootstrap-validator.js"></script>
                <script type="text/javascript" src="static/js/particles.js"></script>
                <script type="text/javascript" src="static/js/app.js"></script>
                <script type="text/javascript" src="${basePath}/common/js/base.js"></script>
                <!--背景图片自动更换-->
                <script src="${basePath}/common/js/jquery.backstretch.min.js"></script>
                <script src="${basePath}/static/js/system/login.js"></script>
                <script type="text/javascript">
                    var basePath = "${basePath}"; //给外部js文件传递路径参数
                    console.log(basePath);
                    $(function () {
                        $(".page-login").backstretch([
                            /*此处为自定义的图片，要注意图片的路径  */
                            "${basePath}/common/images/logoimg/login.jpg",
                            "${basePath}/common/images/logoimg/login1.jpg",
                            "${basePath}/common/images/logoimg/login2.jpg",
                            "${basePath}/common/images/logoimg/login3.jpg",
                            "${basePath}/common/images/logoimg/login4.jpg",
                            "${basePath}/common/images/logoimg/login5.jpg"
                        ], {
                            duration: 6000,
                            fade: 750
                        });
                    });
                </script>
                <s:if test="#request.msg != null">
                    <script type="text/javascript">
                        var msg = "${msg}";
                        $("#notice1").show();
                        $("#msg1").empty().append(msg);
                        setTimeout(function () {
                            $("#notice1").hide();
                        }, 2000);
                    </script>
                </s:if>
            </body>

            </html>