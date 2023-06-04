# 后端遇到的问题
### Spring Security 版本升级后的废弃
    暂使用旧版函数，待升级中。。
### 需要注意的是：角色必须含有ROLE_前缀。
    大概和Security有关。还没确认。先记着
### 系统管理-基础信息设置-部门管理
    使用了递归式的查询（从根目录）mybatis。xml文件中。
### mybatis 存储过程
    <select id="addDep" statementType="CALLABLE">
        call addDep(#{name,mode=IN,jdbcType=VARCHAR},#{parentId,mode=IN,jdbcType=INTEGER},#{enabled,mode=IN,jdbcType=BOOLEAN},#{result,mode=OUT,jdbcType=INTEGER},#{id,mode=OUT,jdbcType=INTEGER})
    </select>
    mode有IN OUT 两种
### @Transactional注解
    不能一起完成则回退
### 引入poi实现数据的导入导出功能
    代码属于EmpBasic页面
### 使用RabbitMQ启用邮件服务
    1。使用Thyleaf模版
    2。mailReceiver
    3。添加员工后调用
### 组件回填：添加员工后把生成的id填入原本没有id的参数中
    xml文件中：
    useGenerateKeys="true"  keyProperty="id"
### 日期格式
    JsonFormat(pattern="yyyy-mm-dd) private Date date;
### websocket
    WScontroller、WebSocketConfig、以及相应的model