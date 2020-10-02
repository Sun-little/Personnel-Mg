package org.javaboy.vhr.service;

import org.javaboy.vhr.mapper.SalaryMapper;
import org.javaboy.vhr.model.RespBean;
import org.javaboy.vhr.model.Salary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SalaryService {
    @Autowired
    SalaryMapper salaryMapper;
    public List<Salary> getAllSaleries() {
        return salaryMapper.getAllSaleries();
    }


    public Integer addSalary(Salary salary) {
        salary.setCreateDate(new Date());
        return salaryMapper.insertSelective(salary);
    }

    public Integer deleteSal(Integer id) {
        return salaryMapper.deleteByPrimaryKey(id);
    }

    public Integer upDateSalById(Salary salary) {
        return salaryMapper.updateByPrimaryKeySelective(salary);
    }
}
