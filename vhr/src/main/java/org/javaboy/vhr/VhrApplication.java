package org.javaboy.vhr;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@MapperScan(basePackages = "org.javaboy.vhr.mapper")
public class VhrApplication {

    public static void main(String[] args) {
        SpringApplication.run(VhrApplication.class, args);
    }

    //mailserver
    @Bean
    Queue queue(){//注意是amqp.core的包
        return new Queue("javaboy.mail.welcome");
    }
}
