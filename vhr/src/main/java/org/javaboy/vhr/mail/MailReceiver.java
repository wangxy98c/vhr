package org.javaboy.vhr.mail;

import com.mysql.cj.log.LogFactory;
import org.javaboy.vhr.model.Employee;
import org.javaboy.vhr.model.Hr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@Component
public class MailReceiver {
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    MailProperties mailProperties;//用于拿到配置文件中的字段
    @Autowired
    TemplateEngine templateEngine;

    public static final Logger logger=LoggerFactory.getLogger(MailReceiver.class);
    @RabbitListener(queues = "javaboy.mail.welcome")//对应启动类中的
    public void handler(Employee employee){
        System.out.println("MailReceiver handler");
        logger.info(employee.toString());
        //收到消息，发送邮件
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg);
        try {
            helper.setTo(employee.getEmail());
            helper.setFrom(mailProperties.getUsername());
            helper.setSubject("入职欢迎邮件");//邮件主题
            helper.setSentDate(new Date());//发送日期，现在

            Context context = new Context();//与${nickname}等对应的字段
            context.setVariable("nickname",employee.getName());//其他字段略去不写了（页面里也没）

            String mail = templateEngine.process("mail", context);//第一个参数对应mail.html
            helper.setText(mail,true);
            javaMailSender.send(msg);
        } catch (MessagingException e) {
            e.printStackTrace();
            logger.error("邮件发送失败");
        }
    }
}
