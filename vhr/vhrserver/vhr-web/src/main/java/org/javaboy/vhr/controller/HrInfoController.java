package org.javaboy.vhr.controller;


import org.javaboy.vhr.config.FastDFSUtils;
import org.javaboy.vhr.model.Hr;
import org.javaboy.vhr.model.RespBean;
import org.javaboy.vhr.service.HrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
public class HrInfoController {

    @Autowired
    HrService hrService;
    @Value("${fastdfs.nginx.host}")
    String nginxHost;

    //拿到个人信息
    @GetMapping("/hr/info")
    public Hr getCurrentHr(Authentication authentication) {
        return ((Hr) authentication.getPrincipal());
    }

    //个人信息编辑操作
    @PutMapping("/hr/info")
    public RespBean updateHr(@RequestBody Hr hr, Authentication authentication) {
        if (hrService.updateHr(hr) == 1) {
            //用于个人信息更新后,前端拿到用户最新的信息,也就是执行完更新操作后的数据
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(hr, authentication.getCredentials(), authentication.getAuthorities()));
            return RespBean.ok("该个人信息编辑成功😄");
        }
        return RespBean.error("该个人信息编辑失败了,请稍后重试-😔");
    }

    //密码修改操作
    @PutMapping("/hr/pass")
    public RespBean updateHrPasswd(@RequestBody Map<String, Object> info) {
        String oldpass = (String) info.get("oldPass");
        String pass = (String) info.get("pass");
        Integer hrid = (Integer) info.get("hrid");
        if (hrService.updateHrPasswd(oldpass, pass, hrid)) {
            return RespBean.ok("您输入の密码已完成修改操作!😄");
        }
        return RespBean.error("您输入の密码在执行修改操作时它失败了,请稍后重试😔!");
    }

    //文件上传控制器
    @PostMapping("/hr/userface")
    public RespBean updateHrUserface(MultipartFile file, Integer id, Authentication authentication) {
        String fileId = FastDFSUtils.upload(file);
        //新文件の访问路径
        String url = nginxHost + fileId;
        //头像更新操作
        if (hrService.updateUserface(url, id) == 1) {
            //更新头像缓存
            Hr hr = (Hr) authentication.getPrincipal();
            hr.setUserface(url);
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(hr, authentication.getCredentials(), authentication.getAuthorities()));
            return RespBean.ok("该用户头像数据更新成功!😄", url);
        }
        return RespBean.error("该用户头像数据更新失败了,请稍后重试!😔");
    }

}
