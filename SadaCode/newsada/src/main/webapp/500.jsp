<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>500错误</title>
    <link href="${pageContext.request.contextPath }/static/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath }/static/css/font-awesome.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath }/static/css2/animate.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath }/static/css2/style.min.css" rel="stylesheet">
</head>

<body class="gray-bg">
    <div class="middle-box text-center animated fadeInDown">
        <h1>500</h1>
        <h3 class="font-bold">服务器内部错误</h3>

        <div class="error-desc">
            服务器好像出错了...
            <br/>您可以返回主页看看
            <br/><a href="index.html" class="btn btn-primary m-t">主页</a>
        </div>
    </div>
    <script src="${pageContext.request.contextPath }/static/js/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath }/static/js/bootstrap.min.js"></script>
    
</body>

</html>
