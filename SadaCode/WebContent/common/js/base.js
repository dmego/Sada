//时间戳转字符串
function datetimeFormat(value, format) {
    return value ? moment(parseInt(value) * 1000).format(format) : 'None';
}

Date.prototype.format = function (fmt) {
    var o = {
        "m+": this.getMonth() + 1, // 月份
        "d+": this.getDate(), // 日
        "h+": this.getHours(), // 小时
        "i+": this.getMinutes(), // 分
        "s+": this.getSeconds(), // 秒
        "q+": Math.floor((this.getMonth() + 3) / 3), // 季度
        "S": this.getMilliseconds()
        // 毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

String.prototype.startWith = function (s) {
    if (s == null || s == "" || this.length == 0 || s.length > this.length)
        return false;
    if (this.substr(0, s.length) == s)
        return true;
    else
        return false;
    return true;
}

String.prototype.replaceAll = function (s1, s2) {
    return this.replace(new RegExp(s1, "gm"), s2);
}

String.prototype.format = function () {
    if (arguments.length == 0) return this;
    for (var s = this, i = 0; i < arguments.length; i++)
        s = s.replace(new RegExp("\\{" + i + "\\}", "g"), arguments[i]);
    return s;
};

//加载页面到主页面
function loadPage(url, container) {
    var basePath = localStorage.getItem("basePath");
    if (!container)
        container = "#mainDiv";
    if (!url.startWith(basePath))
        url = basePath + url;
    jQuery(container).load(url, function (response, status, xhr) {
        if (status == "success") {
            if (response) {
                try {
                    var result = jQuery.parseJSON(response);
                    if (result.code == 100) {
                        jQuery(container).html("");
                        alert(result.data);
                    }
                } catch (e) {
                    return response;
                }
            }
        }
    });
}

/**
 * 字符串转日期
 * @returns {number}
 */
String.prototype.strToDate = function () {
    if (this && this != "") {
        return Date.parse(this.replace(/-/g, "/"));
    }
    else
        return "";
}

//配置Toastr的参数
//Toastr.options.positionClass = Config.controllername == 'index' ? "toast-top-right-index" : "toast-top-right"; 

//窗口大小改变,修正主窗体最小高度
$(window).resize(function () {
    $(".tab-addtabs").css("height", $(".content-wrapper").height() + "px");
});

//快捷搜索
$(".menuresult").width($("form.sidebar-form > .input-group").width());
var isAndroid = /(android)/i.test(navigator.userAgent);
var searchResult = $(".menuresult");
$("form.sidebar-form").on("blur", "input[name=q]", function () {
    searchResult.addClass("hide");
    if (isAndroid) {
        $.AdminLTE.options.sidebarSlimScroll = true;
    }
}).on("focus", "input[name=q]", function () {
    if (isAndroid) {
        $.AdminLTE.options.sidebarSlimScroll = false;
    }
    if ($("a", searchResult).size() > 0) {
        searchResult.removeClass("hide");
    }
}).on("keyup", "input[name=q]", function () {
    searchResult.html('');
    var val = $(this).val();
    var html = new Array();
    if (val != '') {
        $("ul.sidebar-menu li a[py]:not([href^='javascript:;'])").each(function () {
            if ($("span:first", this).text().indexOf(val) > -1 || $(this).attr("py").indexOf(val) > -1 || $(this).attr("pinyin").indexOf(val) > -1) {
                html.push('<a data-url="' + $(this).attr("data-url") + '" href="javascript:;">' + $("span:first", this).text() + '</a>');
                if (html.length >= 100) {
                    return false;
                }
            }
        });
    }
    $(searchResult).append(html.join(""));
    if (html.length > 0) {
        searchResult.removeClass("hide");
    } else {
        searchResult.addClass("hide");
    }
});

//快捷搜索点击事件
$("form.sidebar-form").on('mousedown click', '.menuresult a[data-url]', function () {
    loadPage($(this).data("url"));
    //$("ul.treeview-menu li").removeClass("active");
    //$(this).parent().addClass("active");
});

//切换左侧sidebar显示隐藏
$(document).on("click fa.event.toggleitem", ".sidebar-menu li > a", function (e) {
    $(".sidebar-menu li").removeClass("active");
    //当外部触发隐藏的a时,触发父辈a的事件
    if (!$(this).closest("ul").is(":visible")) {
        //如果不需要左侧的菜单栏联动可以注释下面一行即可
        $(this).closest("ul").prev().trigger("click");
    }
});


//清除缓存
$(document).on('click', "[data-toggle='wipecache']", function () {
    //先清除浏览器本地缓存
    localStorage.clear();
    //再清除服务器缓存
    $.ajax({
        url: basePath + '/menuAction_wipecache.do',
        dataType: 'json',
        cache: false,
        success: function (data) {
            if (data.success) {
                toastr.success("清除缓存成功");
            } else {
                toastr.error("清除缓存失败");
            }
        }, error: function () {
            toastr.error("网络错误");
        }
    });
});

//全屏事件
$(document).on('click', "[data-toggle='fullscreen']", function () {
    var doc = document.documentElement;
    if ($(document.body).hasClass("full-screen")) {
        $(document.body).removeClass("full-screen");
        document.exitFullscreen ? document.exitFullscreen() : document.mozCancelFullScreen ? document.mozCancelFullScreen() : document.webkitExitFullscreen && document.webkitExitFullscreen();
    } else {
        $(document.body).addClass("full-screen");
        doc.requestFullscreen ? doc.requestFullscreen() : doc.mozRequestFullScreen ? doc.mozRequestFullScreen() : doc.webkitRequestFullscreen ? doc.webkitRequestFullscreen() : doc.msRequestFullscreen && doc.msRequestFullscreen();
    }
});


//修复iOS下iframe无法滚动的BUG
if (/iPad|iPhone|iPod/.test(navigator.userAgent) && !window.MSStream) {
    $(".tab-addtabs").addClass("ios-iframe-fix");
}

if ($("ul.sidebar-menu li.active a").size() > 0) {
    $("ul.sidebar-menu li.active a").trigger("click");
} else {
    $("ul.sidebar-menu li a[url!='javascript:;']:first").trigger("click");
}



/**
 * Load a url into a page
 * 增加beforeSend以便拦截器在将该请求识别为非ajax请求
 */
var _old_load = jQuery.fn.load;
jQuery.fn.load = function (url, params, callback) {
    if (typeof url !== "string" && _old_load) {
        return _old_load.apply(this, arguments);
    }

    var selector, type, response,
        self = this,
        off = url.indexOf(" ");
    if (off > -1) {
        selector = jQuery.trim(url.slice(off));
        url = url.slice(0, off);
    }
    if (jQuery.isFunction(params)) {
        callback = params;
        params = undefined;
    } else if (params && typeof params === "object") {
        type = "POST";
    }
    if (self.length > 0) {
        jQuery.ajaxSetup({cache:true});
        jQuery.ajax({
            url: url,
            beforeSend: function (xhr) {
                xhr.setRequestHeader('X-Requested-With', {
                    toString: function () {
                        return '';
                    }
                });
            },
            type: type || "GET",
            dataType: "html",
            data: params
        }).done(function (responseText) {
            //console.log(responseText);
            response = arguments;
            //页面超时跳转到首页
            if (responseText.startWith("<!--login_page_identity-->")) {
                window.location.href = basePath + "/";
            } else {
                self.html(selector ?
                    jQuery("<div>").append(jQuery.parseHTML(responseText)).find(selector) :
                    responseText);
            }
        }).always(callback && function (jqXHR, status) {
                self.each(function () {
                    callback.apply(this, response || [jqXHR.responseText, status, jqXHR]);
                });
            });
    }

    return this;
};

//递归删除空属性防止把null变成空值
function deleteEmptyProp(obj) {
    for (var a in obj) {
        if (typeof (obj[a]) == "object" && obj[a] != null) {
            deleteEmptyProp(obj[a]);
        }
        else {
            if (!obj[a]) {
                delete obj[a];
            }
        }
    }
    return obj;
}

function ajaxPost(url, params, callback) {
    var result = null;
    var headers = {};
    // headers['CSRFToken'] = jQuery("#csrftoken").val();
    // console.log("jQuery('#csrftoken').val()="+ jQuery("#csrftoken").val());

    //console.log("删除空属性之前:");
    //console.log(params);
    if (params && typeof params == "object") {
        params = deleteEmptyProp(params);
    }
    //console.log("删除空属性之后:");
    //console.log(params);

    jQuery.ajax({ 
        type: 'post',
        async: false,
        //async: true,
        url: url,
        data: params,
        dataType: 'json',
        headers: headers,
        //contentType : 'application/json',
        success: function (data, status) {
            result = data;
            if (data && data.code && data.code == '101') {
                modals.error("操作失败，请刷新重试，具体错误：" + data.message);
                return false;
            }
            if (callback) {
                callback.call(this, data, status);
            }
        },
        error: function (err, err1, err2) {
            console.log("ajaxPost发生异常，请仔细检查请求url是否正确，如下面错误信息中出现success，则表示csrftoken更新，请忽略");
            //console.log(err.responseText);
            if (err && err.readyState && err.readyState == '4') {
                var sessionstatus = err.getResponseHeader("session-status");
                //console.log(err);
                //console.log(err1);
                //console.log(err2);
                if (sessionstatus == "timeout") {
                    //如果超时就处理 ，指定要跳转的页面
                    window.location.href = basePath + "/";
                }
                else if (err1 == "parsererror") {//csrf异常
                    var responseBody = err.responseText;
                    if (responseBody) {
                        responseBody = "{'retData':" + responseBody;
                        var resJson = eval('(' + responseBody + ')');
                        jQuery("#csrftoken").val(resJson.csrf.CSRFToken);
                        this.success(resJson.retData, 200);
                    }
                    return;
                } else {
                    modals.error({
                        text: JSON.stringify(err) + '<br/>err1:' + JSON.stringify(err1) + '<br/>err2:' + JSON.stringify(err2),
                        large: true
                    });
                    return;
                }
            }

            modals.error({
                text: JSON.stringify(err) + '<br/>err1:' + JSON.stringify(err1) + '<br/>err2:' + JSON.stringify(err2),
                large: true
            });
        }
    });

    return result;
}

function getServerTime(base_path, format) {
    var result = null;

    var sdate = new Date(ajaxPost(base_path + '/base/getServerTime'));
    if (sdate != 'Invalid Date') {
        result = formatDate(sdate, format || 'yyyy/mm/dd');
    }

    return result;
}

/**
 * 格式化日期
 */
function formatDate(date, format) {
    if (!date)return date;
    date = (typeof date == "number") ? new Date(date) : date;
    return date.format(format);
}

/**
 * 比较两个时间的大小 d1>d2 返回大于0
 * @param d1
 * @param d2
 * @returns {number}
 * @constructor
 */
function DateDiff(d1, d2) {
    var result = Date.parse(d1.replace(/-/g, "/")) - Date.parse(d2.replace(/-/g, "/"));
    return result;
}


/**
 * 将map类型[name,value]的数据转化为对象类型
 */
function getObjectFromMap(aData) {
    var map = {};
    for (var i = 0; i < aData.length; i++) {
        var item = aData[i];
        if (!map[item.name]) {
            map[item.name] = item.value;
        }
    }
    return map;
}


/**
 * 获取下一个编码  参数(000001，000001000006，6)
 * 得到结果 000001000007
 */
function getNextCode(prefix, maxCode, length) {
    if (maxCode == null) { 
        var str = "";
        for (var i = 0; i < length - 1; i++) {
            str += "0";
        }
        return prefix + str + 1;
    } else {
        var str = "";
        var sno = parseInt(maxCode.substring(prefix.length)) + 1;
        for (var i = 0; i < length - sno.toString().length; i++) {
            str += "0";
        }
        return prefix + str + sno;
    }

}

/**
 * 收缩左边栏时，触发markdown编辑的resize
 */
/*$("[data-toggle='offcanvas']").click(function () {
 if (editor) {
 setTimeout(function () {
 editor.resize()
 }, 500);
 }
 });*/


//获取布尔值
/*String.prototype.BoolValue=function(){
 if(this==undefined)
 return false;
 if(this=="false"||this=="0")
 return false;
 return true;
 }*/

var HtmlUtil = {
    /*1.用浏览器内部转换器实现html转码*/
    htmlEncode: function (html) {
        //1.首先动态创建一个容器标签元素，如DIV
        var temp = document.createElement("div");
        //2.然后将要转换的字符串设置为这个元素的innerText(ie支持)或者textContent(火狐，google支持)
        (temp.textContent != undefined ) ? (temp.textContent = html) : (temp.innerText = html);
        //3.最后返回这个元素的innerHTML，即得到经过HTML编码转换的字符串了
        var output = temp.innerHTML;
        temp = null;
        return output;
    },
    /*2.用浏览器内部转换器实现html解码*/
    htmlDecode: function (text) {
        //1.首先动态创建一个容器标签元素，如DIV
        var temp = document.createElement("div");
        //2.然后将要转换的字符串设置为这个元素的innerHTML(ie，火狐，google都支持)
        temp.innerHTML = text;
        //3.最后返回这个元素的innerText(ie支持)或者textContent(火狐，google支持)，即得到经过HTML解码的字符串了。
        var output = temp.innerText || temp.textContent;
        temp = null;
        return output;
    }
};


