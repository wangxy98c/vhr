package org.javaboy.vhr.config;

import org.javaboy.vhr.model.RespBean;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;


//全局异常处理
@RestControllerAdvice
public class GlobalExcepotionHandler {
    @ExceptionHandler(SQLException.class)//如果没有导入包，删掉pom.xml里对应插件的runtime标签。
    //  此处找到异常的祖先异常，统一处理
    public RespBean mySQLException(SQLException e){
        if(e instanceof SQLIntegrityConstraintViolationException){
            return RespBean.error("该数据有关联数据，操作失败");
        }

        return RespBean.error("数据库异常，操作失败。详情请咨询");
    }
}
