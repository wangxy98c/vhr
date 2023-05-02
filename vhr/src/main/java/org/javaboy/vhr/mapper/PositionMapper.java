package org.javaboy.vhr.mapper;

import org.apache.ibatis.annotations.Param;
import org.javaboy.vhr.model.Position;

import java.util.List;

public interface PositionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Position record);

    int insertSelective(Position record);

    Position selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Position record);

    int updateByPrimaryKey(Position record);

    List<Position> getAllPositions();

    //注意事项：此处的@Param("ids") 注解，用于对应xml中的ids
    Integer deletePositionsByIds(@Param("ids") Integer[] ids);
}