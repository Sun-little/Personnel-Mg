package org.javaboy.vhr.controller.System.basic;

import org.javaboy.vhr.model.Position;
import org.javaboy.vhr.model.RespBean;
import org.javaboy.vhr.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/basic/pos")
public class PositionController {
    @Autowired
    PositionService positionService;
    @GetMapping("/")
    public List<Position> getAllPositions(){
        return positionService.getAllPositions();
    }

    @PostMapping("/")
    //因前后端是以json传输数据, 所以要加上@RequestBody注解
    public RespBean addPosition(@RequestBody Position position){
        if(positionService.addPosition(position) == 1){
            return RespBean.ok("新的职位信息添加成功φ(゜▽゜*)♪");
        }
        return RespBean.error("新de职位信息添加失败了(；′⌒`)");
    }

    @PutMapping("/")
    //更新操作
    public RespBean updatePosition(@RequestBody Position position){
        if (positionService.updatePosition(position) == 1){
            return RespBean.ok("您好 更新数据操作执行成功o(*￣▽￣*)ブ");
        }
        return RespBean.error("抱歉 您刚刚的更新操作失败了(；′⌒`)");
    }

    @DeleteMapping("/{id}")
    //删除操作 加上 @PathVariable注解告诉请求中的id属性去哪里拿值
    public RespBean deletePositionById(@PathVariable Integer id){
        if(positionService.deletePositionById(id) == 1){
            return RespBean.ok("您选中的数据删除成功φ(゜▽゜*)♪");
        }
        return RespBean.error("抱歉 您选中的数据删除失败(；′⌒`)");
    }

    //批量删除
    @DeleteMapping("/")
    public RespBean deletePositionByIds(Integer[] ids){
        if(positionService.deletePositionByIds(ids)==ids.length){
            return RespBean.ok("您正在进行的批量删除操作,操作成功φ(゜▽゜*)♪");
        }
        return RespBean.error("您正在进行的批量删除操作,操作失败,请稍后重试(；′⌒`)");
    }
}
