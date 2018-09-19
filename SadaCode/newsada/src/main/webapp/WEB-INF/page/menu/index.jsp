<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    request.setAttribute("basePath", basePath);
    String msg = (String)session.getAttribute("msg");
    request.setAttribute("msg", msg);
    session.removeAttribute("msg");
    %>
        <!DOCTYPE html>
        <html lang="zh-cn">

        <head>
            <meta charset="utf-8">
            <title>Sada</title>
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <meta name="renderer" content="webkit">
            <link rel="shortcut icon" type="image/x-icon" href="${basePath}/common/images/favicon.ico" media="screen" />
            <link rel="stylesheet" href="${basePath}/common/css/base.css">
            <link rel="stylesheet" href="${basePath}/static/css/style.min.css">
            <link rel="stylesheet" href="${basePath}/static/css/animate.min.css">
            <link rel="stylesheet" href="${basePath}/common/adminlte/bootstrap/css/bootstrap.min.css">
            <link rel="stylesheet" href="${basePath}/common/libs/font-awesome/css/font-awesome.min.css">

            <link rel="stylesheet" href="${basePath}/common/adminlte/plugins/datatables/dataTables.bootstrap.css">
            <link rel="stylesheet" href="${basePath}/common/adminlte/plugins/datatables/select.bootstrap.min.css">
            <link rel="stylesheet" href="${basePath}/common/adminlte/plugins/bootstrap-validator/dist/css/bootstrap-validator.css">
            <link rel="stylesheet" href="${basePath}/common/adminlte/plugins/iCheck/all.css">
            <link rel="stylesheet" href="${basePath}/common/libs/jstree/dist/themes/default/style.min.css">
            <link rel="stylesheet" href="${basePath}/common/libs/toastr/toastr.min.css">
            <link rel="stylesheet" href="${basePath}/common/adminlte/plugins/datepicker/datepicker3.css">
            <link rel="stylesheet" href="${basePath}/common/adminlte/plugins/select2/select2.min.css">
            <link rel="stylesheet" href="${basePath}/common/adminlte/plugins/bootstrap-treeview/bootstrap-treeview.min.css">
            <link rel="stylesheet" href="${basePath}/common/adminlte/dist/css/AdminLTE.min.css">
            <link rel="stylesheet" href="${basePath}/common/adminlte/dist/css/skins/skin-blue.css">

            <link rel="stylesheet" href="${basePath}/common/css/animsition.css">
            <!-- daterangepicker -->
            <!--<link rel="stylesheet" href="${basePath}/adminlte/plugins/daterangepicker/daterangepicker.css">-->

            <!-- HTML5 shim, for IE6-8 support of HTML5 elements. All other JS at the end of file. -->
            <!--[if lt IE 9]>
          <script src="${basePath}/assets/js/html5shiv.js"></script>
          <script src="${basePath}/assets/js/respond.min.js"></script>
        <![endif]-->
        </head>

        <body class="hold-transition skin-blue sidebar-mini fixed" id="tabs">
            <a name="main"></a>
            <div class="wrapper">
                <header id="header" class="main-header">
                    <!-- Logo -->
                    <a href="${basePath}/main" class="logo">
                        <!-- 迷你模式下Logo的大小为50X50 -->
                        <span class="logo-mini">SADA</span>
                        <!-- 普通模式下Logo -->
                        <span class="logo-lg">
                            <b>可信数字资产存证应用</b>
                        </span>
                    </a>
                    <!-- 顶部通栏样式 -->
                    <nav class="navbar navbar-static-top">
                        <!-- 边栏切换按钮-->
                        <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
                            <span class="sr-only">Toggle navigation</span>
                        </a>
                        <div id="nav" class="pull-left">
                            <!--如果不想在顶部显示角标,则给ul加上disable-top-badge类即可-->
                            <ul class="nav nav-tabs nav-addtabs disable-top-badge" role="tablist">
                            </ul>
                        </div>
                        <div class="navbar-custom-menu">
                            <ul class="nav navbar-nav">
                                <li>
                                    <a href="#" target="_blank">
                                        <i class="fa fa-home" style="font-size:14px;"></i>
                                    </a>
                                </li>
                                <li class="dropdown notifications-menu">
                                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                        <i class="fa fa-bell-o"></i>
                                        <span class="label label-warning"></span>
                                    </a>
                                    <ul class="dropdown-menu">
                                        <li class="header">最新信息</li>
                                        <li>
                                            <!-- 最新更新信息,你可以替换成你自己站点的信息-->
                                            <ul class="menu">

                                            </ul>
                                        </li>
                                        <li class="footer">
                                            <a href="#" target="_blank">查看全部</a>
                                        </li>
                                    </ul>
                                </li>

                                <li>
                                    <a href="javascript:;" data-toggle="wipecache" title="清空缓存">
                                        <i class="fa fa-trash"></i>
                                    </a>
                                </li>

                                <li class="hidden-xs">
                                    <a href="#" data-toggle="fullscreen">
                                        <i class="fa fa-arrows-alt"></i>
                                    </a>
                                </li>

                                <!-- 账号信息下拉框 -->
                                <li class="dropdown user user-menu">
                                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                        <img src="${basePath}${user.userPic}" class="user-image" alt="${user.name}">
                                        <span class="hidden-xs">${user.nickName}</span>
                                    </a>
                                    <ul class="dropdown-menu">
                                        <!-- User image -->
                                        <li class="user-header">
                                            <img src="${basePath}${user.userPic}" class="img-circle" alt="${user.name}">
                                            <p>
                                                ${user.nickName}
                                                <small>${user.logintime}</small>
                                            </p>
                                        </li>
                                        <!-- Menu Body -->
                                        <li class="user-body">
                                            <div class="row">
                                                <div class="col-xs-4 text-center">
                                                    <a href="#" target="_blank">个人资料</a>
                                                </div>
                                                <div class="col-xs-4 text-center">
                                                    <a href="#" target="_blank">我的资产</a>
                                                </div>
                                                <div class="col-xs-4 text-center">
                                                    <a href="#" target="_blank">鉴权中心</a>
                                                </div>
                                            </div>
                                        </li>
                                    </ul>
                                </li>
                                <!-- 退出注销 -->
                                <li>
                                    <a href="${basePath}/logout">
                                        <i class="fa fa-sign-out"></i>
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </nav>
                </header>
                <!-- Left side column. contains the logo and sidebar -->
                <aside class="main-sidebar">
                    <!-- sidebar: style can be found in sidebar.less -->
                    <section class="sidebar">
                        <!-- Sidebar user panel 左侧菜单栏用户面板-->
                        <div class="user-panel hidden-xs">
                            <div class="pull-left image">
                                <a href="" class="addtabsit">
                                    <img src="${basePath}${user.userPic}" class="img-circle" alt="User Image" />
                                </a>
                            </div>
                            <div class="pull-left info">
                                <p>${user.name}</p>
                                <i class="fa fa-circle text-success"></i>在线
                            </div>
                        </div>

                        <!-- 菜单栏搜索框 -->
                        <form action="" method="get" class="sidebar-form" onsubmit="return false;">
                            <div class="input-group">
                                <input type="text" name="q" class="form-control" placeholder="搜索菜单">
                                <span class="input-group-btn">
                                    <button type="submit" name="search" id="search-btn" class="btn btn-flat">
                                        <i class="fa fa-search"></i>
                                    </button>
                                </span>
                                <div class="menuresult list-group sidebar-form hide">
                                </div>
                            </div>
                        </form>
                        <!-- /.search form -->

                        <!-- 左侧菜单栏, 动态加载: : style can be found in sidebar.less -->
                        <!--如果想始终显示子菜单,则给ul加上show-submenu类即可-->
                        <ul class="sidebar-menu" id="sideber-menu">
                            <li class="header">站内导航</li>
                        </ul>
                    </section>
                    <!-- /.sidebar -->
                </aside>

                <!-- 主界面Content Wrapper. Contains page content -->
                <div class="content-wrapper" id="mainDiv" style="overflow: auto;background-color: #ecf0f5">

                </div>
                <!-- /.content-wrapper -->
                <footer class="main-footer  text-center">
                    <!--隐藏footer需要给footer加上class="hide"
                    <div class="pull-right hidden-xs">
                    </div> -->
                    <strong>Copyright &copy; 2018
                        <a href="#">SADA</a>.</strong> All rights reserved.
                    <!-- <a href="#">鄂ICP备18001431号</a> -->
                </footer>

            </div>
            <!-- ./wrapper -->
            <!-- end main content -->

            <!-- jQuery 2.2.0 -->
            <script src="${basePath}/common/adminlte/plugins/jQuery/jQuery-2.2.0.min.js"></script>
            <!--JSON2-->
            <script src="${basePath}/common/json/json2.js"></script>
            <!-- Bootstrap 3.3.6 -->
            <script src="${basePath}/common/adminlte/bootstrap/js/bootstrap.min.js"></script>
            <!-- FastClick -->
            <script src="${basePath}/common/adminlte/plugins/fastclick/fastclick.js"></script>
            <!-- AdminLTE App -->
            <script src="${basePath}/common/adminlte/dist/js/app.min.js"></script>
            <script src="${basePath}/common/adminlte/plugins/slimScroll/jquery.slimscroll.min.js"></script>
            <!--moment.js-->
            <script src="${basePath}/common/js/moment.js"></script>
            <!-- dataTable -->
            <script src="${basePath}/common/adminlte/plugins/datatables/jquery.dataTables.js"></script>
            <script src="${basePath}/common/adminlte/plugins/datatables/dataTables.bootstrap.min.js"></script>
            <script src="${basePath}/common/js/dataTables.js"></script>
            <!-- form -->
            <script src="${basePath}/common/adminlte/plugins/bootstrap-validator/dist/js/bootstrap-validator.js"></script>
            <script src="${basePath}/common/adminlte/plugins/iCheck/icheck.min.js"></script>

            <script src="${basePath}/common/adminlte/plugins/datepicker/bootstrap-datepicker.js"></script>
            <script src="${basePath}/common/adminlte/plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js"></script>
            <!--jstree-->
            <script src="${basePath}/common/libs/jstree/dist/jstree.min.js"></script>
            <!-- treeview 和页面有冲突，移至每个使用的界面上-->

            <!-- AdminLTE for demo purposes -->
            <script src="${basePath}/common/adminlte/dist/js/demo.js"></script>
            <!--select2-->
            <script src="${basePath}/common/adminlte/plugins/select2/select2.full.min.js"></script>
            <!--toastr-->
            <script src="${basePath}/common/libs/toastr/toastr.min.js"></script>
            <!--加载菜单-->
            <script src="${basePath}/static/js/system/index.js"></script>

            <script type="text/javascript" src="${basePath}/common/js/base.js"></script>
            <script type="text/javascript" src="${basePath}/common/js/base-modal.js"></script>
            <script type="text/javascript" src="${basePath}/common/js/base-form.js"></script>
            <script type="text/javascript" src="${basePath}/common/js/base-org.js"></script>
            <script type="text/javascript" src="${basePath}/common/js/base-datasource.js"></script>
            <script type="text/javascript" src="${basePath}/common/js/base-role.js"></script>
        </body>
        <!-- 加入页面的的脚本 -->
        <script>
            //给外部js文件传递路径参数
            var basePath = "${basePath}";
            localStorage.setItem("basePath", "${basePath}");
            //加载首页 
            $(function () {
                //首页默认加载,若有缓存，刷新页面后跳到到刷新前的页面
                var lastpage = localStorage.getItem("lastpage");
                var userId = localStorage.getItem("userId");
                var roleSort = "${roleSort}";
                if (userId != null) {
                    //刷新页面后跳到到刷新前的页面
                    if (userId == "${userId}" && lastpage) {
                        loadPage(lastpage);
                    } else {
                        if(roleSort == 1){
                            loadPage("userAction_my.do");
                        }else if(roleSort == 2){
                            loadPage("menuAction_home.do");
                        }else if(roleSort == 3){
                            loadPage("menuAction_control.do");
                        }
                       
                    }
                } else {
                    loadPage("menuAction_home.do");
                }
                localStorage.setItem("userId","${userId}");
                $("a[data-url]").click(function (evt) {
                    loadPage($(this).data("url"));
                    $("ul.treeview-menu li").removeClass("active");
                    $(this).parent().addClass("active");
                });
            });

            //保留最后一次点击的窗口
            $(document).on("click", "a[py]", function (e) {
                var dataurl = $(this).attr("data-url");
                var basePath = localStorage.getItem("basePath");
                var userId = localStorage.getItem("userId");
                if (userId == "${userId}") {
                    if (basePath != null && dataurl != null && dataurl != '') {
                        localStorage.setItem("lastpage", dataurl);
                    }
                }
            });

            //注册进入后显示提示信息
            var msg = "${msg}";
            if (msg == "msg") {
                toastr.success("恭喜你，注册成功");
            }
        </script>

        </html>