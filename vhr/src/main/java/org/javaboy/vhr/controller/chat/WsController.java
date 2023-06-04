package org.javaboy.vhr.controller.chat;

import org.javaboy.vhr.model.ChatMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.security.Principal;
import java.util.Date;

//websocket 消息处理类
@Controller
public class WsController {
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
    @MessageMapping("/ws/chat")//这个地址是发消息的地址，
    //@SendTo("/queue/chat")//转发到此处，前端监听此地址都可以收到,广播时才用，因为没办法设置to
    public void handleMsg(Principal principal, ChatMsg chatMsg){
        //principal也可以用HrUtils获得. chatMsg中的from不可信
        chatMsg.setFrom(principal.getName());
        chatMsg.setDate(new Date());
        System.out.println(chatMsg.getFrom());
        System.out.println(chatMsg.getTo());
        System.out.println(chatMsg.getContent());

        simpMessagingTemplate.convertAndSendToUser(chatMsg.getTo(),"/queue/chat",chatMsg);
        //服务端如何知道你是谁呢？是通过security登陆时获得的。
        //第二个参数是目的队列，用前端监听

        //前端请求user/queue/chat并携带了登陆信息，后端根据登陆信息判断是不是给他的？
    }
}
