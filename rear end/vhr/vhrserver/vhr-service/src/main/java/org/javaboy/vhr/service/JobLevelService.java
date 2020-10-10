package org.javaboy.vhr.service;

import org.javaboy.vhr.mapper.JobLevelMapper;
import org.javaboy.vhr.model.JobLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class JobLevelService {
    @Autowired
    JobLevelMapper jobLevelMapper;

    public List<JobLevel> getAllJobLevels() {

        return jobLevelMapper.getAllJobLevels();

    }

    //添加操作
    public Integer addJobLevel(JobLevel jobLevel) {
        jobLevel.setCreateDate(new Date());
        jobLevel.setEnabled(true);
        return jobLevelMapper.insertSelective(jobLevel);
    }

    //编辑操作
    public Integer updateJobLevelById(JobLevel jobLevel) {
        return jobLevelMapper.updateByPrimaryKeySelective(jobLevel);
    }

    //删除操作
    public Integer deleteJobLevelById(Integer id) {
        return jobLevelMapper.deleteByPrimaryKey(id);
    }

    public Integer deleteJobLevelByIds(Integer[] ids) {
        return jobLevelMapper.deleteJobLevelByIds(ids);
    }

}