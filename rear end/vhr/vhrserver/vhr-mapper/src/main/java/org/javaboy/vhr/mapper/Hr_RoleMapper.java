package org.javaboy.vhr.mapper;

import org.apache.ibatis.annotations.Param;
import org.javaboy.vhr.model.Hr_Role;

public interface Hr_RoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Hr_Role record);

    int insertSelective(Hr_Role record);

    Hr_Role selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Hr_Role record);

    int updateByPrimaryKey(Hr_Role record);

    void deleteByHrid(Integer hrid);

    Integer addRole(@Param("hrid") Integer hrid, @Param("rids") Integer[] rids);
}