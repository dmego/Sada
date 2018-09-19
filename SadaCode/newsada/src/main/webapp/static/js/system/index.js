//登录后加载左侧菜单
$().ready(function () {
    loadMenuTree();
    //buildmenu(privilegeDate);
});


function loadMenuTree() {
    // //ajax根据权限动态加载左侧菜单
    // $.post("menuAction_showMenu.do",{},function(privilegeDate){
    // 	buildmenu(privilegeDate);
    // });
    //加载菜单
    var menuData;
    ajaxPost(basePath + "/menuAction_showMenu.do", null, function (data) {
        var $li, $menu_f_ul, $menu_s, $li_s, $menu_s_ul;
        menuData = data;
        $.each(data, function (index, item) {
            if (item.levelCode.length == 6) { 
                if (hasChildMenu(item.id)) {
                    $li = $('<li class="treeview"></li>');
                    var $menu_f = $('<a href="#" py="' + item.py + '" pinyin="' + item.pinyin +'">\n' +
                        '<i class="' + item.icon + '"></i> <span>' + item.name + '</span>\n' +
                        ' <span class="pull-right-container">\n' +
                        '<i class="fa fa-angle-left pull-right"></i>\n' +
                        '</span></a>');
                    $li.append($menu_f);
                    $menu_f_ul = $('<ul class="treeview-menu"></ul>');
                    $li.append($menu_f_ul);
                    $("ul.sidebar-menu").append($li);
                }else{
                    $li = $('<li><a href="#" py="' + item.py + '" pinyin="' + item.pinyin +'" data-url="'+basePath
                        + item.url + '"><i class="' + item.icon + '"></i> <span>' + item.name + '</span>\n</a></li>');
                    $("ul.sidebar-menu").append($li)
                }
                
            } else if (item.levelCode.length == 12) {
                if (hasChildMenu(item.id)) {
                    $li_s = $('<li class="treeview"></li>');
                    $menu_s = $('<a href="#" py="' + item.py + '" pinyin="' + item.pinyin +'">\n' +
                        '<i class="' + item.icon + '"></i> <span>' + item.name + '</span>\n' +
                        ' <span class="pull-right-container">\n' +
                        '<i class="fa fa-angle-left pull-right"></i>\n' +
                        '</span></a>');
                    $li_s.append($menu_s);
                    $menu_s_ul = $('<ul class="treeview-menu"></ul>');
                    $li_s.append($menu_s_ul);
                    $menu_f_ul.append($li_s);
                } else {
                    $menu_s = $('<li><a href="#" py="' + item.py + '" pinyin="' + item.pinyin +'" data-url="'
                    +basePath + item.url + '"><i class="' + item.icon + '"></i> <span>' + item.name + '</span>\n</a></li>');
                    $menu_f_ul.append($menu_s);
                }
            } else if (item.levelCode.length == 18) {
                $menu_t = $('<li><a href="#" py="' + item.py + '" pinyin="' + item.pinyin +'" data-url="'
                +basePath + item.url + '"><i class="' + item.icon + '"></i> <span>' + item.name + '</span>\n</a></li>');
                $menu_s_ul.append($menu_t);
            }
        });
    });

    //判断当前二级菜单是否有包含三级菜单
    function hasChildMenu(parentId) {
        if (!menuData)
            return false;
        for (var i = 0; i < menuData.length; i++) {
            var item = menuData[i];
            if (item.parentId == parentId) {
                return true;
            }
        }
        return false;
    }

}




function buildmenu(privilegeDate) {
    var $sidemenu = $("#sideber-menu");
    var content = "";
    for (var i = 0; i < privilegeDate.length; i++) {
        if (privilegeDate[i].isParent == true) { //节点是父节点
            content += "<li class='treeview'><a href='#'  py='" + privilegeDate[i].py + "' pinyin='" + privilegeDate[i].pinyin +
                "' ><i class='" + privilegeDate[i].icon + "'></i> <span>" + privilegeDate[i].name +
                "</span><span class='pull-right-container'><i class='fa fa-angle-left'></i> </span></a>";

            content += "<ul class='treeview-menu'>";
            for (var j = 0; j < privilegeDate[i].nodes.length; j++) {
                content += "<li class=''><a href='#'data-url='" + privilegeDate[i].nodes[j].url + "' py='" + privilegeDate[i].nodes[j].py +
                    "' pinyin='" + privilegeDate[i].nodes[j].pingyin + "'><i class='" + privilegeDate[i].nodes[j].icon +
                    "'></i> <span>" + privilegeDate[i].nodes[j].name + "</span> <span class='pull-right-container'></span></a></li>";
            }
            content += "</ul></li>"
        } else { //节点不是父节点
            content += "<li class=''><a href='#' data-url='" + privilegeDate[i].url +
                "' py='" + privilegeDate[i].py + "' pinyin='" + privilegeDate[i].pinyin + "'><i class='" + privilegeDate[i].icon +
                "'></i> <span>" + privilegeDate[i].name + "</span> <span class='pull-right-container'></span></a></li>";

        }
    }
    $sidemenu.append(content);
}