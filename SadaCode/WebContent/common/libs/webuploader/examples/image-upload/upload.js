var fileName = '';
var filePath = '';
var fileType = '';
var commonType = '';
var keyInfo = '';

(function ($) {
    // 当domReady的时候开始初始化
    $(function () {
        var $wrap = $('#uploader'),

            // 图片容器
            $queue = $('<ul class="filelist"></ul>').appendTo($wrap.find('.queueList')),
            // 状态栏，包括进度和控制按钮
            $statusBar = $wrap.find('.statusBar'),
            // 文件总体选择信息。
            $info = $statusBar.find('.info'),
            // 上传按钮
            $upload = $wrap.find('.uploadBtn'),
            // 没选择文件之前的内容。
            $placeHolder = $wrap.find('.placeholder'),
            $progress = $statusBar.find('.progress').hide(),
            // 添加的文件数量
            fileCount = 0,
            // 添加的文件总大小
            fileSize = 0,
            // 优化retina, 在retina下这个值是2
            ratio = window.devicePixelRatio || 1,
            // 缩略图大小
            thumbnailWidth = 110 * ratio,
            thumbnailHeight = 110 * ratio,
            // 可能有pedding, ready, uploading, confirm, done.
            state = 'pedding',
            // 所有文件的进度信息，key为file id
            percentages = {},
            // 判断浏览器是否支持图片的base64
            isSupportBase64 = (function () {
                var data = new Image();
                var support = true;
                data.onload = data.onerror = function () {
                    if (this.width != 1 || this.height != 1) {
                        support = false;
                    }
                }
                data.src = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==";
                return support;
            })(),

            // 检测是否已经安装flash，检测flash的版本
            flashVersion = (function () {
                var version;
                try {
                    version = navigator.plugins['Shockwave Flash'];
                    version = version.description;
                } catch (ex) {
                    try {
                        version = new ActiveXObject('ShockwaveFlash.ShockwaveFlash')
                            .GetVariable('$version');
                    } catch (ex2) {
                        version = '0.0';
                    }
                }
                version = version.match(/\d+/g);
                return parseFloat(version[0] + '.' + version[1], 10);
            })(),

            supportTransition = (function () {
                var s = document.createElement('p').style,
                    r = 'transition' in s ||
                    'WebkitTransition' in s ||
                    'MozTransition' in s ||
                    'msTransition' in s ||
                    'OTransition' in s;
                s = null;
                return r;
            })(),
            // WebUploader实例
            uploader;

        if (!WebUploader.Uploader.support('flash') && WebUploader.browser.ie) {
            // flash 安装了但是版本过低。
            if (flashVersion) {
                (function (container) {
                    window['expressinstallcallback'] = function (state) {
                        switch (state) {
                            case 'Download.Cancelled':
                                alert('您取消了更新！')
                                break;

                            case 'Download.Failed':
                                alert('安装失败')
                                break;

                            default:
                                alert('安装已成功，请刷新！');
                                break;
                        }
                        delete window['expressinstallcallback'];
                    };

                    var swf = './expressInstall.swf';
                    // insert flash object
                    var html = '<object type="application/' +
                        'x-shockwave-flash" data="' + swf + '" ';

                    if (WebUploader.browser.ie) {
                        html += 'classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" ';
                    }

                    html += 'width="100%" height="100%" style="outline:0">' +
                        '<param name="movie" value="' + swf + '" />' +
                        '<param name="wmode" value="transparent" />' +
                        '<param name="allowscriptaccess" value="always" />' +
                        '</object>';

                    container.html(html);

                })($wrap);

                // 压根就没有安转。
            } else {
                $wrap.html('<a href="http://www.adobe.com/go/getflashplayer" target="_blank" border="0"><img alt="get flash player" src="http://www.adobe.com/macromedia/style_guide/images/160x41_Get_Flash_Player.jpg" /></a>');
            }
            return;
        } else if (!WebUploader.Uploader.support()) {
            alert('Web Uploader 不支持您的浏览器！');
            return;
        }


        // 实例化
        uploader = WebUploader.create({
            pick: {
                id: '#filePicker',
                label: '点击选择图片'
            },
            dnd: '#dndArea',
            paste: '#uploader',
            swf: '../../dist/Uploader.swf',
            chunked: false,
            chunkSize: 512 * 1024,
            server: 'fileUploadServlet',
            // 禁掉全局的拖拽功能。这样不会出现图片拖进页面的时候，把图片打开。
            disableGlobalDnd: true,
            fileNumLimit: 300,
            fileSizeLimit: 200 * 1024 * 1024, // 200 M
            fileSingleSizeLimit: 50 * 1024 * 1024 // 50 M
        });

        // 拖拽时不接受 js, txt 文件。
        uploader.on('dndAccept', function (items) {
            var denied = false,
                len = items.length,
                i = 0,
                // 修改js类型
                unAllowed = 'text/plain;application/javascript ';

            for (; i < len; i++) {
                // 如果在列表里面
                if (~unAllowed.indexOf(items[i].type)) {
                    denied = true;
                    break;
                }
            }

            return !denied;
        });
        uploader.on('ready', function () {
            window.uploader = uploader;
        });

        // 当有文件添加进来时执行，负责view的创建
        function addFile(file) {
            var $li = $('<li id="' + file.id + '">' +
                    '<p class="title">' + file.name + '</p>' +
                    '<p class="imgWrap"></p>' +
                    '<p class="progress"><span></span></p>' +
                    '</li>'),

                $btns = $('<div class="file-panel">' +
                    '<span class="cancel">删除</span>' +
                    '<span class="rotateRight">向右旋转</span>' +
                    '<span class="rotateLeft">向左旋转</span></div>').appendTo($li),
                $prgress = $li.find('p.progress span'),
                $wrap = $li.find('p.imgWrap'),
                $info = $('<p class="error"></p>'),

                showError = function (code) {
                    switch (code) {
                        case 'exceed_size':
                            text = '文件大小超出';
                            break;

                        case 'interrupt':
                            text = '上传暂停';
                            break;

                        default:
                            text = '上传失败，请重试';
                            break;
                    }

                    $info.text(text).appendTo($li);
                };

            if (file.getStatus() === 'invalid') {
                showError(file.statusText);
            } else {
                // @todo lazyload
                $wrap.text('预览中');
                uploader.makeThumb(file, function (error, src) {
                    var img;

                    if (error) {
                        $wrap.text('不能预览');
                        return;
                    }

                    if (isSupportBase64) {
                        img = $('<img src="' + src + '">');
                        $wrap.empty().append(img);
                    } else {
                        //                        $.ajax('../../server/preview.php', {
                        //                            method: 'POST',
                        //                            data: src,
                        //                            dataType:'json'
                        //                        }).done(function( response ) {
                        //                            if (response.result) {
                        //                                img = $('<img src="'+response.result+'">');
                        //                                $wrap.empty().append( img );
                        //                            } else {
                        //                                $wrap.text("预览出错");
                        //                            }
                        //                        });
                    }
                }, thumbnailWidth, thumbnailHeight);

                percentages[file.id] = [file.size, 0];
                file.rotation = 0;
            }

            file.on('statuschange', function (cur, prev) {
                if (prev === 'progress') {
                    $prgress.hide().width(0);
                } else if (prev === 'queued') {
                    $li.off('mouseenter mouseleave');
                    $btns.remove();
                }

                // 成功
                if (cur === 'error' || cur === 'invalid') {
                    console.log(file.statusText);
                    showError(file.statusText);
                    percentages[file.id][1] = 1;
                } else if (cur === 'interrupt') {
                    showError('interrupt');
                } else if (cur === 'queued') {
                    percentages[file.id][1] = 0;
                } else if (cur === 'progress') {
                    $info.remove();
                    $prgress.css('display', 'block');
                } else if (cur === 'complete') {
                    $li.append('<span class="success"></span>');
                }

                $li.removeClass('state-' + prev).addClass('state-' + cur);
            });

            $li.on('mouseenter', function () {
                $btns.stop().animate({
                    height: 30
                });
            });

            $li.on('mouseleave', function () {
                $btns.stop().animate({
                    height: 0
                });
            });

            $btns.on('click', 'span', function () {
                var index = $(this).index(),
                    deg;

                switch (index) {
                    case 0:
                        uploader.removeFile(file);
                        return;

                    case 1:
                        file.rotation += 90;
                        break;

                    case 2:
                        file.rotation -= 90;
                        break;
                }

                if (supportTransition) {
                    deg = 'rotate(' + file.rotation + 'deg)';
                    $wrap.css({
                        '-webkit-transform': deg,
                        '-mos-transform': deg,
                        '-o-transform': deg,
                        'transform': deg
                    });
                } else {
                    $wrap.css('filter', 'progid:DXImageTransform.Microsoft.BasicImage(rotation=' + (~~((file.rotation / 90) % 4 + 4) % 4) + ')');
                    // use jquery animate to rotation
                    // $({
                    //     rotation: rotation
                    // }).animate({
                    //     rotation: file.rotation
                    // }, {
                    //     easing: 'linear',
                    //     step: function( now ) {
                    //         now = now * Math.PI / 180;

                    //         var cos = Math.cos( now ),
                    //             sin = Math.sin( now );

                    //         $wrap.css( 'filter', "progid:DXImageTransform.Microsoft.Matrix(M11=" + cos + ",M12=" + (-sin) + ",M21=" + sin + ",M22=" + cos + ",SizingMethod='auto expand')");
                    //     }
                    // });
                }


            });

            $li.appendTo($queue);
        }

        // 负责view的销毁
        function removeFile(file) {
            var $li = $('#' + file.id);

            delete percentages[file.id];
            updateTotalProgress();
            $li.off().find('.file-panel').off().end().remove();
        }

        function updateTotalProgress() {
            var loaded = 0,
                total = 0,
                spans = $progress.children(),
                percent;

            $.each(percentages, function (k, v) {
                total += v[0];
                loaded += v[0] * v[1];
            });

            percent = total ? loaded / total : 0;


            spans.eq(0).text(Math.round(percent * 100) + '%');
            spans.eq(1).css('width', Math.round(percent * 100) + '%');
            updateStatus();
        }

        function updateStatus() {
            var text = '',
                stats;

            if (state === 'ready') {
                text = '选中' + fileCount + '张图片，共' +
                    WebUploader.formatSize(fileSize) + '。';
            } else if (state === 'confirm') {
                stats = uploader.getStats();
                if (stats.uploadFailNum) {
                    text = '已成功上传' + stats.successNum + '张照片至XX相册，' +
                        stats.uploadFailNum + '张照片上传失败，<a class="retry" href="#">重新上传</a>失败图片或<a class="ignore" href="#">忽略</a>'
                }

            } else {
                stats = uploader.getStats();
                text = '共' + fileCount + '张（' +
                    WebUploader.formatSize(fileSize) +
                    '），已上传' + stats.successNum + '张';

                if (stats.uploadFailNum) {
                    text += '，失败' + stats.uploadFailNum + '张';
                }
            }

            $info.html(text);
        }

        function setState(val) {
            var file, stats;

            if (val === state) {
                return;
            }

            $upload.removeClass('state-' + state);
            $upload.addClass('state-' + val);
            state = val;

            switch (state) {
                case 'pedding':
                    $placeHolder.removeClass('element-invisible');
                    $queue.hide();
                    $statusBar.addClass('element-invisible');
                    uploader.refresh();
                    break;

                case 'ready':
                    $placeHolder.addClass('element-invisible');
                    $('#filePicker2').removeClass('element-invisible');
                    $queue.show();
                    $statusBar.removeClass('element-invisible');
                    uploader.refresh();
                    break;

                case 'uploading':
                    $('#filePicker2').addClass('element-invisible');
                    $progress.show();
                    $upload.text('暂停上传');
                    break;

                case 'paused':
                    $progress.show();
                    $upload.text('继续上传');
                    break;

                case 'confirm':
                    $progress.hide();
                    $('#filePicker2').removeClass('element-invisible');
                    $upload.text('开始上传');

                    stats = uploader.getStats();
                    if (stats.successNum && !stats.uploadFailNum) {
                        setState('finish');
                        return;
                    }
                    break;
                case 'finish':
                    stats = uploader.getStats();
                    if (stats.successNum) {
                        //  alert( '上传成功' );
                    } else {
                        // 没有成功的图片，重设
                        state = 'done';
                        location.reload();
                    }
                    break;
            }

            updateStatus();
        }

        uploader.onUploadProgress = function (file, percentage) {
            var $li = $('#' + file.id),
                $percent = $li.find('.progress span');

            $percent.css('width', percentage * 100 + '%');
            percentages[file.id][1] = percentage;
            updateTotalProgress();
        };

        uploader.onFileQueued = function (file) {
            fileCount++;
            fileSize += file.size;

            if (fileCount === 1) {
                $placeHolder.addClass('element-invisible');
                $statusBar.show();
            }

            addFile(file);
            setState('ready');
            updateTotalProgress();
        };

        uploader.onFileDequeued = function (file) {
            fileCount--;
            fileSize -= file.size;

            if (!fileCount) {
                setState('pedding');
            }

            removeFile(file);
            updateTotalProgress();

        };

        uploader.on('all', function (type) {
            var stats;
            switch (type) {
                case 'uploadFinished':
                    setState('confirm');
                    break;

                case 'startUpload':
                    setState('uploading');
                    break;

                case 'stopUpload':
                    setState('paused');
                    break;

            }
        });

        uploader.onError = function (code) {
            alert('Eroor: ' + code);
        };

        $upload.on('click', function () {
            if ($(this).hasClass('disabled')) {
                return false;
            }

            if (state === 'ready') {
                uploader.upload();
            } else if (state === 'paused') {
                uploader.upload();
            } else if (state === 'uploading') {
                uploader.stop();
            }
        });

        $info.on('click', '.retry', function () {
            uploader.retry();
        });

        $info.on('click', '.ignore', function () {
            alert('todo');
        });

        $upload.addClass('state-' + state);
        updateTotalProgress();
        uploader.on('uploadSuccess', function (file, data) {
            fileName = data.fileName;
            filePath = data.filePath;
            fileType = data.fileType;
            commonType = data.commonType;

//            getKeyInfo(data.fileName, data.filePath);
            getKeyInfo();

        });
    });

})(jQuery);

$("#assetType").change(function(){
    getKeyInfo();
});

$("#reIdentify").click(function(event) {
    getKeyInfo();
});

var loadingTime = 0;
var a;
var a2;
var resp;


function getKeyInfo() {
    //  			$("#getKeyInfoStatus").show();
    if(fileName == null || fileName.length == 0 || filePath == null || filePath.length == 0){
        return ;
    }
    

    assetType = $("#assetType").val();

    if (assetType == null || assetType.length == 0) {
        toastr.error('请选择资产类型');
        // alert("请选择资产类型");
        $("#assetType").focus();
        return;
    }
    loadingKeyInfo();
    $.post('assetAction_getFileKeyInfo.do', {
            'fileName': fileName,
            'filePath': filePath,
            'code': assetType
        },
        function (data, textStatus, xhr) {
            // $("#getKeyInfoStatus").hide();
            // $("#keyInfoDivWrap").show();
        	resp = data;
            a2 = setInterval("removeKI(2,'keyInfoDiv')",1000);
            
        });
}


function getAssetType() {
    $.post('dictAction_getAssetTypeList.do', {},
        function (data, textStatus, xhr) {
            if (data != null) {
                if (!data.success) {
                    toastr.error(data.message);
                    // alert();
                } else {
                    content = '';
                    $.each(data.data, function (index, item) {
                        content += '<option value="' + item.code + '">' + item.name + '</option>';
                    });
                    $("#assetType").html(content);
                }
            } else {
                toastr.error("获取资产类型信息失败！");
            }
        });
}

function save() {
    var name = $("#name").val();
    if (name == null || name.length == 0) {
        toastr.error("请填写资产名称");
        $("#name").focus();
        return;
    }
    loadingSave();
    var keyInfoStr = '{';
    for (var key in keyInfo) {
        keyInfoStr += '"' + key + '":"' + keyInfo[key] + '",';

    }
    keyInfoStr += '}';
    //var keyInfoStr = JSON.stringify(keyInfo);
    $.post('assetAction_save.do', {
            'name': name,
            'fileName': fileName,
            'filePath': filePath,
            'fileType': fileType,
            'commonType': commonType,
            // 'assetType': $("#assetType").val(),
            'assetType':$("#assetType").find("option:selected").text(),
            'code':$("#assetType").val(),
            'keyInfo': keyInfoStr,
            'tag': $("#tag").val()
        },
        function (data, textStatus, xhr) {
           // removeSave(7);
            resp = data;
            a2 = setInterval("removeSave(7)",1000);
            
        });
}

function loadingKeyInfo() {
    loadingTime = 0;
    //alert("进来了");
    $('body').loading({
        loadingWidth: 300,
        title: '请稍等!',
        name: 'test',
        discription: [
            '正在识别关键信息 . . .',
            '识别关键信息完成 . . .'
            // '正在初始化一个交易 . . .',
            // 'Endorsing peers 验证签名并执行交易 . . .',
            // 'Proposal 回应被检查 . . .',
            // 'Endorsement 被装配到交易中 . . .',
            // '交易被验证并提交 . . .',
            // '账本更新 . . . ',
            // '更新成功 . . .'
        ],
        timeSpan: [1,1], //每条描述显示的时间
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
    a = setInterval("add()",1000);
    // setTimeout(function () {
    //     removeLoading('test');
    // }, 31000);
}




function removeKI(t,id){
    // alert("removeL" + loadingTime);
   if(loadingTime >= t){
     //   alert("移除");
        removeLoading('test');
       

            if (resp != null) {
                if (!resp.success) {
                    
                    toastr.error(resp.message);
                    // alert();
                } else {
                    var content = '';
                    keyInfo = resp.data;
                
                    


//----------------------------
content += '<div class="widget-body">\
                        <div class="ibox float-e-margins">\
                            <div class="ibox-content">\
                                <table class="table table-striped">\
                                    <thead>\
                                        <tr>\
                                            <th>关键信息</th>\
                                        </tr>\
                                    </thead>\
                                    <tbody>';
                    for (var key in resp.data) {
                        if(key == "error"){
                            toastr.error("获取资产关键信息失败！");
                            // alert();
                            content = '';
                            break;
                        }

                            content += '<tr>\
                                            <td>' + key + '</td>\
                                            <td>' + resp.data[key] + '</td>\
                                        </tr>';


//----------------------------
                        

                    }
                    content +=  '</tbody>\
                                </table></div></div></div>';
                   
                    $("#keyInfoDiv").html(content);
                }
            } else {
                toastr.error("获取资产关键信息失败！");
                // alert();
            }
            $("#submitBtn").prop('disabled', false);
            $("#reIdentifyBtn").prop('disabled', false);
            

       
        clearInterval(a);  
        clearInterval(a2);     
   }
    
   
   // alert("移除了");
}

function removeSave(t){
    // alert("removeL" + loadingTime);
   if(loadingTime >= t){
        // alert("移除");
        removeLoading('test');
        
        if (resp != null) {
            if (!resp.success) {
                toastr.error(resp.message);
            } else {
                toastr.success(resp.message);
                // document.location.reload();
            }
        } else {
             toastr.error("保存资产信息失败！");
        }
        modals.hideWin('addAssetWin');
        clearInterval(a);  
        clearInterval(a2);   
    }
      
 
    
   
   // alert("移除了");
}

function loadingSave() {
    //alert("进来了");
    loadingTime = 0;
    $('body').loading({
        loadingWidth: 300,
        title: '请稍等!',
        name: 'test',
        discription: [
            // '正在识别关键信息 . . .',
            // '识别关键信息成功 . . .',
            '正在初始化一个交易 . . .',
            'Endorsing peers 验证签名并执行交易 . . .',
            'Proposal 回应被检查 . . .',
            'Endorsement 被装配到交易中 . . .',
            '交易被验证并提交 . . .',
            '账本更新 . . . ',
            '更新成功 . . .'
        ],
        timeSpan: [1,1,1,1,1,1,1], //每条描述显示的时间
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

    a = setInterval("add()",1000);

    // setTimeout(function () {
    //     removeLoading('test');
    // }, 31000);
}


function add(){
    loadingTime++;
    // alert("add" + loadingTime);
}



$(document).ready(function () {
    getAssetType();

});