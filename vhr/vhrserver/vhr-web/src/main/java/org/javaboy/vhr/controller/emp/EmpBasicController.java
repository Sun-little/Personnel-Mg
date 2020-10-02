package org.javaboy.vhr.controller.emp;


import org.javaboy.vhr.model.*;
import org.javaboy.vhr.service.*;
import org.javaboy.vhr.utils.POIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/employee/basic")
public class EmpBasicController {
    @Autowired
    EmployeeService employeeService;
    @Autowired
    NationService nationService;
    @Autowired
    PoliticsstatusService politicsstatusService;
    @Autowired
    JobLevelService jobLevelService;
    @Autowired
    PositionService positionService;
    @Autowired
    SpecialtyService specialtyService;
    @Autowired
    DepartmentService departmentService;

    //分页查询(默认查询一页十条数据)
    @GetMapping("/")
    public RespPageBean getEmployeeByPage(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size, Employee employee, Date[] beginDateScope) {
        return employeeService.getEmployeeByPage(page, size, employee,beginDateScope);
    }

    //添加操作
    @PostMapping("/")
    public RespBean addEmp(@RequestBody Employee employee){
        if(employeeService.addEmp(employee)==1){
            return RespBean.ok("该员工信息添加操作已执行成功(●ˇ∀ˇ●)");
        }
        return RespBean.error("该员工信息添加操作它执行失败了,请稍后重试(；′⌒`)");
    }

    //拿到所有的民族
    @GetMapping("/nations")
    public List<Nation> getAllNations(){
        return nationService.getAllNations();
    }

    //拿到所有的政治面貌
    @GetMapping("/politicsstatus")
    public List<Politicsstatus> getAllPoliticsstatus(){
        return politicsstatusService.getAllPoliticsstatus();
    }

    //拿到所有的职称
    @GetMapping("/jobLevels")
    public List<JobLevel> getAllJobLevels(){
        return jobLevelService.getAllJobLevels();
    }

    @GetMapping("/positions")
    public List<Position> getAllPositions(){
        return positionService.getAllPositions();
    }

    //工号
    @GetMapping("/maxworkid")
    public RespBean  maxWorkID(){
        //添加时工号自增长
        RespBean respBean = RespBean.build().setStatus(200)
                .setObj(String.format("%08d", employeeService.maxWorkID() + 1));
        return respBean;
    }

    //拿到所有的部门
    @GetMapping("/allDeps")
    public List<Department> getAllDep(){
        return departmentService.getAllDepartments();
    }

    //删除操作
    @DeleteMapping("/{id}")
    public RespBean deleteEmpById(@PathVariable Integer id){
       if(employeeService.deleteEmpById(id)==1){
           return RespBean.ok("您选择的该数据删除成功(●ˇ∀ˇ●)");
       };
           return RespBean.error("您选择的该数据它删除失败了,请稍后重试(；′⌒`)");
    }

    //更新操作
    @PutMapping("/")
    public  RespBean updateEmp(@RequestBody Employee employee){
        if(employeeService.updateEmp(employee)==1){
            return  RespBean.ok("该员工信息更新成功(●'◡'●)");
        }
            return  RespBean.error("该员工信息更新失败了,请稍后重试（＞人＜；）");
    }

    //数据导出
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportEmpData(){
        List<Employee> list = (List<Employee>) employeeService.getEmployeeByPage(null, null, new Employee(),null).getData();
        return POIUtils.employee2Excel(list);
    }

    //数据导入第一步: 文件上传
    @PostMapping("/import")
    public RespBean importData(MultipartFile file) throws IOException {
        //数据导入第二步: 解析excel文件存到数据库
        List<Employee> list = POIUtils.excel2Employee(file, nationService.getAllNations(), politicsstatusService.getAllPoliticsstatus(), departmentService.getAllDepartmentsWithOutChildren(), positionService.getAllPositions(), jobLevelService.getAllJobLevels());

       if(employeeService.addEmps(list) == list.size()){
            return RespBean.ok("文件上传成功,正在解析,请稍后~ ~");
       }
            return RespBean.ok("文件上传失败,请稍后重试~ ~");

    }
}
