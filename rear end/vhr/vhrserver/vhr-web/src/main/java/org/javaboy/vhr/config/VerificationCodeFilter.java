package org.javaboy.vhr.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.javaboy.vhr.model.RespBean;
import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//登录验证码校验
@Component
public class VerificationCodeFilter extends GenericFilter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        if ("POST".equals(req.getMethod()) && "/doLogin".equals(req.getServletPath())) {
            //登录请求处理
            String code = req.getParameter("code");//获取前端输入的验证码
            //图片中生成的验证码
            String verify_code = (String) req.getSession().getAttribute("verify_code");
            //.toLowerCase():忽略大小写
            if (code == null || "".equals(code) || !verify_code.toLowerCase().equals(code.toLowerCase())) {
                //输入的验证码有问题处理
                resp.setContentType("application/json;charset=utf-8");
                PrintWriter out = resp.getWriter();
                out.write(new ObjectMapper().writeValueAsString(RespBean.error("对不起,您输入的验证码有误🤭")));
                out.flush();
                out.close();
                return;
            } else {
                //否则过滤器继续执行
                filterChain.doFilter(req, resp);
            }
        } else {
                filterChain.doFilter(req, resp);
        }


    }
}
