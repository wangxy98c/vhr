package org.javaboy.vhr.controller.config.sys.basic;

import org.javaboy.vhr.model.Position;
import org.javaboy.vhr.model.RespBean;
import org.javaboy.vhr.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController///    不要写成Controller ！！！，会返回404。。RestController=ResponseBody+Controller
@RequestMapping("/system/basic/pos")
public class PositionController {
    @Autowired
    PositionService positionService;
    @GetMapping("/")
    public List<Position> getAllPostion(){
        System.out.println("--getAllPosition");
        List<Position> res=positionService.getAllPosition();
        for (Position position : res) {
            System.out.println(position.getName());
        }
        return res;
        //return positionService.getAllPosition();
    }
    @PostMapping("/")
    public RespBean addPosition(@RequestBody Position position){
        System.out.println("--addPosition");
        if(positionService.addPosition(position)==1){
            return RespBean.ok("添加成功");
        }else{
            return RespBean.error("添加失败");
        }
    }
    //注意事项：学习只更新一部分的xml方法（前端不用传输全部的数据，只需要给出更新部分）
    @PutMapping("/")
    public RespBean updatePositions(@RequestBody Position position){
        if(positionService.updatePosition(position)==1){
            return RespBean.ok("更新成功");
        }else{
            return  RespBean.error("更新失败");
        }
    }

    //注意此处的删除并不是传递RequestBody，而是直接在地址里传递职务的id进行删除。以及注意参数前的@PathVariable
    @DeleteMapping("/{id}")
    public RespBean deletePositionById(@PathVariable Integer id){
        if(positionService.deletePositionById(id)==1){//论为什么是1：因为最终sql操作返回的是成功的行数。即1。故传递回此处即1
            return RespBean.ok("删除成功");
        }else{
            return  RespBean.error(("删除失败"));
        }
    }

    //注意事项： 学习如何数组形式地使用xml
    @DeleteMapping("/")
    public RespBean deletePositionByIds(Integer[] ids){
        //数据用
        if(positionService.deletePositionByIds(ids)==ids.length){
            return RespBean.ok("批量删除成功");
        }else {
            return RespBean.error("批量删除失败");
        }
    }
}
