package org.javaboy.vhr.service;

import org.javaboy.vhr.mapper.PositionMapper;
import org.javaboy.vhr.model.Position;
import org.javaboy.vhr.model.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PositionService
{
    @Autowired
    PositionMapper positionMapper;
    public List<Position> getAllPosition(){
//        List<Position> res=positionMapper.getAllPositions();
//        for (Position position : res) {
//            System.out.println(position.getName());
//        }
//        return res;
        return positionMapper.getAllPositions();
    }
    public int addPosition(Position position){
        position.setEnabled(true);
        position.setCreateDate(new Date());
        return positionMapper.insertSelective(position);
    }
    public int deletePositionById(Integer id){
        return positionMapper.deleteByPrimaryKey(id);
    }
    public int updatePosition(Position position){
        return positionMapper.updateByPrimaryKeySelective(position);
        //xxxSelective是根据传进来的字段更新而不是全部数据更新,如果数据为空就不会更新此字段
    }

    public int deletePositionByIds(Integer[] ids) {
        return positionMapper.deletePositionsByIds(ids);
    }
}
