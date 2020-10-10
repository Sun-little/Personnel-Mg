package org.javaboy.vhr.service;

import org.javaboy.vhr.mapper.MenuMapper;
import org.javaboy.vhr.mapper.Menu_RoleMapper;
import org.javaboy.vhr.model.Hr;
import org.javaboy.vhr.model.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {
    @Autowired
    MenuMapper menuMapper;
    @Autowired
    Menu_RoleMapper menu_roleMapper;
    public List<Menu> getMenusByHrId() {
        //拿到登录用户对象(getPrincipal即拿到的对象)中的 Id属性
        return menuMapper.getMenusByHrId(((Hr) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
    }

    //@Cacheable 开启缓存
    public List<Menu> getAllMenusWithRole(){
        return menuMapper.getAllMenusWithRole();
    }

    public List<Menu> getAllMenus() {
        return menuMapper.getAllMenus();
    }

    public List<Integer> getMIdsByRid(Integer rid) {
        return menuMapper.getMIdsByRid(rid);
    }

    //添加事务注解(避免数据更新操作失败)
    @Transactional
    public boolean updateMenuRole(Integer rid, Integer[] mids) {
        menu_roleMapper.deleteByRid();

        //判断权限组删除所有权限の失败问题
        if(mids== null || mids.length==0){
            return true;
        }
        Integer result = menu_roleMapper.insertReCord(rid,mids);
        return result==mids.length;
    }
}
