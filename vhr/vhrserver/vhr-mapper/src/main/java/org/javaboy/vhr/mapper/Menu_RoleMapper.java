package org.javaboy.vhr.mapper;

import org.apache.ibatis.annotations.Param;
import org.javaboy.vhr.model.Menu_Role;

public interface Menu_RoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Menu_Role record);

    int insertSelective(Menu_Role record);

    Menu_Role selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Menu_Role record);

    int updateByPrimaryKey(Menu_Role record);

    void deleteByRid();

    Integer insertReCord(@Param("rid") Integer rid, @Param("mids") Integer[] mids);
}