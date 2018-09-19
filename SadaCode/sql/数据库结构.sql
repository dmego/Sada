#数据字典表
create table Dict(
	id varchar(36) NOT NULL primary key,
  name varchar(200) NULL,
  levelCode varchar(36) NULL, 
  code varchar(50) NULL, 
	parentId varchar(36) NULL,#父节点ID
	deleted varchar(10) NULL,
  remark varchar(200) NULL,
  value varchar(200) NULL,
  createtime varchar(50) NULL,  #创建时间(时间戳形式)
  updatetime varchar(50) NULL  #更新时间(时间戳形式)
)  charset utf8 collate utf8_general_ci;

#用户表
create table User(
    userId varchar(50) NOT NULL primary key, #主键
    userName varchar(50)   NULL, #用户名
    nickName varchar(50)   NULL, #昵称（登录名）
    password varchar(50)   NULL, #登录密码
    sexId varchar(50)   NULL, #性别ID,根据数据字典表获取
    birthday varchar(50) NULL, #出生日期 (时间戳形式)
    email varchar(50)  NULL, #邮箱
    phoneNum varchar(50) NULL, #手机号
    userPic varchar(50)  NULL, #用户头像（url）
    isAdmin varchar(10) NULL, #是否是管理员
    remark varchar(500)  NULL,  #备注
    isDelete varchar(10)  NULL,  #是否删除
    createtime varchar(50) NULL,  #创建时间(时间戳形式)
    updatetime varchar(50) NULL  #更新时间(时间戳形式)
) charset utf8 collate utf8_general_ci;

  #权限信息表 
  create table Popedom(
	id varchar(36) NOT NULL primary key, #权限id(主键ID)
  name varchar(500) NULL, #权限名称
  levelCode varchar(36) NULL, 
  code varchar(50) NULL, 
	pid varchar(36) NOT NULL, #父级权限code，如果是根节点，则为0
	url varchar(5000) NULL, #权限在系统中执行访问地址的URL
	icon varchar(5000) NULL, #显示图标
	functype varchar(255) NULL, #0=目录 1=功能 2=按钮
  queryId varchar(255) NULL, 
  py varchar(20) NULL,#快速菜搜索的拼音简写
  pingyin varchar(50) NULL,#快速菜搜索的拼音
  remark varchar(500)  NULL,  #备注
  createtime varchar(50) NULL,  #创建时间(时间戳形式)
  updatetime varchar(50) NULL,#更新时间(时间戳形式)
  isParent varchar(10) NULL, #是否是父节点，是为true，否为false
  )  charset utf8 collate utf8_general_ci;

  
   
insert into Popedom(id,pid,name,url,icon,isMenu,isParent,addtabs,py,pingyin) 
values ('aa','0','个人中心','userAction_userDetail.do','fa fa-user-md',true,false,'11283','grzx','gerenzhongxin');
insert into Popedom(id,pid,name,url,icon,isMenu,isParent,addtabs,py,pingyin) 
values ('ab','0','用户管理','userAction_userList.do','fa fa-user',true,false,'11186','yhgl','yonhuguanli');
insert into Popedom(id,pid,name,url,icon,isMenu,isParent,addtabs,py,pingyin) 
values ('ac','0','资产管理','','fa fa-id-card',true,true,'11255','zcgl','zichanguanli');
insert into Popedom(id,pid,name,url,icon,isMenu,isParent,addtabs,py,pingyin) 
values ('ad','ac','资产管理','assetAction_manage.do','fa fa-id-card',true,false,'11265','zcgl','zichanguanli');
insert into Popedom(id,pid,name,url,icon,isMenu,isParent,addtabs,py,pingyin) 
values ('ae','ac','资产鉴权','userAction_userDetail.do','fa fa-search',true,false,'11257','zcjq','zichanjianquan');
insert into Popedom(id,pid,name,url,icon,isMenu,isParent,addtabs,py,pingyin) 
values ('af','0','权限管理','','fa fa-group',true,true,'11198','qxgl','quanxianguanli');
insert into Popedom(id,pid,name,url,icon,isMenu,isParent,addtabs,py,pingyin) 
values ('ag','af','管理员管理','userAction_adminList.do','fa fa-user',true,false,'11199','glygl','guanliyuanguanli');
insert into Popedom(id,pid,name,url,icon,isMenu,isParent,addtabs,py,pingyin) 
values ('ah','af','角色组','roleAction_roleList.do','fa fa-group',true,false,'11208','jsz','jiaosezu');
insert into Popedom(id,pid,name,url,icon,isMenu,isParent,addtabs,py,pingyin) 
values ('ai','af','权限管理','authAction_authList.do','fa fa-bars',true,false,'11213','qxgl','gerenzhongxin');


  #角色信息表
  create table Role(
	  roleId varchar(32) NOT NULL primary key, #角色ID
	  roleName varchar(500), #角色名称
    remark varchar(500) NULL,  #备注
    createtime varchar(50) NULL,  #创建时间(时间戳形式)
    updatetime varchar(50) NULL  #更新时间(时间戳形式)
  ) charset utf8 collate utf8_general_ci;

#由于权限表使用的联合主键，采用手工创建角色权限表结构，不使用hibernate来实现多对多关系
#角色权限表
create table RolePopedom(
  roleId varchar(32) NOT NUll,
	id varchar(32)  NOT NUll,
	pid varchar(32) NOT NUll,
  remark varchar(500) NULL,  #备注
  createtime varchar(50) NULL,  #创建时间(时间戳形式)
  updatetime varchar(50) NULL  #更新时间(时间戳形式)
	primary key(roleId,id,pid)
)charset utf8 collate utf8_general_ci;