package org.javaboy.vhr.controller.salary;


import org.javaboy.vhr.model.RespBean;
import org.javaboy.vhr.model.Salary;
import org.javaboy.vhr.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/salary/sob")
public class SalaryController {

    @Autowired
    SalaryService salaryService;

    @GetMapping("/")
    public List<Salary> getAllSaleries(){
        return salaryService.getAllSaleries();
    }

    //添加操作
    @PostMapping("/")
    public RespBean addSalary(@RequestBody Salary salary){
        if(salaryService.addSalary(salary) ==1){
            return RespBean.ok("该工资套账添加操作已成功执行🤭");
        }
        return RespBean.error("该工资套账添加操作执行失败了,请稍后重试(●ˇ∀ˇ●)");
    }

    //删除操作
    @DeleteMapping("/{id}")
    public RespBean deleteSal(@PathVariable Integer id){
        if(salaryService.deleteSal(id)==1){
           return RespBean.ok("您选择的该套账删除操作已执行成功😊");
        }
        return RespBean.error("您选择的该套账删除操作它执行失败了,请稍后重试😔");
    }

    @PutMapping("/")
    public RespBean upDateSalById(@RequestBody Salary salary){
        if(salaryService.upDateSalById(salary)== 1){
            return RespBean.ok("您刚刚的编辑操作已成功执行😊");
        }
        return RespBean.ok("您刚刚的编辑操作执行失败了,请稍后重试😔");
    }


}
