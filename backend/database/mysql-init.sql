drop table if exists questionnaire_answer cascade;
drop table if exists questionnaire cascade;
drop table if exists sys_user_role cascade;
drop table if exists sys_role_menu cascade;
drop table if exists sys_role_dept cascade;
drop table if exists sys_dept cascade;
drop table if exists sys_user cascade;
drop table if exists sys_role cascade;
drop table if exists sys_menu cascade;
drop table if exists sys_oper_log cascade;
drop table if exists sys_dict_type cascade;
drop table if exists sys_dict_data cascade;
drop table if exists sys_config cascade;
drop table if exists sys_logininfor cascade;
drop table if exists sys_notice cascade;

-- ----------------------------
-- 部门表
-- ----------------------------
create table sys_dept
(
    dept_id     bigint(20) not null auto_increment comment '部门id',
    parent_id   bigint(20)  default 0 comment '父部门id',
    ancestors   varchar(50) default '' comment '祖级列表',
    dept_name   varchar(30) default '' comment '部门名称',
    order_num   int(4)      default 0 comment '显示顺序',
    leader      varchar(20) default null comment '负责人',
    phone       varchar(11) default null comment '联系电话',
    email       varchar(50) default null comment '邮箱',
    status      char(1)     default '0' comment '部门状态（0正常 1停用）',
    del_flag    char(1)     default '0' comment '删除标志（0代表存在 2代表删除）',
    create_by   varchar(64) default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64) default '' comment '更新者',
    update_time datetime comment '更新时间',
    primary key (dept_id)
) engine = innodb
  auto_increment = 200 comment = '部门表';

-- ----------------------------
-- 初始化-部门表数据
-- ----------------------------
insert into sys_dept
values (100, 0, '0', '苏小问', 0, 'SXW', null, 'SXW@suxiaowen.com', '0', '0', 'root', sysdate(), '', null);
insert into sys_dept
values (101, 100, '0,100', '苏州大学', 1, 'SUDA', null, 'SUDA@suxiaowen.com', '0', '0', 'root', sysdate(), '', null);
insert into sys_dept
values (102, 101, '0,100,101', '系统超级管理员', 2, 'SXW', null, 'root@suxiaowen.com', '0', '0', 'root', sysdate(), '', null);
insert into sys_dept
values (103, 101, '0,100,101', '系统管理员', 3, 'root', null, 'admin@suxiaowen.com', '0', '0', 'root', sysdate(), '', null);
insert into sys_dept
values (104, 101, '0,100,101', '系统用户', 4, 'admin', null, 'user@suxiaowen.com', '0', '0', 'admin', sysdate(), '', null);


-- ----------------------------
-- 用户信息表
-- ----------------------------

create table sys_user
(
    user_id     bigint(20)  not null auto_increment comment '用户ID',
    dept_id     bigint(20)   default null comment '部门ID',
    user_name   varchar(30) not null comment '用户账号',
    nick_name   varchar(30) not null comment '用户昵称',
    user_type   varchar(2)   default '00' comment '用户类型（00系统用户）',
    email       varchar(50)  default '' comment '用户邮箱',
    phonenumber varchar(11)  default '' comment '手机号码',
    sex         char(1)      default '0' comment '用户性别（0男 1女 2未知）',
    avatar      varchar(100) default '' comment '头像地址',
    password    varchar(100) default '' comment '密码',
    status      char(1)      default '0' comment '帐号状态（0正常 1停用）',
    del_flag    char(1)      default '0' comment '删除标志（0代表存在 2代表删除）',
    credit      bigint(20)   default 0 comment '账户积分',
    student_id  varchar(16)  default '' comment '学号',
    login_ip    varchar(128) default '' comment '最后登录IP',
    login_date  datetime comment '最后登录时间',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark      varchar(500) default null comment '备注',
    primary key (user_id)
) engine = innodb
  auto_increment = 100 comment = '用户信息表';

-- ----------------------------
-- 初始化-用户信息表数据
-- ----------------------------
insert into sys_user
values (1, 102, 'root', '苏小问-超级管理员', '00', 'root@suxiaowen.com', '15888888888', '2', '',
        '$2a$10$pR/.zfzF2sIRJ2jzzH78KOgPdhNllc1EetqRzJ483Z6jfAL1iLIri', '0', '0', 0, '', '127.0.0.1', sysdate(), '',
        sysdate(), '', null, '超级管理员');
insert into sys_user
values (2, 103, 'admin', '苏小问-系统管理员', '00', 'root@suxiaowen.com', '15888888888', '2', '',
        '$2a$10$pR/.zfzF2sIRJ2jzzH78KOgPdhNllc1EetqRzJ483Z6jfAL1iLIri', '0', '0', 0, '', '127.0.0.1', sysdate(),
        'root', sysdate(), '', null, '系统(高校)管理员');
insert into sys_user
values (3, 104, 'test', '苏小问-普通用户', '00', 'user@suxiaowen.com', null, '0', '',
        '$2a$10$pR/.zfzF2sIRJ2jzzH78KOgPdhNllc1EetqRzJ483Z6jfAL1iLIri', '0', '0', 0, '', '127.0.0.1', sysdate(),
        'admin', sysdate(), '', null, '普通用户');

-- ----------------------------
-- 角色信息表
-- ----------------------------
create table sys_role
(
    role_id             bigint(20)   not null auto_increment comment '角色ID',
    role_name           varchar(30)  not null comment '角色名称',
    role_key            varchar(100) not null comment '角色权限字符串',
    role_sort           int(4)       not null comment '显示顺序',
    data_scope          char(1)      default '1' comment '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
    menu_check_strictly tinyint(1)   default 1 comment '菜单树选择项是否关联显示',
    dept_check_strictly tinyint(1)   default 1 comment '部门树选择项是否关联显示',
    status              char(1)      not null comment '角色状态（0正常 1停用）',
    del_flag            char(1)      default '0' comment '删除标志（0代表存在 2代表删除）',
    create_by           varchar(64)  default '' comment '创建者',
    create_time         datetime comment '创建时间',
    update_by           varchar(64)  default '' comment '更新者',
    update_time         datetime comment '更新时间',
    remark              varchar(500) default null comment '备注',
    primary key (role_id)
) engine = innodb
  auto_increment = 100 comment = '角色信息表';

-- ----------------------------
-- 初始化-角色信息表数据
-- ----------------------------
insert into sys_role
values ('1', '超级管理员', 'root', 1, 1, 1, 1, '0', '0', '', sysdate(), '', null, '超级管理员');
insert into sys_role
values ('2', '系统管理员', 'admin', 2, 2, 1, 1, '0', '0', 'root', sysdate(), '', null, '系统(高校)管理员');
insert into sys_role
values ('3', '问卷设计者', 'designer', 3, 2, 1, 0, '0', '0', 'admin', sysdate(), '', null, '问卷设计者');
insert into sys_role
values ('4', '问卷填写者', 'user', 4, 2, 1, 0, '0', '0', 'admin', sysdate(), '', null, '问卷填写者');


-- ----------------------------
-- 菜单权限表
-- ----------------------------
create table sys_menu
(
    menu_id     bigint(20)  not null auto_increment comment '菜单ID',
    menu_name   varchar(50) not null comment '菜单名称',
    parent_id   bigint(20)   default 0 comment '父菜单ID',
    order_num   int(4)       default 0 comment '显示顺序',
    path        varchar(200) default '' comment '路由地址',
    component   varchar(255) default null comment '组件路径',
    query       varchar(255) default null comment '路由参数',
    is_frame    int(1)       default 1 comment '是否为外链（0是 1否）',
    is_cache    int(1)       default 0 comment '是否缓存（0缓存 1不缓存）',
    menu_type   char(1)      default '' comment '菜单类型（M目录 C菜单 F按钮）',
    visible     char(1)      default 0 comment '菜单状态（0显示 1隐藏）',
    status      char(1)      default 0 comment '菜单状态（0正常 1停用）',
    perms       varchar(100) default null comment '权限标识',
    icon        varchar(100) default '#' comment '菜单图标',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark      varchar(500) default '' comment '备注',
    primary key (menu_id)
) engine = innodb
  auto_increment = 2000 comment = '菜单权限表';

-- ----------------------------
-- 初始化-菜单信息表数据
-- ----------------------------
-- 一级菜单
insert into sys_menu
values ('1', '问卷中心', '0', '1', 'questionnaire', null, '', 1, 0, 'M', '0', '0', '', 'form', 'admin', sysdate(), '', null,
        '问卷中心目录');
insert into sys_menu
values ('2', '互助大厅', '0', '2', 'help', 'help/index', '', 1, 0, 'C', '0', '0', '', 'guide', 'admin', sysdate(), '', null,
        '互助大厅');
insert into sys_menu
values ('3', '系统管理', '0', '3', 'system', null, '', 1, 0, 'M', '0', '0', '', 'system', 'root', sysdate(), '', null,
        '系统管理目录');
insert into sys_menu
values ('4', '系统监控', '0', '4', 'monitor', null, '', 1, 0, 'M', '0', '0', '', 'monitor', 'root', sysdate(), '', null,
        '系统监控目录');
-- 二级菜单
insert into sys_menu
values ('100', '新建问卷', '1', '1', 'new', 'questionnaire/new/index', '', 1, 0, 'C', '0', '0', 'center:questionnaire:new',
        'edit',
        'admin', sysdate(), '', null, '新建问卷菜单');
insert into sys_menu
values ('101', '全部问卷', '1', '2', 'all', 'questionnaire/all/index', '', 1, 0, 'C', '0', '0', 'center:questionnaire:all',
        'documentation',
        'admin', sysdate(), '', null, '全部问卷菜单');
insert into sys_menu
values ('102', '星标问卷', '1', '3', 'star', 'questionnaire/star/index', '', 1, 0, 'C', '0', '0',
        'center:questionnaire:star', 'star',
        'admin', sysdate(), '', null, '星标问卷菜单');
insert into sys_menu
values ('103', '回收站', '1', '4', 'trash', 'questionnaire/trash/index', '', 1, 0, 'C', '0', '0',
        'center:questionnaire:trash', 'example',
        'admin', sysdate(), '', null, '回收站菜单');
insert into sys_menu
values ('300', '用户管理', '3', '1', 'user', 'system/user/index', '', 1, 0, 'C', '0', '0', 'system:user:list', 'user',
        'admin', sysdate(), '', null, '用户管理菜单');
insert into sys_menu
values ('301', '角色管理', '3', '2', 'role', 'system/role/index', '', 1, 0, 'C', '0', '0', 'system:role:list', 'peoples',
        'admin', sysdate(), '', null, '角色管理菜单');
insert into sys_menu
values ('302', '菜单管理', '3', '3', 'menu', 'system/menu/index', '', 1, 0, 'C', '0', '0', 'system:menu:list', 'tree-table',
        'admin', sysdate(), '', null, '菜单管理菜单');
insert into sys_menu
values ('303', '部门管理', '3', '4', 'dept', 'system/dept/index', '', 1, 0, 'C', '0', '0', 'system:dept:list', 'tree',
        'admin', sysdate(), '', null, '部门管理菜单');
insert into sys_menu
values ('305', '字典管理', '3', '6', 'dict', 'system/dict/index', '', 1, 0, 'C', '0', '0', 'system:dict:list', 'dict',
        'admin', sysdate(), '', null, '字典管理菜单');
insert into sys_menu
values ('306', '参数设置', '3', '7', 'config', 'system/config/index', '', 1, 0, 'C', '0', '0', 'system:config:list', 'edit',
        'admin', sysdate(), '', null, '参数设置菜单');
insert into sys_menu
values ('307', '通知公告', '3', '8', 'notice', 'system/notice/index', '', 1, 0, 'C', '0', '0', 'system:notice:list',
        'message', 'admin', sysdate(), '', null, '通知公告菜单');
insert into sys_menu
values ('308', '日志管理', '3', '9', 'log', '', '', 1, 0, 'M', '0', '0', '', 'log', 'admin', sysdate(), '', null, '日志管理菜单');
insert into sys_menu
values ('401', '在线用户', '4', '1', 'online', 'monitor/online/index', '', 1, 0, 'C', '0', '0', 'monitor:online:list',
        'online', 'admin', sysdate(), '', null, '在线用户菜单');
insert into sys_menu
values ('402', '服务监控', '4', '2', 'server', 'monitor/server/index', '', 1, 0, 'C', '0', '0', 'monitor:server:list',
        'server', 'admin', sysdate(), '', null, '服务监控菜单');
insert into sys_menu
values ('403', '缓存监控', '4', '3', 'cache', 'monitor/cache/index', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis',
        'admin', sysdate(), '', null, '缓存监控菜单');
-- 三级菜单
insert into sys_menu
values ('500', '操作日志', '308', '1', 'operlog', 'monitor/operlog/index', '', 1, 0, 'C', '0', '0', 'monitor:operlog:list',
        'form', 'admin', sysdate(), '', null, '操作日志菜单');
insert into sys_menu
values ('501', '登录日志', '308', '2', 'logininfor', 'monitor/logininfor/index', '', 1, 0, 'C', '0', '0',
        'monitor:logininfor:list', 'logininfor', 'admin', sysdate(), '', null, '登录日志菜单');
-- 用户管理按钮
insert into sys_menu
values ('1001', '用户查询', '100', '1', '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', sysdate(), '',
        null, '');
insert into sys_menu
values ('1002', '用户新增', '100', '2', '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', sysdate(), '',
        null, '');
insert into sys_menu
values ('1003', '用户修改', '100', '3', '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', sysdate(), '',
        null, '');
insert into sys_menu
values ('1004', '用户删除', '100', '4', '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin', sysdate(), '',
        null, '');
insert into sys_menu
values ('1005', '用户导出', '100', '5', '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', 'admin', sysdate(), '',
        null, '');
insert into sys_menu
values ('1006', '用户导入', '100', '6', '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', 'admin', sysdate(), '',
        null, '');
insert into sys_menu
values ('1007', '重置密码', '100', '7', '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', sysdate(),
        '', null, '');
-- 角色管理按钮
insert into sys_menu
values ('1008', '角色查询', '101', '1', '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', 'admin', sysdate(), '',
        null, '');
insert into sys_menu
values ('1009', '角色新增', '101', '2', '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', 'admin', sysdate(), '',
        null, '');
insert into sys_menu
values ('1010', '角色修改', '101', '3', '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', 'admin', sysdate(), '',
        null, '');
insert into sys_menu
values ('1011', '角色删除', '101', '4', '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', 'admin', sysdate(), '',
        null, '');
insert into sys_menu
values ('1012', '角色导出', '101', '5', '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', 'admin', sysdate(), '',
        null, '');
-- 菜单管理按钮
insert into sys_menu
values ('1013', '菜单查询', '102', '1', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 'admin', sysdate(), '',
        null, '');
insert into sys_menu
values ('1014', '菜单新增', '102', '2', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 'admin', sysdate(), '',
        null, '');
insert into sys_menu
values ('1015', '菜单修改', '102', '3', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 'admin', sysdate(), '',
        null, '');
insert into sys_menu
values ('1016', '菜单删除', '102', '4', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 'admin', sysdate(), '',
        null, '');
-- 部门管理按钮
insert into sys_menu
values ('1017', '部门查询', '103', '1', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', 'admin', sysdate(), '',
        null, '');
insert into sys_menu
values ('1018', '部门新增', '103', '2', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', 'admin', sysdate(), '',
        null, '');
insert into sys_menu
values ('1019', '部门修改', '103', '3', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', 'admin', sysdate(), '',
        null, '');
insert into sys_menu
values ('1020', '部门删除', '103', '4', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', 'admin', sysdate(), '',
        null, '');
-- 字典管理按钮
insert into sys_menu
values ('1026', '字典查询', '105', '1', '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', 'admin', sysdate(), '',
        null, '');
insert into sys_menu
values ('1027', '字典新增', '105', '2', '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', 'admin', sysdate(), '',
        null, '');
insert into sys_menu
values ('1028', '字典修改', '105', '3', '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', 'admin', sysdate(), '',
        null, '');
insert into sys_menu
values ('1029', '字典删除', '105', '4', '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', 'admin', sysdate(), '',
        null, '');
insert into sys_menu
values ('1030', '字典导出', '105', '5', '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', 'admin', sysdate(), '',
        null, '');
-- 参数设置按钮
insert into sys_menu
values ('1031', '参数查询', '106', '1', '#', '', '', 1, 0, 'F', '0', '0', 'system:config:query', '#', 'admin', sysdate(),
        '', null, '');
insert into sys_menu
values ('1032', '参数新增', '106', '2', '#', '', '', 1, 0, 'F', '0', '0', 'system:config:add', '#', 'admin', sysdate(), '',
        null, '');
insert into sys_menu
values ('1033', '参数修改', '106', '3', '#', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', sysdate(), '',
        null, '');
insert into sys_menu
values ('1034', '参数删除', '106', '4', '#', '', '', 1, 0, 'F', '0', '0', 'system:config:remove', '#', 'admin', sysdate(),
        '', null, '');
insert into sys_menu
values ('1035', '参数导出', '106', '5', '#', '', '', 1, 0, 'F', '0', '0', 'system:config:export', '#', 'admin', sysdate(),
        '', null, '');
-- 通知公告按钮
insert into sys_menu
values ('1036', '公告查询', '107', '1', '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:query', '#', 'admin', sysdate(),
        '', null, '');
insert into sys_menu
values ('1037', '公告新增', '107', '2', '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:add', '#', 'admin', sysdate(), '',
        null, '');
insert into sys_menu
values ('1038', '公告修改', '107', '3', '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit', '#', 'admin', sysdate(), '',
        null, '');
insert into sys_menu
values ('1039', '公告删除', '107', '4', '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove', '#', 'admin', sysdate(),
        '', null, '');
-- 操作日志按钮
insert into sys_menu
values ('1040', '操作查询', '500', '1', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:query', '#', 'admin', sysdate(),
        '', null, '');
insert into sys_menu
values ('1041', '操作删除', '500', '2', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:remove', '#', 'admin', sysdate(),
        '', null, '');
insert into sys_menu
values ('1042', '日志导出', '500', '4', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:export', '#', 'admin', sysdate(),
        '', null, '');
-- 登录日志按钮
insert into sys_menu
values ('1043', '登录查询', '501', '1', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:query', '#', 'admin',
        sysdate(), '', null, '');
insert into sys_menu
values ('1044', '登录删除', '501', '2', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:remove', '#', 'admin',
        sysdate(), '', null, '');
insert into sys_menu
values ('1045', '日志导出', '501', '3', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:export', '#', 'admin',
        sysdate(), '', null, '');
-- 在线用户按钮
insert into sys_menu
values ('1046', '在线查询', '109', '1', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query', '#', 'admin', sysdate(),
        '', null, '');
insert into sys_menu
values ('1047', '批量强退', '109', '2', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#', 'admin',
        sysdate(), '', null, '');
insert into sys_menu
values ('1048', '单条强退', '109', '3', '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', 'admin',
        sysdate(), '', null, '');

-- ----------------------------
-- 用户和角色关联表  用户N-M角色
-- ----------------------------
create table sys_user_role
(
    user_id bigint(20) not null comment '用户ID',
    role_id bigint(20) not null comment '角色ID',
    primary key (user_id, role_id),
    constraint foreign key (user_id) references sys_user (user_id),
    constraint foreign key (role_id) references sys_role (role_id)
) engine = innodb comment = '用户和角色关联表';

-- ----------------------------
-- 初始化-用户和角色关联表数据
-- ----------------------------
insert into sys_user_role
values ('1', '1');
insert into sys_user_role
values ('2', '2');
insert into sys_user_role
values ('3', '3');
insert into sys_user_role
values ('3', '4');


-- ----------------------------
-- 角色和菜单关联表  角色N-M菜单
-- ----------------------------
create table sys_role_menu
(
    role_id bigint(20) not null comment '角色ID',
    menu_id bigint(20) not null comment '菜单ID',
    primary key (role_id, menu_id),
    constraint foreign key (role_id) references sys_role (role_id),
    constraint foreign key (menu_id) references sys_menu (menu_id)
) engine = innodb comment = '角色和菜单关联表';

-- ----------------------------
-- 初始化-角色和菜单关联表数据
-- ----------------------------
insert into sys_role_menu
values ('2', '1');
insert into sys_role_menu
values ('2', '2');
insert into sys_role_menu
values ('2', '3');
insert into sys_role_menu
values ('2', '100');
insert into sys_role_menu
values ('2', '101');
insert into sys_role_menu
values ('2', '102');
insert into sys_role_menu
values ('2', '103');
insert into sys_role_menu
values ('2', '300'); -- 用户管理
insert into sys_role_menu
values ('2', '301'); -- 角色管理
insert into sys_role_menu
values ('2', '303'); -- 部门管理
insert into sys_role_menu
values ('2', '307'); -- 通知公告
insert into sys_role_menu
values ('2', '308'); -- 日志管理
insert into sys_role_menu
values ('2', '501'); -- 登录日志
insert into sys_role_menu
values ('2', '1001');
insert into sys_role_menu
values ('2', '1002');
insert into sys_role_menu
values ('2', '1003');
insert into sys_role_menu
values ('2', '1004');
insert into sys_role_menu
values ('2', '1005');
insert into sys_role_menu
values ('2', '1006');
insert into sys_role_menu
values ('2', '1007');
insert into sys_role_menu
values ('2', '1008');
insert into sys_role_menu
values ('2', '1009');
insert into sys_role_menu
values ('2', '1010');
insert into sys_role_menu
values ('2', '1011');
insert into sys_role_menu
values ('2', '1012');
insert into sys_role_menu
values ('2', '1017');
insert into sys_role_menu
values ('2', '1018');
insert into sys_role_menu
values ('2', '1019');
insert into sys_role_menu
values ('2', '1020');
insert into sys_role_menu
values ('2', '1036');
insert into sys_role_menu
values ('2', '1037');
insert into sys_role_menu
values ('2', '1038');
insert into sys_role_menu
values ('2', '1039');
insert into sys_role_menu
values ('2', '1043');
insert into sys_role_menu
values ('2', '1044');
insert into sys_role_menu
values ('2', '1045');
-- 问卷设计者
insert into sys_role_menu
values ('3', '1');
insert into sys_role_menu
values ('3', '2');
insert into sys_role_menu
values ('3', '100');
insert into sys_role_menu
values ('3', '101');
insert into sys_role_menu
values ('3', '102');
insert into sys_role_menu
values ('3', '103');
-- 问卷填写者
insert into sys_role_menu
values ('4', '1');
insert into sys_role_menu
values ('4', '2');
insert into sys_role_menu
values ('4', '101');
insert into sys_role_menu
values ('4', '102');

-- ----------------------------
-- 角色和部门关联表  角色N-M部门
-- ----------------------------
create table sys_role_dept
(
    role_id bigint(20) not null comment '角色ID',
    dept_id bigint(20) not null comment '部门ID',
    primary key (role_id, dept_id),
    constraint foreign key (role_id) references sys_role (role_id),
    constraint foreign key (dept_id) references sys_dept (dept_id)
) engine = innodb comment = '角色和部门关联表';

-- ----------------------------
-- 初始化-角色和部门关联表数据
-- ----------------------------
insert into sys_role_dept
values ('1', '100');
insert into sys_role_dept
values ('1', '101');
insert into sys_role_dept
values ('1', '102');
insert into sys_role_dept
values ('2', '100');
insert into sys_role_dept
values ('2', '101');
insert into sys_role_dept
values ('2', '103');
insert into sys_role_dept
values ('2', '104');
insert into sys_role_dept
values ('3', '100');
insert into sys_role_dept
values ('3', '101');
insert into sys_role_dept
values ('3', '104');
insert into sys_role_dept
values ('4', '100');
insert into sys_role_dept
values ('4', '101');
insert into sys_role_dept
values ('4', '104');

-- ----------------------------
-- 操作日志记录
-- ----------------------------
create table sys_oper_log
(
    oper_id        bigint(20) not null auto_increment comment '日志主键',
    title          varchar(50)   default '' comment '模块标题',
    business_type  int(2)        default 0 comment '业务类型（0其它 1新增 2修改 3删除）',
    method         varchar(100)  default '' comment '方法名称',
    request_method varchar(10)   default '' comment '请求方式',
    operator_type  int(1)        default 0 comment '操作类别（0其它 1后台用户 2手机端用户）',
    oper_name      varchar(50)   default '' comment '操作人员',
    dept_name      varchar(50)   default '' comment '部门名称',
    oper_url       varchar(255)  default '' comment '请求URL',
    oper_ip        varchar(128)  default '' comment '主机地址',
    oper_location  varchar(255)  default '' comment '操作地点',
    oper_param     varchar(2000) default '' comment '请求参数',
    json_result    varchar(2000) default '' comment '返回参数',
    status         int(1)        default 0 comment '操作状态（0正常 1异常）',
    error_msg      varchar(2000) default '' comment '错误消息',
    oper_time      datetime comment '操作时间',
    primary key (oper_id)
) engine = innodb
  auto_increment = 100 comment = '操作日志记录';


-- ----------------------------
-- 字典类型表
-- ----------------------------
create table sys_dict_type
(
    dict_id     bigint(20) not null auto_increment comment '字典主键',
    dict_name   varchar(100) default '' comment '字典名称',
    dict_type   varchar(100) default '' comment '字典类型',
    status      char(1)      default '0' comment '状态（0正常 1停用）',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark      varchar(500) default null comment '备注',
    primary key (dict_id),
    unique (dict_type)
) engine = innodb
  auto_increment = 100 comment = '字典类型表';

insert into sys_dict_type
values (1, '用户性别', 'sys_user_sex', '0', 'admin', sysdate(), '', null, '用户性别列表');
insert into sys_dict_type
values (2, '菜单状态', 'sys_show_hide', '0', 'admin', sysdate(), '', null, '菜单状态列表');
insert into sys_dict_type
values (3, '系统开关', 'sys_normal_disable', '0', 'admin', sysdate(), '', null, '系统开关列表');
insert into sys_dict_type
values (4, '系统是否', 'sys_yes_no', '0', 'admin', sysdate(), '', null, '系统是否列表');
insert into sys_dict_type
values (5, '通知类型', 'sys_notice_type', '0', 'admin', sysdate(), '', null, '通知类型列表');
insert into sys_dict_type
values (6, '通知状态', 'sys_notice_status', '0', 'admin', sysdate(), '', null, '通知状态列表');
insert into sys_dict_type
values (7, '操作类型', 'sys_oper_type', '0', 'admin', sysdate(), '', null, '操作类型列表');
insert into sys_dict_type
values (8, '系统状态', 'sys_common_status', '0', 'admin', sysdate(), '', null, '登录状态列表');


-- ----------------------------
-- 字典数据表
-- ----------------------------
create table sys_dict_data
(
    dict_code   bigint(20) not null auto_increment comment '字典编码',
    dict_sort   int(4)       default 0 comment '字典排序',
    dict_label  varchar(100) default '' comment '字典标签',
    dict_value  varchar(100) default '' comment '字典键值',
    dict_type   varchar(100) default '' comment '字典类型',
    css_class   varchar(100) default null comment '样式属性（其他样式扩展）',
    list_class  varchar(100) default null comment '表格回显样式',
    is_default  char(1)      default 'N' comment '是否默认（Y是 N否）',
    status      char(1)      default '0' comment '状态（0正常 1停用）',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark      varchar(500) default null comment '备注',
    primary key (dict_code)
) engine = innodb
  auto_increment = 100 comment = '字典数据表';

insert into sys_dict_data
values (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', sysdate(), '', null, '性别男');
insert into sys_dict_data
values (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', sysdate(), '', null, '性别女');
insert into sys_dict_data
values (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', sysdate(), '', null, '性别未知');
insert into sys_dict_data
values (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', sysdate(), '', null, '显示菜单');
insert into sys_dict_data
values (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', sysdate(), '', null, '隐藏菜单');
insert into sys_dict_data
values (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', sysdate(), '', null, '正常状态');
insert into sys_dict_data
values (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', sysdate(), '', null, '停用状态');
insert into sys_dict_data
values (8, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', sysdate(), '', null, '系统默认是');
insert into sys_dict_data
values (9, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', sysdate(), '', null, '系统默认否');
insert into sys_dict_data
values (10, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', sysdate(), '', null, '通知');
insert into sys_dict_data
values (11, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '公告');
insert into sys_dict_data
values (12, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', sysdate(), '', null, '正常状态');
insert into sys_dict_data
values (13, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', sysdate(), '', null, '关闭状态');
insert into sys_dict_data
values (14, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', sysdate(), '', null, '新增操作');
insert into sys_dict_data
values (15, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', 'admin', sysdate(), '', null, '修改操作');
insert into sys_dict_data
values (16, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', sysdate(), '', null, '删除操作');
insert into sys_dict_data
values (17, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '授权操作');
insert into sys_dict_data
values (18, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '导出操作');
insert into sys_dict_data
values (19, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '导入操作');
insert into sys_dict_data
values (20, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', sysdate(), '', null, '强退操作');
insert into sys_dict_data
values (21, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '生成操作');
insert into sys_dict_data
values (22, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', sysdate(), '', null, '清空操作');
insert into sys_dict_data
values (23, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '正常状态');
insert into sys_dict_data
values (24, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', sysdate(), '', null, '停用状态');


-- ----------------------------
-- 参数配置表
-- ----------------------------
create table sys_config
(
    config_id    int(5) not null auto_increment comment '参数主键',
    config_name  varchar(100) default '' comment '参数名称',
    config_key   varchar(100) default '' comment '参数键名',
    config_value varchar(500) default '' comment '参数键值',
    config_type  char(1)      default 'N' comment '系统内置（Y是 N否）',
    create_by    varchar(64)  default '' comment '创建者',
    create_time  datetime comment '创建时间',
    update_by    varchar(64)  default '' comment '更新者',
    update_time  datetime comment '更新时间',
    remark       varchar(500) default null comment '备注',
    primary key (config_id)
) engine = innodb
  auto_increment = 100 comment = '参数配置表';

insert into sys_config
values (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', 'admin', sysdate(), '', null,
        '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
insert into sys_config
values (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', sysdate(), '', null, '初始化密码 123456');
insert into sys_config
values (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', 'admin', sysdate(), '', null,
        '深色主题theme-dark，浅色主题theme-light');
insert into sys_config
values (4, '账号自助-验证码开关', 'sys.account.captchaOnOff', 'true', 'Y', 'admin', sysdate(), '', null,
        '是否开启验证码功能（true开启，false关闭）');
insert into sys_config
values (5, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'true', 'Y', 'admin', sysdate(), '', null,
        '是否开启注册用户功能（true开启，false关闭）');


-- ----------------------------
-- 系统访问记录
-- ----------------------------
create table sys_logininfor
(
    info_id        bigint(20) not null auto_increment comment '访问ID',
    user_name      varchar(50)  default '' comment '用户账号',
    ipaddr         varchar(128) default '' comment '登录IP地址',
    login_location varchar(255) default '' comment '登录地点',
    browser        varchar(50)  default '' comment '浏览器类型',
    os             varchar(50)  default '' comment '操作系统',
    status         char(1)      default '0' comment '登录状态（0成功 1失败）',
    msg            varchar(255) default '' comment '提示消息',
    login_time     datetime comment '访问时间',
    primary key (info_id)
) engine = innodb
  auto_increment = 100 comment = '系统访问记录';


-- ----------------------------
-- 通知公告表
-- ----------------------------
create table sys_notice
(
    notice_id      int(4)      not null auto_increment comment '公告ID',
    notice_title   varchar(50) not null comment '公告标题',
    notice_type    char(1)     not null comment '公告类型（1通知 2公告）',
    notice_content longblob     default null comment '公告内容',
    status         char(1)      default '0' comment '公告状态（0正常 1关闭）',
    create_by      varchar(64)  default '' comment '创建者',
    create_time    datetime comment '创建时间',
    update_by      varchar(64)  default '' comment '更新者',
    update_time    datetime comment '更新时间',
    remark         varchar(255) default null comment '备注',
    primary key (notice_id)
) engine = innodb
  auto_increment = 10 comment = '通知公告表';

-- ----------------------------
-- 初始化-公告信息表数据
-- ----------------------------
insert into sys_notice
values ('1', '苏小问校园问卷系统开发公告', '2', '开发中，敬请期待！', '0', 'root', sysdate(), '', null, '管理员');

-- ----------------------------
-- 问卷表
-- ----------------------------
create table questionnaire
(
    questionnaire_id     varchar(40)  not null comment '问卷ID',
    title                varchar(255) not null comment '问卷标题',
    introduction         varchar(255) default '' comment '问卷简介',
    structure            varchar(32)  default '' comment '问卷结构 Document ID',
    user_id              bigint(20)   not null comment '创建者',
    create_time          datetime     not null comment '创建时间',
    due_time             datetime     default null comment '到期时间',
    questionnaire_status char(1)      default '0' comment '问卷状态（0-未发布 1-收集中 2-已结束）',
    del_flag             char(1)      default '0' comment '删除标记（0-未删除 1-已删除）',
    star_flag            char(1)      default '0' comment '星标（0-否 1-是）',
    modify_flag          char(1)      default '0' comment '可修改答卷（0-否 1-是）',
    anonymous_flag       char(1)      default '0' comment '可匿名答题（0-否 1-是）',
    primary key (questionnaire_id),
    constraint foreign key (user_id) references sys_user (user_id)
) engine = innodb comment = '问卷表';

-- ----------------------------
-- 答卷表
-- ----------------------------
create table questionnaire_answer
(
    answer_id        varchar(40) not null comment '答卷ID',
    questionnaire_id varchar(40) not null comment '问卷ID',
    detail           varchar(32)  default '' comment '答卷内容 Document ID',
    user_id          bigint(20)   default null comment '填写者',
    create_time      datetime    not null comment '填写时间',
    fill_ip          varchar(128) default '' comment '填写IP',
    star_flag        char(1)      default '0' comment '星标（0-否 1-是）',
    answer_status    char(1)      default '0' comment '答卷状态（0-有效 1-无效 2-邀请）',
    primary key (answer_id),
    constraint foreign key (questionnaire_id) references questionnaire (questionnaire_id),
    constraint foreign key (user_id) references sys_user (user_id)
) engine = innodb comment = '答卷表';

-- ----------------------------
-- 互助平台表
-- ----------------------------
create table question_help
(
    task_id          varchar(40) not null comment '互助任务ID',
    questionnaire_id varchar(40) not null unique comment '目标问卷ID',
    price            bigint(20) default 0 comment '悬赏积分',
    amount           bigint(20) default 0 comment '目标个数',
    counts           bigint(20) default 0 comment '已填写个数',
    enabled          char(1)    default '0' comment '启用状态（0-启用 1-停用）',
    primary key (task_id),
    constraint foreign key (questionnaire_id) references questionnaire (questionnaire_id)
) engine = innodb comment = '互助平台表';