package org.javaboy.vhr.controller.System.basic;

import org.javaboy.vhr.model.Menu;
import org.javaboy.vhr.model.RespBean;
import org.javaboy.vhr.model.Role;
import org.javaboy.vhr.service.MenuService;
import org.javaboy.vhr.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//权限组操作
@RestController
@RequestMapping("/system/basic/permiss")
public class PermissController {
    @Autowired
    RoleService roleService;
    @Autowired
    MenuService menuService;

    @GetMapping("/")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping("/menus")
    public List<Menu> getAllMenus(){
        return menuService.getAllMenus();
    }


    @GetMapping("/mids/{rid}")
    public List<Integer> getMIdsByRid(@PathVariable Integer rid){
        return menuService.getMIdsByRid(rid);
    }


    //菜单树的更新操作
    @PutMapping("/")
    public RespBean updateMenuRole(Integer rid, Integer[] mids){
        if(menuService.updateMenuRole(rid,mids)){
            return RespBean.ok("您好,您刚刚的数据更新操作成功(●'◡'●)");
        }
        return RespBean.error("对不起,您刚刚的数据更新操作失败了￣へ￣");
    }

    @PostMapping("/")
    public RespBean addRole(@RequestBody Role role){
        if (roleService.addRole(role)==1){
            return RespBean.ok("您刚刚的添加操作已成功执行(●'◡'●)");
        }
        return RespBean.error("您刚刚的添加操作执行失败了(；′⌒`)请重试~");
    }

    @DeleteMapping("/del/{rid}")
    public RespBean deleteRoleById(@PathVariable Integer rid){
        if(roleService.deleteRoleById(rid)==1){
           return RespBean.ok("该删除操作已执行成功o(*￣▽￣*)ブ");
        }
        return RespBean.error("对不起,此删除操作执行失败了,请稍后重试￣へ￣");
    }

}
