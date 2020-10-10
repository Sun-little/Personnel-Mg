package org.javaboy.vhr.controller.System.basic;

import org.javaboy.vhr.model.JobLevel;
import org.javaboy.vhr.model.RespBean;
import org.javaboy.vhr.service.JobLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/basic/joblevel")
public class JobLevelController {
   @Autowired
    JobLevelService jobLevelService;

    @GetMapping("/")
    public List<JobLevel> getAllJobLevels(){
        return jobLevelService.getAllJobLevels();
    }

    //添加操作
    @PostMapping("/")
    public RespBean addJobLevel(@RequestBody JobLevel jobLevel){
       if (jobLevelService.addJobLevel(jobLevel)==1){
           return RespBean.ok("新的职称信息添加成功φ(゜▽゜*)♪");
       }
       return RespBean.error("新的职称信息添加失败(；′⌒`)");
    }

    //编辑操作
    @PutMapping("/")
    public RespBean updateJobLevelById(@RequestBody JobLevel jobLevel){
        if (jobLevelService.updateJobLevelById(jobLevel)==1){
            return RespBean.ok("您操作的信息编辑成功o(*￣▽￣*)ブ");
        }
        return RespBean.error("您操作的信息编辑失败(；′⌒`)");
    }

    //删除操作
    @DeleteMapping("/{id}")
    public RespBean deleteJobLevelById(@PathVariable Integer id){
        if(jobLevelService.deleteJobLevelById(id)==1){
            return RespBean.ok("您选择的该条数据删除成功(●ˇ∀ˇ●)");
        }
        return RespBean.error("您选择的该条数据删除失败(；′⌒`)");
    }

    //批量删除
    @DeleteMapping("/")
    public RespBean deletePositionByIds(Integer[] ids){
        if(jobLevelService.deleteJobLevelByIds(ids)==ids.length){
            return RespBean.ok("您正在进行的批量删除操作,操作成功φ(゜▽゜*)♪");
        }
        return RespBean.error("您正在进行的批量删除操作,操作失败,请稍后重试(；′⌒`)");
    }

}
