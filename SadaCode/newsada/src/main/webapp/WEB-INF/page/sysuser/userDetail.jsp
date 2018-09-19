<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    request.setAttribute("basePath", basePath);
    %>
<!DOCTYPE html>
<html lang="zh-cn">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>个人信息</title>
        <meta name="description" content="Restyling jQuery UI Widgets and Elements" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
		<link rel="stylesheet" href="${basePath}common/css/bootstrap.min.css" />
		<link rel="stylesheet" href="${basePath}common/css/font-awesome.min.css" />
		<link rel="stylesheet" href="${basePath}common/css/ace-fonts.css" />
		<link rel="stylesheet" href="${basePath}common/css/ace.min.css" id="main-ace-style" />
		
		<script src="${basePath}/common/js/ace-extra.min.js"></script>
      
    </head>

    <body class="no-skin">
			<div class="main-content">
				<!-- #section:basics/content.breadcrumbs -->
				<div class="breadcrumbs" id="breadcrumbs">
					<ul class="breadcrumb">
						<li>
							<i class="ace-icon fa fa-home home-icon"></i>
							<a href="index.jsp">应用首页</a>						</li>
						<li>
							<a href="javascript:void(0)">用户个人资料</a>						</li>
					</ul><!-- /.breadcrumb -->

					
				</div>

				<!-- /section:basics/content.breadcrumbs -->
				<div class="page-content">

					<!-- /section:settings.box -->
					<div class="page-content-area">
						<div class="row">
							<div class="col-xs-12">
								<div id="user-profile-2" class="user-profile">
										<div class="tabbable">
											
											<div class="tab-content no-border padding-24">
												<div id="home" class="tab-pane in active">
													<div class="row">
														<div class="col-xs-12 col-sm-3 center">
															<span class="profile-picture">
																<%-- <img class="editable img-responsive" alt="Alex's Avatar" id="avatar2" src="${basePath}/common/images/profile-pic.jpg" /> --%>	
																<img class="editable img-responsive" alt="Alex's Avatar" id="avatar2" src="${basePath}${user.userPic}" />				
															</span>

															<div class="space space-4"></div>


															<a href="#" class="btn btn-sm btn-block btn-primary">
																<i class="icon-envelope-alt bigger-110"></i>
																<span class="bigger-110">发送邮件</span>															</a>														</div><!-- /span -->

														<div class="col-xs-12 col-sm-9">
															<h4 class="blue">
																<span class="middle">${user.name}</span>

																<span class="label label-purple arrowed-in-right">
																	<i class="icon-circle smaller-80 align-middle"></i>
																	在线																</span>															</h4>

															<div class="profile-user-info">
																<div class="profile-info-row">
																	<div class="profile-info-name"> 昵称</div>

																	<div class="profile-info-value">
																		<span>${user.nickName}</span>																	</div>
																</div>
																
																<div class="profile-info-row">
																	<div class="profile-info-name"> 性别 </div>

																	<div class="profile-info-value">
																		<span>${user.sexName}</span>																	</div>
																</div>

																<div class="profile-info-row">
																	<div class="profile-info-name"> 联系电话 </div>

																	<div class="profile-info-value">
																		<i class="icon-map-marker light-orange bigger-110"></i>
																		<span>${user.mobile}</span>
																																</div>
																</div>

																<div class="profile-info-row">
																	<div class="profile-info-name"> 出生日期 </div>

																	<div class="profile-info-value">
																		<span>${user.dbirthday}</span>	
																		<%-- <span>${user.createtime}</span>	 --%>							
																	</div>
																		
																</div>
															</div>

															<div class="hr hr-8 dotted"></div>

															<div class="profile-user-info">
																<div class="profile-info-row">
																	<div class="profile-info-name"> 电子邮箱 </div>

																	<div class="profile-info-value">
																		<a href="#" target="_blank">${user.email}</a>																	</div>
																</div>
															</div>
														</div><!-- /span -->
													</div><!-- /row-fluid -->

													<div class="space-20"></div>

													<div class="row">
														<div class="col-xs-12 col-sm-6">
															<div class="widget-box transparent">
																<div class="widget-header widget-header-small">
																	<h4 class="smaller">
																		<i class="icon-check bigger-110"></i>
																		自我介绍																	</h4>
																</div>

																<div class="widget-body">
																	<div class="widget-main">
																		<p>
																			我是一 名本科毕业生，毕业刚半年，学的是英语专业，大学毕业后一直从事翻译员一职。一直以来都很想从事教育工作，但因为一些特殊的原因一直未去考取教师资格证， 这段时间才开始准备考取此证书。不过，我认为，一个人的能力并非完全是由一纸证书来权衡的；同时，暂时还未取得教师资格证并不能说明以后不会有。我忠诚地 希望贵单位能够给我一次任职的机会，让我能够在教育行业中干出一番事业！																		</p>
																	</div>
																</div>
															</div>
														</div>

														<div class="col-xs-12 col-sm-6">
															<div class="widget-box transparent">
																<div class="widget-header widget-header-small header-color-blue2">
																	<h4 class="smaller">
																		<i class="icon-lightbulb bigger-120"></i>
																		个人资产																	</h4>
																</div>

																<div class="widget-body">
																	<div class="widget-main padding-16">
																		<div class="clearfix">
																			
																				<div class="grid3 center">
																					<div class="easy-pie-chart percentage" data-percent="45" data-color="#CA5952">
																						<span class="percent">${assetStatisticPhoto}</span>%																				
																					</div>

																					<div class="space-2"></div>
																						图片																			
																				</div>
																			
																			<div class="grid3 center">
																					<div class="easy-pie-chart percentage" data-percent="45" data-color="#CA5952">
																						<span class="percent">${assetStatisticDoc}</span>%																				
																					</div>

																					<div class="space-2"></div>
																						文档																			
																				</div>
																				<div class="grid3 center">
																					<div class="easy-pie-chart percentage" data-percent="45" data-color="#CA5952">
																						<span class="percent">${assetStatisticVideo}</span>%																				
																					</div>

																					<div class="space-2"></div>
																						视频																			
																				</div>
																											
																		</div>

																		<div class="hr hr-16"></div>

																		<div class="profile-skills">
																			<%-- <c:forEach items="${assetStatistic}" var="item">
																			<div class="progress">
																				<div class="progress-bar" style="width:${item.value}%">
																					<span class="pull-left">${item.key}</span>
																					<span class="pull-right">${item.value}%</span>																				
																				</div>
																			</div>
                                           									</c:forEach> --%>
																			
																			<div class="progress">
																				<div class="progress-bar progress-bar-warning" style="width:${assetStatisticPhoto}%">
																					<span class="pull-left">图片</span>

																					<span class="pull-right">${assetStatisticPhoto}%</span>																				</div>
																			</div> 

																			<div class="progress">
																				<div class="progress-bar progress-bar-purple" style="width:${assetStatisticDoc}%">
																					<span class="pull-left">文档</span>

																					<span class="pull-right">${assetStatisticDoc}</span>																				</div>
																			</div>

																			<div class="progress">
																				<div class="progress-bar progress-bar" style="width:${assetStatisticVideo}%">
																					<span class="pull-left">视频</span>

																					<span class="pull-right">${assetStatisticVideo}%</span>																				</div>
																			</div>
																		</div>
																	</div>
																</div>
															</div>
														</div>
													</div>
												</div><!-- #home -->

											</div>
										</div>
									</div>
							</div><!-- /.col -->
						</div><!-- /.row -->
					</div><!-- /.page-content-area -->
				</div><!-- /.page-content -->
			</div><!-- /.main-content -->

			<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
				<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>			</a>		</div><!-- /.main-container -->

		<!-- basic scripts -->

		<!--[if !IE]> -->
		<script type="text/javascript">
			window.jQuery || document.write("<script src='assets/js/jquery.min.js'>"+"<"+"/script>");
		</script>

		<!-- <![endif]-->

		<!--[if IE]>
		<script type="text/javascript">
		 window.jQuery || document.write("<script src='assets/js/jquery1x.min.js'>"+"<"+"/script>");
		</script>
		<![endif]-->
		<script type="text/javascript">
			if('ontouchstart' in document.documentElement) document.write("<script src='assets/js/jquery.mobile.custom.min.js'>"+"<"+"/script>");
		</script>
		<script src="assets/js/bootstrap.min.js"></script>

		<!-- page specific plugin scripts -->

		<!-- ace scripts -->
		<script src="assets/js/ace-elements.min.js"></script>
		<script src="assets/js/ace.min.js"></script>
	</body>
</html>