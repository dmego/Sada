<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
    <html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Insert title here</title>
        <link href="${pageContext.request.contextPath }/static/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath }/static/css/font-awesome.min.css" rel="stylesheet">

        <link href="${pageContext.request.contextPath }/static/css/animate.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath }/static/css/style.min.css" rel="stylesheet">
    </head>

    <body class="gray-bg">
        <div class="wrapper wrapper-content">
            <div class="row animated fadeInRight">

                <div class="col-sm-12">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <h5>个人资料</h5>
                        </div>
                        <div>
                            <div class="ibox-content no-padding border-left-right">
                                <img alt="image" class="img-responsive" src="img/profile_big.jpg">
                            </div>
                            <div class="ibox-content profile-content">
                                <h4>
                                    <strong>Beaut-zihan</strong>
                                </h4>
                                <p>
                                    <i class="fa fa-map-marker"></i> 上海市闵行区绿地科技岛广场A座2606室</p>
                                <h5>
                                    关于我
                                </h5>
                                <p>
                                    会点前端技术，div+css啊，jQuery之类的，不是很精；热爱生活，热爱互联网，热爱新技术；有一个小的团队，在不断的寻求新的突破。
                                </p>
                                <div class="row m-t-lg">
                                    <div class="col-sm-4">
                                        <span class="bar">5,3,9,6,5,9,7,3,5,2</span>
                                        <h5>
                                            <strong>169</strong> 文章</h5>
                                    </div>
                                    <div class="col-sm-4">
                                        <span class="line">5,3,9,6,5,9,7,3,5,2</span>
                                        <h5>
                                            <strong>28</strong> 关注</h5>
                                    </div>
                                    <div class="col-sm-4">
                                        <span class="bar">5,3,2,-1,-3,-2,2,3,5,2</span>
                                        <h5>
                                            <strong>240</strong> 关注者</h5>
                                    </div>
                                </div>
                                <div class="user-button">
                                    <div class="row">
                                        <div class="col-sm-6">
                                            <button type="button" class="btn btn-primary btn-sm btn-block">
                                                <i class="fa fa-envelope"></i> 发送消息</button>
                                        </div>
                                        <div class="col-sm-6">
                                            <button type="button" class="btn btn-default btn-sm btn-block">
                                                <i class="fa fa-coffee"></i> 赞助</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script src="${pageContext.request.contextPath }/static/js/jquery.min.js"></script>
        <script src="${pageContext.request.contextPath }/static/js/bootstrap.min.js"></script>
        <script src="${pageContext.request.contextPath }/static/js/content.min.js"></script>
        <script>
            $(document).ready(function () {
                $("#loading-example-btn").click(function () {
                    btn = $(this);
                    simpleLoad(btn, true);
                    simpleLoad(btn, false)
                })
            });

            function simpleLoad(btn, state) {
                if (state) {
                    btn.children().addClass("fa-spin");
                    btn.contents().last().replaceWith(" Loading")
                } else {
                    setTimeout(function () {
                        btn.children().removeClass("fa-spin");
                        btn.contents().last().replaceWith(" Refresh")
                    }, 2000)
                }
            };
        </script>


    </body>

    </html>