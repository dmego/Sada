<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    request.setAttribute("basePath", basePath);
    %>
        <link rel="stylesheet" href="${basePath}/static/css/dashboard.css">
        <link href="${basePath}/common/libs/footable/footable.core.css" rel="stylesheet">
        <script src="${basePath}/common/adminlte/plugins/jQuery/jQuery-2.2.0.min.js"></script>
        <script src="${basePath}/common/adminlte/bootstrap/js/bootstrap.min.js"></script>
        <script src="${basePath}/common/libs/footable/footable.all.min.js"></script>
        </style>
        <div id="main" role="main">
            <div class="tab-content tab-addtabs">
                <div id="content">
                    <div class="row">
                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                            <!-- END RIBBON -->
                            <div class="content">
                                <div class="panel panel-default panel-intro">
                                    <div class="panel-heading">
                                        <div class="panel-lead">
                                            <em>区块链控制台（Dashboard）</em>用于展示后台区块链网络的区块、节点、交易等信息</div>
                                        <ul class="nav nav-tabs">
                                            <li class="active">
                                                <a href="#one" data-toggle="tab">控制台</a>
                                            </li>
                                            <li>
                                                <a href="#two" data-toggle="tab">区块详情</a>
                                            </li>
                                        </ul>
                                    </div>
                                    <div class="panel-body">
                                        <div id="myTabContent" class="tab-content">
                                            <div class="tab-pane fade active in" id="one">
                                                <div class="row">
                                                    <div class="col-sm-3 col-xs-6">
                                                        <div class="sm-st clearfix">
                                                            <div class="count-card dark-card card">
                                                                <div class="card-body">
                                                                    <h1 id="blockNum"></h1>
                                                                    <h4>
                                                                        <span aria-hidden="true" class="fa fa-cube"></span>&nbsp;&nbsp;区块</h4>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-3 col-xs-6">
                                                        <div class="sm-st clearfix">
                                                            <div class="count-card light-card card">
                                                                <div class="card-body">
                                                                    <h1 id="txSum"></h1>
                                                                    <h4>
                                                                        <span aria-hidden="true" class="fa fa-list-alt"></span>&nbsp;&nbsp;交易</h4>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-3 col-xs-6">
                                                        <div class="sm-st clearfix">
                                                            <div class="count-card dark-card card">
                                                                <div class="card-body">
                                                                    <h1 id="nodeNum"></h1>
                                                                    <h4>
                                                                        <span aria-hidden="true" class="fa fa-users"></span>&nbsp;&nbsp;节点</h4>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col-sm-3 col-xs-6">
                                                        <div class="sm-st clearfix">
                                                            <div class="count-card light-card card">
                                                                <div class="card-body">
                                                                    <h1 id="chaincodeNum"></h1>
                                                                    <h4>
                                                                        <span aria-hidden="true" class="fa fa-handshake-o"></span>&nbsp;&nbsp;链码</h4>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="row">
                                                    <div class="col-lg-6">
                                                        <div class="activity-stream">
                                                            <div class="card">
                                                                <div class="card-header">
                                                                    <h5>区块活动记录</h5>
                                                                </div>
                                                                <div class="card-body">
                                                                    <div class="scrollable-card">
                                                                        <div id="blockActivateRecordDiv">    
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>

                                                    <div class="col-lg-6">
                                                        <div class="activity-stream">
                                                            <div class="card">
                                                                <div class="card-header">
                                                                    <h5>网络拓扑图</h5>
                                                                </div>
                                                                <div class="card-body">
                                                                    <div id="container" style="height: 300px; width: 100%;"></div> 
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="tab-pane fade" id="two">
                                                <div class="row">
                                                    <div class="col-sm-12">
                                                        <div class="ibox float-e-margins">
                                                            <div class="ibox-title">
                                                                <h5>区块详情显示</h5>
                                                            </div>
                                                            <div class="ibox-content">
                                                                <table class="footable table table-stripped toggle-arrow-tiny" style="width:100%;table-layout: fixed;" data-page-size="8" id="blockTab">
                                                                    
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
                </div>
            </div>

        </div>
        <div class="modal inmodal" data-backdrop="false" id="myModal" tabindex="-1" role="dialog" aria-hidden="true">
            <div class="modal-dialog" style="width:700px">
                <div class="modal-content animated bounceInRight">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">
                            <span aria-hidden="true">&times;</span>
                            <span class="sr-only">关闭</span>
                        </button>
                        
                        <h4 class="modal-title"><i class="fa fa-cube cubeIcon"></i>&nbsp;&nbsp; Block Details</h4>
                    </div>
                    <div class="modal-body">
                        <div class="table-responsive">
                            <table class="table-striped table table-striped table-hover">
                                <tbody>
                                    <tr>
                                        <th>通道名称:</th>
                                        <td id="channelNameM"></td>
                                    </tr>
                                    <tr>
                                        <th>区块ID</th>
                                        <td id="blockIdM"></td>
                                    </tr>
                                    <tr>
                                        <th>区块高度</th>
                                        <td id="blockNumM"></td>
                                    </tr>
                                    <tr>
                                        <th>创建日期</th>
                                        <td id="dateM"></td>
                                    </tr>
                                    <tr>
                                        <th>交易数量</th>
                                        <td id="txNumM"></td>
                                    </tr>
                                    <tr>
                                        <th>Block Hash</th>
                                        <td id="currHashM">
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>Data Hash</th>
                                        <td id="dataHashM">
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>Prehash</th>
                                        <td id="preHashM">
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script>
            $(function () {
                $('#myModal').on('show.bs.modal',function () {
                    $("#myModel").append("<div class='modal-backdrop fade in' id='close'></div>")
                })

                 $('#myModal').on('hide.bs.modal',function () {
                    $("#close").remove()
                })
        });

        </script>
        <script>
            $(document).ready(function () {
               
            });

            function getDateStr(dateStr){
                var time = dateStr / 1000,
                    d_seconds,
                    d_minutes,
                    d_hours,
                    d_days,
                    timeNow = parseInt(new Date().getTime() / 1000),
                    d,

                    date = new Date(time * 1000);
                    
                   
                //小于10的在前面补0
               return date.toLocaleString();
            }

            function getDateDiff(dateStr) {
                var time = dateStr / 1000,
                    d_seconds,
                    d_minutes,
                    d_hours,
                    d_days,
                    timeNow = parseInt(new Date().getTime() / 1000),
                    d,

                    date = new Date(time * 1000),
                    
                    Y = date.getFullYear(),
                    M = date.getMonth() + 1,
                    D = date.getDate(),
                    H = date.getHours(),
                    m = date.getMinutes(),
                    s = date.getSeconds();
                //小于10的在前面补0
          //      alert(date.toLocaleString());
                if (M < 10) {
                    M = '0' + M;
                }
                if (D < 10) {
                    D = '0' + D;
                }
                if (H < 10) {
                    H = '0' + H;
                }
                if (m < 10) {
                    m = '0' + m;
                }
                if (s < 10) {
                    s = '0' + s;
                }

                d = timeNow - time;
                d_days = parseInt(d / 86400);
                d_hours = parseInt(d / 3600);
                d_minutes = parseInt(d / 60);
                d_seconds = parseInt(d);

                if (d_days > 0 && d_days < 3) {
                    return d_days + '天前';
                } else if (d_days <= 0 && d_hours > 0) {
                    return d_hours + '小时前';
                } else if (d_hours <= 0 && d_minutes > 0) {
                    return d_minutes + '分钟前';
                } else if (d_seconds < 60) {
                    if (d_seconds <= 0) {
                        return '刚刚';
                    } else {
                        return d_seconds + '秒前';
                    }
                } else if (d_days >= 3 && d_days < 30) {
                    return M + '-' + D + ' ' + H + ':' + m;
                } else if (d_days >= 30) {
                    return Y + '-' + M + '-' + D + ' ' + H + ':' + m;
                }
            }


            function blockDetail(id){
                var blockNum = $("#blockNum" + id).html();
                var txNum = $("#txNum" + id).html();
                var currHash = $("#currHash" + id).html();
                var preHash = $("#preHash" + id).html();
                var dataHash = $("#dataHash" + id).html();
                var txId = $("#txId" + id).html();
                var channelName = $("#channelName" + id).val();
                var date = $("#date" + id).val();
                //var timestamp = $("#timestamp" + id).val();
                $("#blockNumM").html(blockNum);
                $("#txNumM").html(txNum);
                $("#currHashM").html(currHash);
                $("#preHashM").html(preHash);
                $("#dataHashM").html(dataHash);
                $("#txIdM").html(txId);
                $("#channelNameM").html(channelName);
                $("#blockIdM").html(id);
                $("#dateM").html(date);

                // timestamp = Date.parse(timestamp.replace());
                

            }

            $(document).ready(function() {
                $.get('${pageContext.request.contextPath}/assetAction_queryBlockInfo.do', function(data2) {
                    if(!data2.success){
                        return;
                    }
                   
                    var data = eval("(" + data2.data + ")");
                   var blockNum2 = parseInt(data.blockNum) + 1;
                    $("#blockNum").html(blockNum2);
                    $("#txSum").html(data.txSum);
                    $("#nodeNum").html(4);
                    $("#chaincodeNum").html(1);

                    var count = 0;
                    var blockActivateContent = '<section style="position: relative; font-size: 80%; font-weight: 300; padding: 10px 0px; width: 95%; margin: 0px auto;">\
                                                    <div style="position: absolute; top: 0px; height: 100%; width: 2px; background: rgb(160, 178, 184) none repeat scroll 0% 0%; left: 16px;"></div>\
                                                    ';
                    var blockInfo = '<thead>\
                                        <tr>\
                                            <th data-toggle="true">区块高度</th>\
                                            <th>交易数量</th>\
                                            <th colspan="2">数据哈希</th>\
                                            <th colspan="2">区块哈希</th>\
                                            <th colspan="2">上一区块</th>\
                                            <th colspan="2">交易ID</th>\
                                        </tr>\
                                    </thead>';
             
                    //var json = eval("(" + data.blocks + ")");
                   // alert(json);
                    $.each(data.blocks, function(index, item) {
             

                        if(count < 3){
                            //var timestampL = parseInt();
                 //           alert(item.date);
                            blockActivateContent += '\
                            <div style="position: relative; margin: 10px 0px; padding-left: 45px; text-align: left;">\
                                <div style="position: absolute; top: 0px; border-radius: 50%; width: 30px; height: 30px; margin-left: 1px; background: rgb(233, 240, 245) none repeat scroll 0% 0%; border: 2px solid rgb(13, 55, 153); display: flex; color: rgb(13, 55, 153); left: 0px;">\
                                    <span style="display: flex; width: 32px; height: 32px; position: relative; justify-content: center; align-self: center; align-items: center;">\
                                        <span aria-hidden="true" class="fa fa-cube"></span>\
                                    </span>\
                                </div>\
                                <div style="box-shadow: rgba(0, 0, 0, 0.118) 0px 1px 6px, rgba(0, 0, 0, 0.118) 0px 1px 4px; background-color: rgb(255, 255, 255); position: relative; width: 400px;">\
                                    <div style="top: 24px; left: 100%; border-color: transparent transparent transparent rgb(255, 255, 255);"></div>\
                                    <div style="background-color: rgb(98, 131, 208); padding: 10px; color: rgb(255, 255, 255); font-size: 13pt;">\
                                        <div style="font-weight: bold;">区块 ' + item.blockNum + '</div>\
                                        <div style="margin-top: -20px; float: right; text-align: right;">\
                                            <span aria-hidden="true" class="fa fa-play-circle"></span>\
                                        </div>\
                                    </div>\
                                    <div style="background-color: rgb(255, 255, 255); margin-bottom: 1em; line-height: 1.6; padding: 10px; min-height: 40px;">\
                                        <p class="jss75 jss84">\
                                            <b> Datahash:</b> ' + item.dataHash + '\
                                            <br>\
                                            <b> Number of Tx:</b> 1</p>\
                                        <h5>\
                                            <span class="badge badge-secondary">\
                                                <time datetime="' + item.date + '" title="' + item.date + '">' + getDateDiff(item.timestamp) +'</time>\
                                            </span>\
                                        </h5>\
                                        <div style="clear: both; display: table;"></div>\
                                    </div>\
                                </div>\
                                <div style="clear: both; display: table;"></div>\
                            </div>';
                        }

                        blockInfo += ' <tr>\
                                        <td style="overflow: hidden;text-overflow:ellipsis;white-space: nowrap;" id="blockNum' + item.id + '">' + item.blockNum + '</td>\
                                        <td style="overflow: hidden;text-overflow:ellipsis;white-space: nowrap;" id="txNum' + item.id + '">' + item.txNum + '</td>\
                                        <td style="overflow: hidden;text-overflow:ellipsis;white-space: nowrap;" colspan="2" id="dataHash' + item.id + '">' + item.dataHash + '</td>\
                                        <td style="overflow: hidden;text-overflow:ellipsis;white-space: nowrap;" colspan="2" id="currHash' + item.id + '"><a onclick="blockDetail(' + item.id + ')" data-toggle="modal" data-target="#myModal" href="#">' + item.currHash + '</a></td>\
                                        <td style="overflow: hidden;text-overflow:ellipsis;white-space: nowrap;" colspan="2" id="preHash' + item.id + '">' + item.preHash + '</td>\
                                        <td style="overflow: hidden;text-overflow:ellipsis;white-space: nowrap;" colspan="2" id="txId' + item.id + '">' + item.txInfo[0] + '</td>\
                                        <input type="hidden" id="channelName' + item.id + '" value="' + data.channelName + '">\
                                        <input type="hidden" id="date' + item.id + '" value="' + getDateStr(item.timestamp) + '">\
                                 </tr>';

                        // blockInfo += ' <tr>\
                        //                 <td  id="blockNum' + item.id + '">' + item.blockNum + '</td>\
                        //                 <td  id="txNum' + item.id + '">' + item.txNum + '</td>\
                        //                 <td  id="dataHash' + item.id + '" colspan="2">' + item.dataHash + '</td>\
                        //                 <td id="currHash' + item.id + '" colspan="2"><a onclick="blockDetail(' + item.id + ')" data-toggle="modal" data-target="#myModal" href="#">' + item.currHash + '</a></td>\
                        //                 <td  id="preHash' + item.id + '" colspan="2">' + item.preHash + '</td>\
                        //                 <td  id="txId' + item.id + '" colspan="2">' + item.txInfo[0] + '</td>\
                        //                 <input type="hidden" id="channelName' + item.id + '" value="' + data.channelName + '">\
                        //                 <input type="hidden" id="date' + item.id + '" value="' + getDateStr(item.timestamp) + '">\
                        //          </tr>';



                        count++;
                    });
                    blockActivateContent += '<div style="display: table; clear: both;"></div>\
                                        </section>';
                    blockInfo += ' </tbody>\
                                    <tfoot>\
                                        <tr>\
                                            <td colspan="5">\
                                                <ul class="pagination pull-right"></ul>\
                                            </td>\
                                        </tr>\
                                    </tfoot>';
                    $("#blockActivateRecordDiv").html(blockActivateContent);
                    $("#blockTab").html(blockInfo);
                    $(".footable").footable();
                });
            });

            
        </script>
        <script src="${basePath}static/js/echarts-theme.js"></script>
        <script src="${basePath}static/js/echarts.min.js"></script>
        <script src="${basePath}static/js/system/adminboard.js"></script>