package org.javaboy.vhr.controller.System;


import org.javaboy.vhr.model.Hr;
import org.javaboy.vhr.model.RespBean;
import org.javaboy.vhr.model.Role;
import org.javaboy.vhr.service.HrService;
import org.javaboy.vhr.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/system/hr")
public class HrController {
    @Autowired
    HrService hrService ;
    @Autowired
    RoleService roleService;

    @GetMapping("/")
    public List<Hr> getAllHrs(String keywords){
        return hrService.getAllHrs(keywords);
    }

    //更新hr对象操作接口
    @PutMapping("/")
    public RespBean updateHr(@RequestBody Hr hr){
        if(hrService.updateHr(hr)==1){
            return RespBean.ok("您刚刚的操作已执行成功o(*￣▽￣*)ブ");
        }
        return RespBean.error("您刚刚的操作执行失败了,请稍后重试(；′⌒`)");
    }


    //获取所有角色
    @GetMapping("/roles")
    public  List<Role> getAllRoles(){
      return   roleService.getAllRoles();
    }

    //更新角色接口
    @PutMapping("/role")
    public RespBean updateHrRole(Integer hrid, Integer[] rids) {
        if (hrService.updateHrRole(hrid, rids)) {
            return RespBean.ok("角色信息更新操作以执行成功(●'◡'●)");
        }
        return RespBean.error("角色信息更新操作执行失败了,请稍后重试~┭┮﹏┭┮");
    }

    //删除操作
    @DeleteMapping("/{id}")
    public RespBean deleteHrById(@PathVariable Integer id){
        if(hrService.deleteHrById(id)==1){
            return RespBean.ok("您进行的用户删除操作已成功执行(●'◡'●)");
        }
        return RespBean.error("您进行的用户删除操作,它执行失败了,请稍后重试(；′⌒`)");
    }


}
