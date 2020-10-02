package org.javaboy.vhr.controller.System.basic;


import org.javaboy.vhr.model.Department;
import org.javaboy.vhr.model.RespBean;
import org.javaboy.vhr.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/basic/department")
public class DepartmentController{

    @Autowired
    DepartmentService departmentService;

    //递归拿到Department的所有数据
    @GetMapping("/")
    public List<Department> getAllDepartments(){
        return departmentService.getAllDepartments();
    }

    @PostMapping("/")
    public RespBean addDep(@RequestBody Department dep) {
        departmentService.addDep(dep);
        if (dep.getResult() == 1) {
            return RespBean.ok("此部门添加操作执行成功φ(゜▽゜*)♪", dep);
        }
        return RespBean.error("对不起,此部门添加操作失败了,请重试(；′⌒`)~");
    }

    @DeleteMapping("/{id}")
    public RespBean deleteDepById(@PathVariable Integer id) {
        Department dep = new Department();
        dep.setId(id);
        departmentService.deleteDepById(dep);
        if (dep.getResult() == -2) {
            return RespBean.error("对不起,该部门下有子部门,无法进行删除操作~");
        } else if (dep.getResult() == -1) {
            return RespBean.error("对不起,该部门下有员工,无法进行删除操作~");
        } else if (dep.getResult() == 1) {
            return RespBean.ok("告诉您个好消息,你选择的该部门已完成删除操作φ(゜▽゜*)♪~");
        }
        return RespBean.error("对不起,您刚刚的删除操作执行失败(；′⌒`)~");
    }
}
