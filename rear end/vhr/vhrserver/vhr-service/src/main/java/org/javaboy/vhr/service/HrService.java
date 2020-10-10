package org.javaboy.vhr.service;

import org.javaboy.vhr.mapper.HrMapper;
import org.javaboy.vhr.mapper.Hr_RoleMapper;
import org.javaboy.vhr.model.Hr;
import org.javaboy.vhr.utils.HrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HrService implements UserDetailsService {
    //根据用户名会自动加载用户对象
    @Autowired
    HrMapper hrMapper;
    @Autowired
    Hr_RoleMapper hr_roleMapper ;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Hr hr= hrMapper.loadUserByUsername(username);
        if(username==null){
            throw new UsernameNotFoundException("该用户名不存在！");
        }
        hr.setRoles(hrMapper.getRolesById(hr.getId()));
        return hr;
    }

    //获取用户对象信息
    public List<Hr> getAllHrs(String keywords) {
        return hrMapper.getAllHrs(HrUtils.getCurrentHr().getId(), keywords);
    }

    public Integer updateHr(Hr hr) {
        return hrMapper.updateByPrimaryKeySelective(hr);
    }

    @Transactional
    public boolean updateHrRole(Integer hrid, Integer[] rids) {
        hr_roleMapper.deleteByHrid(hrid);
        return hr_roleMapper.addRole(hrid, rids) == rids.length;
    }

    public  Integer deleteHrById(Integer id) {
        return  hrMapper.deleteByPrimaryKey(id);
    }

    public List<Hr> getAllHrsExceptCurrentHr() {
        return hrMapper.getAllHrsExceptCurrentHr(HrUtils.getCurrentHr().getId());
    }

    //密码修改操作
    public boolean updateHrPasswd(String oldpass, String pass, Integer hrid) {
        Hr hr = hrMapper.selectByPrimaryKey(hrid);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(oldpass, hr.getPassword())) {
            //给前端传来的修改后的密码也进行加密
            String encodePass = encoder.encode(pass);

            Integer result = hrMapper.updatePasswd(hrid, encodePass);
            if (result == 1) {
                return true;
            }
        }
        return false;
    }

    //头像修改操作
    public Integer updateUserface(String url, Integer id) {
        return hrMapper.updateUserface(url, id);
    }
}
