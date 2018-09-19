<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<link rel="stylesheet" href="${basePath}/common/css/site.css">
	<div class="wrapper wrapper-content" style="background-color: #ecf0f5;padding: 20px;">
		<div class="row">
			<div class="col-sm-3">
				<div class="ibox float-e-margins">
					<div class="ibox-content" style="background-color: #fff;">
						<div class="file-manager">
							
							<div class="hr-line-dashed"></div>
							<button class="btn btn-primary btn-block" onclick="addAsset()">上传资产</button>
							<div class="hr-line-dashed"></div>
							<div>
								<form action="" method="get" onsubmit="return false;">
									<div class="input-group">
										<input name="q" id="searchTextInput" class="form-control" placeholder="搜索" type="text">
										<span class="input-group-btn">
											<button type="button" name="search" id="search-btn" class="btn btn-flat" onclick="searchByText()">
												<i class="fa fa-search"></i>
											</button>
										</span>
										<div class="menuresult list-group sidebar-form hide" style="width: 210px;">
										</div>
									</div>
								</form>
							</div>
							<h5>文件夹</h5>

							<div id="typeTree">

							</div>
							<h5 class="tag-title">标签</h5>
							<ul class="tag-list" style="padding: 0" id="tagUl">
								
							</ul>
							<div class="clearfix"></div>
						</div>
					</div>
				</div>
			</div>
			<div class="col-sm-9 animated fadeInRight">
				<div class="row">
					<div class="col-sm-11" id="assetDiv">


					</div>
					<div class="text-center" id="pageDiv" style="display: none;">
						<ul class="pagination">
							<li id="firstPageLi">
								<a href="#" name="0" id="firstPage" onclick="getItem(null,null,null,this.name);">首页</a>
							</li>
							<li id="prePageLi">
								<a href="#" name="0" id="prePage" onclick="getItem(null,null,null,this.name);">上一页</a>
							</li>
							<li class="disabled">
								<a href="#">
									<span id="currPage">1</span>/
									<span id="allPage">1</span>
									</span>
								</a>
							</li>
							<li id="postPageLi">
								<a href="#" name="0" id="postPage" onclick="getItem(null,null,null,this.name);">下一页</a>
							</li>
							<li id="rearPageLi">
								<a href="#" name="0" id="rearPage" onclick="getItem(null,null,null,this.name);">尾页</a>
							</li>

						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>

	




	<script src="${pageContext.request.contextPath }/static/js/jquery.min.js"></script>
	<script src="${pageContext.request.contextPath }/static/js/bootstrap.min.js"></script>
	<script src="${pageContext.request.contextPath }/static/js/content.min.js"></script>
	<script src="${pageContext.request.contextPath }/common/libs/jstree/dist/jstree.min.js"></script>
	<script type="text/javascript">
		var gtype = "";
		var gtag = "";
		var gcb = '';
		var gpage = 0;
		var gname = 0;

		// function getTreeMsg(id,cb) {
		// 	$
		// 			.post(
		// 					'${pageContext.request.contextPath}/assetAction_getTreeMsgById.do',
		// 					{
		// 						'id' : id
		// 					}, function(data, textStatus, xhr) {
		// 						if (data != null || data.length > 0) {
		// 							if (data == "error") {
		// 								alert("获取数据发生错误！");
		// 							}
		// 							else{
		// 								cb(data);
		// 							}

		// 						} else {
		// 							alert("获取数据发生错误！");
		// 						}

		// 					});

		// }

		function goto() {
			window.loadPage(basePath + "/assetAction_compar.do");
		}


		function getById(id) {
			$.post(
					'${pageContext.request.contextPath}/assetAction_getAssetById.do', {
						'id': id
					},
					function (data, textStatus, xhr) {
						if (data != null || data.length > 0) {
							if (data == "error") {
								alert("获取数据发生错误！");
							} else {

//--------------------------------------
//将资产详细信息渲染到页面

var fileName = data.data.fileName;
if(data.data.commonType != "图片"){
	var indexOf = fileName.indexOf(".");
	fileName = fileName.substr(0,indexOf) + ".png";	
}

var content = '<div class="row">\
            <div class="col-md-12 col-xs-12">\
                <div class="widget widget-article widget-shadow">\
					<div class="widget-body paddingAsset">\
						<img class="cover-image coverImages overlay-scale" src="' + basePath + data.data.filePath + '/' + fileName + '" alt="资产图片">\
                    </div>\
                    <div class="widget-body">\
                        <div class="ibox float-e-margins">\
                            <div>\
                                <h4 align="center" class="widget-title">';
                                var name = data.data.name;
								if (name != null) {
									content += name;
								}

                              content += '</h4>\
                            </div>\
                            <div class="ibox-content">\
                                <table class="table table-striped">\
                                    <thead>\
                                        <tr>\
                                            <th style="font-weight:bold;">关键信息</th>\
                                        </tr>\
                                    </thead>\
                                    <tbody>';
                                    var keyInfo = data.data.keyInfo;
									var keyInfoJson = eval("(" + keyInfo + ")");
									for (var key in keyInfoJson) {
										content += '<tr><td style="font-weight:bold;">' + key +
											'</td><td>' + keyInfoJson[key] + '</td></tr>';
									}
                                    content += '</tbody>\
                                </table>\
                                <table class="table table-striped">\
                                    <thead>\
                                        <tr>\
                                            <th style="font-weight:bold;">其他信息</th>\
                                        </tr>\
                                    </thead>\
                                    <tbody>\
                                        <tr>\
                                            <td style="font-weight:bold;">资产文件类型</td>\
                                            <td>' + data.data.commonType + '(' + data.data.fileType + ')</td>\
                                        </tr>\
                                        <tr>\
                                            <td style="font-weight:bold;">资产类型</td>\
                                            <td>' + data.data.assetType + '</td>\
                                        </tr>\
                                        <tr>\
                                            <td style="font-weight:bold;">创建时间</td>\
                                            <td>' + data.data.createDate + '</td>\
                                        </tr>\
                                        <tr>\
                                            <td style="font-weight:bold;">更新时间</td>\
                                            <td>' + data.data.updateDate + '</td>\
                                        </tr>\
                                        <tr>\
                                            <td style="font-weight:bold;">标签</td>\
                                            <td>' + data.data.tag + '</td>\
                                        </tr>\
                                    </tbody>\
                                </table>\
                            </div>\
                        </div>\
                    </div>\
                </div>\
            </div>\
        </div>';
//----------------------------------------

								content +=
									'<div class="user-button">\
                            <div class="row">\
                            	<div class="col-sm-3"></div>\
                                <div class="col-sm-2">\
                                    <button type="button" class="btn btn-default btn-sm btn-block" onclick="deleteAsset(\'' +
									data.data.id +
									'\')">删除</button>\
                                </div>\
                                <div class="col-sm-2">\
                                        <button type="button" class="btn btn-primary btn-sm btn-block" onclick="assetValidate(\'' + data.data.id + '\');">鉴权</button>\
                                </div>\
                                <div class="col-sm-2">\
                                        <a href="fileDownloadServlet?filePath=' +
									data.data.filePath + '&fileName=' + data.data.fileName +
									'"><button type="button" class="btn btn-secondary btn-sm" >下载原件</button></a>\
                                </div>\
                                <div class="col-sm-3"></div>\
                            </div>\
                        </div>\
                    </div>\
                </div>';

								$("#assetDiv").html(content);
								//alert(content);
								$("#pageDiv").hide();
							}

						} else {
							alert("获取数据发生错误！");
						}

					});

		}




		function getItem(type, tag, name, page, cb) {
			if (type != null) {
				gtype = type;
			}
			if (tag != null) {
				gtag = tag;
			}
			if (name != null) {
				gname = name;
			}
			if (page != null) {
				gpage = page;
			}

			$.post(
				'${pageContext.request.contextPath}/assetAction_getAssetByParam.do', {
					'commonType': gtype,
					'tag': gtag,
					'name': gname,
					'page': gpage
				},
				function (data, textStatus, xhr) {
					if (data != null || data.length > 0) {
						if (data == "error") {
							alert("获取数据发生错误！");
						} else {
							var content = "";
							// alert(data.data.list);
							if(data.data.list == null || data.data.list == ''){

								content = '<div class="site-page" style="margin-top: 65px;">\
    	<div class="page vertical-align text-center animation-scale-up page-error">\
        	<div class="page-content vertical-align-middle">\
            	<div>\
                	<i class="fa fa-address-card-o fa-5x" aria-hidden="true"></i>\
                	<h3>没有资产</h3>\
                	<p>您还没有' + gtype + '资产，请上传</p>\
            	</div>\
        	</div>\
    	</div>\
	</div>';

								// content = '<div class="page-content vertical-align-middle">\
            					// 				<header>\
                				// 					<i class="icon pe-id" aria-hidden="true"></i>\
                				// 					<h3>没有资产</h3>\
                				// 					<p>您还没有' + gtype + '资产</p>\
            					// 				</header>\
        						// 			</div>';
								
								
								// content = '<div class="middle-box text-center animated fadeInDown">\
        						// 			<div class="error-desc">\
            					// 				您还没有' + gtype + '资产\
            					// 				<br/>\
            					// 				</div>\
								// 			</div>';
							}
							var treeStr = "[";
							$
								.each(
									data.data.list,
									function (index, item) {
										treeStr += '{"text":"' + item.name + '","id":"' + item.id + '","data":"leaf","type":"' + item.fileType +
											'"},';
										var createDate = item.createDate;
										// alert(createDate);
										var date = createDate.substr(0, 10);
										content += '<div class="file-box"><div class="file">';
										if(item.commonType != "图片"){
											content += '<div class="icon">\
                                        <a href="#" onclick="getById(\'' + item.id +
											'\')"><i class="fa fa-file-pdf-o"></i>\
                                    </div>';
										}
										else{
											content += '<a href="#" onclick="getById(\'' + item.id +
											'\')"><span class="corner"></span><div class="image"><img alt="image" class="img-responsive" src="' +
											basePath + item.filePath + '/' + item.fileName + '" onclick="getById(\'' + item.id +
											'\')"></div>';
										}

										content += '<div class="file-name" onclick="getById(\'' + item.id + '\')">' +
											item.name +
											'<br/><small>添加时间：' +
											date +
											'</small></div></a></div></div>';
									});
							$("#assetDiv").html(content);
							//添加动态效果
							$(".file-box").each(function () {
								animationHover(this, "pulse")
							})

							//设置分页信息
							if (data.data.hasPage == "1") {
								$("#prePage").prop('name', data.data.pagingBean.prePage);
								$("#postPage").prop('name', data.data.pagingBean.postPage);
								$("#currPage").html(parseInt(data.data.pagingBean.currentPage) + 1);
								$("#allPage").html(data.data.pagingBean.totalPage);
								$("#rearPage").prop('name', parseInt(data.data.pagingBean.totalPage) - 1);
								if (data.data.pagingBean.isLast == "1") {
									//alert("islast");
									$("#postPageLi").addClass('disabled');
									$("#rearPageLi").addClass('disabled');
								} else {
									$("#postPageLi").removeClass('disabled');
									$("#rearPageLi").removeClass('disabled');
								}
								if (data.data.pagingBean.isFirst == "1") {
									$("#prePageLi").addClass('disabled');
									$("#firstPageLi").addClass('disabled');
								} else {
									$("#prePageLi").removeClass('disabled');
									$("#firstPageLi").removeClass('disabled');
								}
								$("#pageDiv").show();
							} else {
								$("#pageDiv").hide();
							}


							treeStr += "]";
							if (cb != null) {
								if (cb != '') {
									gcb = cb;
								}
								gcb(eval("(" + treeStr + ")"));
							}

							return treeStr;
						}

					} else {
						alert("获取数据发生错误！");
					}


				});


		}

		function getTag() {
			$.post('${pageContext.request.contextPath}/assetAction_getUserTag.do', {}, function (data, textStatus, xhr) {
				if (data != null) {
					if (!data.success) {
						alert(data.message);
					} else {
						var content = '';
						$.each(data.data, function (index, item) {
							content += '<li><a href="#" onclick="getItem(\'\',\'' + item.name + '\',\'\',0)">' + item.name + '</a></li>';
						});
						$("#tagUl").html(content);
					}

				} else {
					alert("删除出错！");
				}
			});
		}

		function deleteAsset(id) {
			modals.confirm("是否确定删除？", function () {
                $.post('${pageContext.request.contextPath}/assetAction_deleteAsset.do', {
					'id': id,
				}, function (data, textStatus, xhr) {
					if (data != null) {
						toastr.success(data.message);
						getItem(null, null, null, null);
					} else {
						toastr.error("删除出错！");
					}
				});
            })





			
			
		}

		function addAsset() {
			var addAssetWin = "addAssetWin";
			modals.openWin({
				winId: addAssetWin,
				width: 700,
				title: '上传资产',
				url: basePath + '/assetAction_add.do',
				hideFunc: function () {
					modals.hideWin(addAssetWin);
					$('body').addClass('modal-open');
					window.loadPage(basePath + "/assetAction_manage.do");
				}
			});
		}


		function assetValidate(id){
			var coreAuthWin = "coreAuthWin";
            modals.openWin({
                winId: coreAuthWin,
                title: '资产鉴权',
                width: '600px',
                url: basePath + "/assetAction_auth.do?id=" + id
            });
		}

		// function displayFunc(id){
		// 	$("#all").removeClass("active");
		// 	$("#photo").removeClass("active");
		// 	$("#doc").removeClass("active");
		// 	$("#video").removeClass("active");
		// 	$("#" + id).addClass("active");
		// 	gtype = $("#" + id).html();
		// 	if(id == "all"){
		// 		gtype = '';
		// 	}
		// 	getItem(gtype,0);
		// }

		function searchByText() {
			var searchText = $("#searchTextInput").val();
			getItem('', '', searchText, 0);
		}

		$(document).ready(function () {

			$("#typeTree").jstree({
				"core": {
					"check_callback": true,
					'data': function (node, cb) {
						// alert(node.data);
						if (node.id === '#') {
							//初始化一级节点------TODO 从后台获取
							cb([{
								"text": "图片",
								"data": "图片",
								"children": true
							}, {
								"text": "文档",
								"data": "文档",
								"children": true
							}, {
								"text": "视频",
								"data": "视频",
								"children": true
							}]);
						} else if (node.data != "leaf") {
							getItem(node.data, '', '', 0, cb);

						} else {
							getById(node.id);
						}

					}
				},
				"plugins": ["types", "dnd"],
				"types": {
					"default": {
						"icon": "fa fa-folder"
					},
					"html": {
						"icon": "fa fa-file-code-o"
					},

					"pdf": {
						"icon": "fa fa-file-pdf-o"
					},
					"video": {
						"icon": "fa fa-file-video-o"
					},
					"word": {
						"icon": "fa fa-file-word-o"
					},
					"audio": {
						"icon": "fa fa-file-audio-o"
					},

					"img": {
						"icon": "fa fa-file-image-o"
					},
					"png": {
						"icon": "fa fa-file-image-o"
					},
					"jpg": {
						"icon": "fa fa-file-image-o"
					},
					"bmp": {
						"icon": "fa fa-file-image-o"
					},

				},

			});

			$('#typeTree').bind("activate_node.jstree", function (obj, e) {
				if (e.node.data != "leaf") {
					getItem(e.node.data, '', '', 0, null);
				} else {
					getById(e.node.id);
				}
			});

			$('#typeTree').bind("open_node.jstree", function (obj, e) {
				e.instance.set_icon(e.node, "fa fa-folder-open"); 
			});

			$('#typeTree').bind("close_node.jstree", function (obj, e) {
				e.instance.set_icon(e.node, "fa fa-folder"); 
			});

			

			getItem('', '', '', 0);
			getTag();
		});
	</script>