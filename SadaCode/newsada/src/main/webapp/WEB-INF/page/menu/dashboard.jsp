<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <!DOCTYPE html>
    <style type="text/css">
        .sm-st {
            background: #fff;
            padding: 20px;
            -webkit-border-radius: 3px;
            -moz-border-radius: 3px;
            border-radius: 3px;
            margin-bottom: 20px;
            -webkit-box-shadow: 0 1px 0px rgba(0, 0, 0, 0.05);
            box-shadow: 0 1px 0px rgba(0, 0, 0, 0.05);
        }

        .sm-st-icon {
            width: 60px;
            height: 60px;
            display: inline-block;
            line-height: 60px;
            text-align: center;
            font-size: 30px;
            background: #eee;
            -webkit-border-radius: 5px;
            -moz-border-radius: 5px;
            border-radius: 5px;
            float: left;
            margin-right: 10px;
            color: #fff;
        }

        .sm-st-info {
            font-size: 12px;
            padding-top: 2px;
        }

        .sm-st-info span {
            display: block;
            font-size: 24px;
            font-weight: 600;
        }

        .orange {
            background: #fa8564 !important;
        }

        .tar {
            background: #45cf95 !important;
        }

        .sm-st .green {
            background: #86ba41 !important;
        }

        .pink {
            background: #AC75F0 !important;
        }

        .yellow-b {
            background: #fdd752 !important;
        }

        .stat-elem {

            background-color: #fff;
            padding: 18px;
            border-radius: 40px;

        }

        .stat-info {
            text-align: center;
            background-color: #fff;
            border-radius: 5px;
            margin-top: -5px;
            padding: 8px;
            -webkit-box-shadow: 0 1px 0px rgba(0, 0, 0, 0.05);
            box-shadow: 0 1px 0px rgba(0, 0, 0, 0.05);
            font-style: italic;
        }

        .stat-icon {
            text-align: center;
            margin-bottom: 5px;
        }

        .st-red {
            background-color: #F05050;
        }

        .st-green {
            background-color: #27C24C;
        }

        .st-violet {
            background-color: #7266ba;
        }

        .st-blue {
            background-color: #23b7e5;
        }

        .stats .stat-icon {
            color: #28bb9c;
            display: inline-block;
            font-size: 26px;
            text-align: center;
            vertical-align: middle;
            width: 50px;
            float: left;
        }

        .stat {
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            display: inline-block;
            margin-right: 10px;
        }

        .stat .value {
            font-size: 20px;
            line-height: 24px;
            overflow: hidden;
            text-overflow: ellipsis;
            font-weight: 500;
        }

        .stat .name {
            overflow: hidden;
            text-overflow: ellipsis;
        }

        .stat.lg .value {
            font-size: 26px;
            line-height: 28px;
        }

        .stat.lg .name {
            font-size: 16px;
        }

        .stat-col .progress {
            height: 2px;
        }

        .stat-col .progress-bar {
            line-height: 2px;
            height: 2px;
        }

        .item {
            padding: 30px 0;
        }

        .ibox-content {
            border-color: #e7eaec
        }
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
                                        <em>控制台（Dashboard）</em>用于展示当前系统中的统计数据、统计报表及重要实时数据</div>
                                </div>
                                <div class="panel-body">
                                    <div id="myTabContent" class="tab-content">
                                        <div class="tab-pane fade active in" id="one">
                                            <div class="row">
                                                <div class="col-sm-3 col-xs-6">
                                                    <div class="sm-st clearfix">
                                                        <span class="sm-st-icon st-red">
                                                            <i class="fa fa-users"></i>
                                                        </span>
                                                        <div class="sm-st-info">
                                                            <span>20</span>
                                                            今日鉴权次数
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-3 col-xs-6">
                                                    <div class="sm-st clearfix">
                                                        <span class="sm-st-icon st-violet">
                                                            <i class="fa fa-book"></i>
                                                        </span>
                                                        <div class="sm-st-info">
                                                            <span>390</span>
                                                            总鉴权次数
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-3 col-xs-6">
                                                    <div class="sm-st clearfix">
                                                        <span class="sm-st-icon st-blue">
                                                            <i class="fa fa-cubes"></i>
                                                        </span>
                                                        <div class="sm-st-info">
                                                            <span>43</span>
                                                            今日新增资产数
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-sm-3 col-xs-6">
                                                    <div class="sm-st clearfix">
                                                        <span class="sm-st-icon st-green">
                                                            <i class="fa fa-chain"></i>
                                                        </span>
                                                        <div class="sm-st-info">
                                                            <span>800</span>
                                                            组织总资产数
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="row">
                                                <div class="col-lg-7">
                                                    <div id="echartA" style="min-height: 250px; width: 100%;"></div>
                                                </div>
                                                <div class="col-lg-5">
                                                    <div class="ibox float-e-margins">
                                                        <div class="ibox-title">
                                                            <h4 align="center">登录信息统计</h4>
                                                        </div>
                                                        <div class="ibox-content">
                                                            <table class="table table-striped">
                                                                <thead>
                                                                    <tr>
                                                                        <th>浏览器</th>
                                                                        <th>IP地址</th>
                                                                        <th>登录位置</th>
                                                                        <th>时间</th>
                                                                    </tr>
                                                                </thead>
                                                                <tbody>
                                                                    <tr>
                                                                        <td>Chrome</td>
                                                                        <td>121.28.69.89</td>
                                                                        <td>河北省石家庄市</td>
                                                                        <td>刚刚</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>Firefox</td>
                                                                        <td>121.28.69.89</td>
                                                                        <td>河北省石家庄市</td>
                                                                        <td>30 分钟前</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>Opera</td>
                                                                        <td>121.28.69.89</td>
                                                                        <td>河北省石家庄市</td>
                                                                        <td>1 小时前</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>Safari</td>
                                                                        <td>121.28.69.89</td>
                                                                        <td>河北省石家庄市</td>
                                                                        <td>1 天前</td>
                                                                    </tr>  
                                                                </tbody>
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
    <script src="${basePath}static/js/echarts-theme.js"></script>
    <script src="${basePath}static/js/echarts.min.js"></script>
    <script src="${basePath}static/js/system/dashboard.js"></script>