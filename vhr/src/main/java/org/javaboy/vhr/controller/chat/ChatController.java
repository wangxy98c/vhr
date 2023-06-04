package org.javaboy.vhr.controller.chat;

import org.javaboy.vhr.model.Hr;
import org.javaboy.vhr.service.HrService;
import org.javaboy.vhr.utils.HrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    HrService hrService;
    @GetMapping("/hrs")//仅获取hr的信息，不包含聊天记录
    public List<Hr> getAllHrs(){
        return hrService.getAllHrsExceptCurrentHr();
    }
}
