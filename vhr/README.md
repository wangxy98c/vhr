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