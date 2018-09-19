<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     <%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    request.setAttribute("basePath", basePath);
    %>
        <script type="text/javascript" src="${basePath}/common/libs/webuploader/dist/webuploader.js"></script>
        
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
            .alert1.alert-warning1.alert-dismissible1 {}
            .alert-warning1 hr {
              border-top-color: #f7e1b5;
            }
            .alert-warning1 .alert-link1 {
              color: #c87f0a;
            }
        </style>
        </style>
    <div>
        <input type="hidden" name="id" id="id" value="${id}">
        <input type="hidden" name="fileName" id="fileName" value="${fileName}">
        <input type="hidden" name="filePath" id="filePath" value="${filePath}">
    </div>
    <div id="wrapDiv">
        
    </div>




<script type="text/javascript">


    $(document).ready(function() {
        // var compareData;
        // var id;
        // var fileName;
        // var filePath;
        // id = $("#id").val();
        // fileName = $("#fileName").val();
        // filePath = $("#filePath").val();
        //  $.post('assetAction_getFileKeyInfoAndCompare.do', {
        //     'id':id,
        //     'fileName': fileName,
        //     'filePath': filePath
        // },
        // function (data, textStatus, xhr) {
        //     // $("#getKeyInfoStatus").hide();
        //     // $("#keyInfoDivWrap").show();
        //     compareData = data.data;
        //     //a2 = setInterval('removeL(9)',1000);
        //    compareMd5();
            
        // });
        var compareData = localStorage.getItem("compareData");
        var commonType = localStorage.getItem("commonType");
        compareMd5(0);
        loading();
        



function loading() {
//    alert("进来了");
    loadingTime = 0;
    $('body').loading({
        loadingWidth: 300,
        title: '请稍等!',
        name: 'test',
        discription: [
            '正在比对MD5 . . .',
            '比对完成 . . .'
            
        ],
        timeSpan: [0.5,0.5], //每条描述显示的时间
        direction: 'column',
        type: 'origin',
        //originBg:'#71EA71',
        originDivWidth: 40,
        originDivHeight: 40,
        originWidth: 6,
        originHeight: 6,
        smallLoading: false,
        loadingMaskBg: 'rgba(0,0,0,0.2)'
    });

    //a = setInterval('add()',1000);

    setTimeout(function () {
         displayResult();
    }, 1000);
}

function displayResult(){
    // alert(1);
    removeLoading('test');
    var json = eval("(" + compareData + ")");
    var compareResultStr = json.compareResult;
    var compareResult = eval("(" + compareResultStr + ")");
    var chainAssetStr = json.chainAsset;
    var chainAsset = eval("(" + chainAssetStr + ")");
    var diffResult = '';
    var isFirst = 1;
    for(key in compareResult){
        if(compareResult[key] == 'no'){
            // $("#" + key + "1").
            $("#" + key + "1").css('color','red');
            $("#" + key + "1").css('font-weight','bold');
            $("#" + key + "2").css('color','red');
            $("#" + key + "2").css('font-weight','bold');
        }
    }
    if(chainAsset.assetMd5 != json.nMd5){
        $("#md5Td1").css('color','red');
        $("#md5Td1").css('font-weight','bold');
        $("#md5Td2").css('color','red');
        $("#md5Td2").css('font-weight','bold');
         $("#validateResult").removeClass();
        $("#validateResult").addClass("alert1 alert-warning1 alert-dismissible1");
        $("#validateResult").html("<p style='color:#f39c12;text-align:center;'>MD5与原件信息不一致,鉴权资产被篡改！</p>");
    }
    else{
        $("#validateResult").removeClass();
        $("#validateResult").addClass("alert1 alert-success1");
        $("#validateResult").html("<p style='text-align:center;'>鉴权资产信息与原件信息一致！</p>");
    }
}

function compareMd5(){

    var json = eval("(" + compareData + ")");
//    alert(1);
//    alert(json);
    var nKeyInfoStr = json.nKeyInfo;
    var compareResultStr = json.compareResult;
    var chainAssetStr = json.chainAsset;

    var nKeyInfo = eval("(" + nKeyInfoStr + ")");
    var compareResult = eval("(" + compareResultStr + ")");
    var chainAsset = eval("(" + chainAssetStr + ")");

    var fileName2 = chainAsset.fileName;


    if(commonType != "图片"){
        var indexOf = fileName.indexOf(".");
        fileName = fileName.substr(0,indexOf) + ".png"; 
        var indexOf = fileName2.indexOf(".");
        fileName2 = fileName2.substr(0,indexOf) + ".png"; 
    }



//    alert(2);
//   alert(nKeyInfo);
//   alert(3);
//   alert(compareResult);
//   alert(4);
//   alert(chainAsset);
    var diffResult = '';
    var isFirst = 1;
    var content = '<div class="wrapper wrapper-content" style="background-color: #ecf0f5;padding: 20px;">\
        <div class="row">\
            <div class="col-md-6 col-xs-12">\
                <div class="widget widget-article widget-shadow">\
                    <div class="widget-body cover overlay">\
                        <img class="cover-image coverImages overlay-scale" src="' + basePath + filePath + '/' + fileName + '" alt="">\
                    </div>\
                    <div class="widget-body">\
                        <div class="ibox float-e-margins">\
                            <div>\
                                <h4 align="center" class="widget-title">资产鉴权信息</h4>\
                            </div>\
                            <div class="ibox-content">\
                                <table class="table table-striped">\
                                    <thead>\
                                        <tr>\
                                            <th style="font-weight:bold;">关键信息</th>\
                                        </tr>\
                                    </thead>\
                                    <tbody>';
                                  
                                    for(key in nKeyInfo){
                                         if(key == "error"){
                                            toastr.error("获取资产关键信息失败！");
                                            content = '';
                                            break;
                                        } 
                                        content += '<tr><td style="font-weight:bold;">' + key;
                                        // if(compareResult[key] == 'no'){
                                        //     content += '<td style="color:red;font-weight:bold;">';
                                        // }  
                                        // else{
                                        //    content += '<td>'; 
                                        // }
                                        content += '<td id="' + key + '1">'
                                        content += nKeyInfo[key] + '</td></tr>';
                                    }
                                    content += "<tr><td style='font-weight:bold;'>MD5</td>";
                                    // if(json.md5CompareResult == 'no'){
                                    //     content += '<td style="color:red;font-weight:bold;">';
                                    // }  
                                    // else{
                                    //    content += '<td>'; 
                                    // }
                                    content += '<td id="md5Td1">'
                                    content += json.nMd5;
                                    content += "</td></tr>";
                                    content += '</tbody>\
                                </table>\
                            </div>\
                        </div>\
                    </div>\
                </div>\
            </div>';




            content += '\
            <div class="col-md-6 col-xs-12">\
                <div class="widget widget-article widget-shadow">\
                    <div class="widget-body cover overlay">\
                        <img class="cover-image coverImages overlay-scale" src="' + basePath + chainAsset.filePath + '/' + fileName2 + '" alt="">\
                    </div>\
                    <div class="widget-body">\
                        <div class="ibox float-e-margins">\
                            <div>\
                                <h4 align="center" class="widget-title">资产原件信息</h4>\
                            </div>\
                            <div class="ibox-content">\
                                <table class="table table-striped">\
                                    <thead>\
                                        <tr>\
                                            <th style="font-weight:bold;">关键信息</th>\
                                        </tr>\
                                    </thead>\
                                   <tbody>';
                                 //  alert("chainmsg");
                                   var chainKeyInfoStr = chainAsset.keyInfo;
//                                   alert(chainKeyInfoStr);
                                var chainKeyInfo = eval("(" + chainKeyInfoStr + ")");
//                                alert(chainKeyInfo);
                                    for(key in chainKeyInfo){
                                         if(key == "error"){
                                            toastr.error("获取资产关键信息失败！");
                                            content = '';
                                            break;
                                        } 
                                        content += '<tr><td style="font-weight:bold;">' + key;
                                        // if(compareResult[key] == 'no'){
                                        //     content += '<td style="color:red;font-weight:bold;">';
                                        //     //  if(isFirst == 1){
                                        //     //     diffResult += key;
                                        //     //     isFirst = 0;
                                        //     // }
                                        //     // else{
                                        //     //     diffResult += "、" + key;
                                        //     // }
                                        // }  
                                        // else{
                                        //    content += '<td>'; 
                                        // }
                                        content += '<td id="' + key + '2">'
                                        content += chainKeyInfo[key] + '</td></tr>';
                                    }
                                    content += "<tr><td style='font-weight:bold;'>MD5</td>";
                                    // if(json.md5CompareResult == 'no'){
                                    //     content += '<td style="color:red;font-weight:bold;">';
                                    //      if(isFirst == 1){
                                    //         diffResult += "MD5";
                                    //         isFirst = 0;
                                    //     }
                                    //     else{
                                    //         diffResult += "、MD5";
                                    //     }
                                    // }  
                                    // else{
                                    //    content += '<td>'; 
                                    // }
                                    content += '<td id="md5Td2">'
                                    content += chainAsset.assetMd5;
                                    content += "</td></tr>";
                                    content += '</tbody>\
                                </table>\
                            </div>\
                        </div>\
                    </div>\
                </div>\
            </div>';
        
        content += '</div><div class="collapse1 in1">\
                        <div class="alert1" id="validateResult">\
                        </div>\
                    </div>';
        content += '</div>';


    $("#wrapDiv").html(content);
    
            
}


    });




    </script>