package org.javaboy.vhr.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.javaboy.vhr.model.Hr;
import org.javaboy.vhr.model.RespBean;
import org.javaboy.vhr.service.HrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class  SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    HrService hrService;
    @Autowired
    CustomFilterInvocationSecurityMetadataSource customFilterInvocationSecurityMetadataSource;
    @Autowired
    CustomUrlDecisionManager customUrlDecisionManager;
    @Autowired
    VerificationCodeFilter verificationCodeFilter;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //用户详细信息Service加载
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(hrService);
    }

    //没有登录,访问登录即可访问的接口,跳转到登录页;将登录页放行,直接过滤
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/login", "/css/**", "/js/**", "/index.html", "/img/**", "/fonts/**", "/favicon.ico", "/verifyCode");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //将验证码过滤器加在UsernamePasswordAuthenticationFilter(即在用户名密码过滤器前面执行)
        http.addFilterBefore(verificationCodeFilter, UsernamePasswordAuthenticationFilter.class);
        //授权处理
        http.authorizeRequests()
                /*所有接口都必须登录后才能访问*/
                //.anyRequest().authenticated()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        //加载权限配置策略
                        object.setAccessDecisionManager(customUrlDecisionManager);
                        object.setSecurityMetadataSource(customFilterInvocationSecurityMetadataSource);
                        return object;
                    }
                })
                .and()
                .formLogin()
                .usernameParameter("username")
                .passwordParameter("password")
                .loginProcessingUrl("/doLogin")
                .loginPage("/login")
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
                        /*登陆成功操作 登录成功后的用户完整的信息保存在 authentication参数中*/
                        resp.setContentType("application/json;charset=utf-8");
                        PrintWriter out = resp.getWriter();
                        //拿到用户登陆成功的对象
                        Hr hr = (Hr) authentication.getPrincipal();
                        //登录成功之后 将返回到前端的密码设为null
                        hr.setPassword(null);
                        RespBean ok = RespBean.ok("恭喜😄, 您已成功登录本系统φ(゜▽゜*)♪", hr);
                        //将拿到的用户对象转成字符串
                        String s = new ObjectMapper().writeValueAsString(ok);
                        //写出去
                        out.write(s);
                        out.flush();
                        out.close();


                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse resp, AuthenticationException exception) throws IOException, ServletException {
                        /*登录失败操作*/
                        resp.setContentType("application/json;charset=utf-8");
                        PrintWriter out = resp.getWriter();
                        RespBean respBean = RespBean.error("抱歉 系统登录失败(；′⌒`)");
                        if (exception instanceof LockedException) {
                            respBean.setMsg("抱歉 该账户已被锁定 请联系管理员进行处理!");

                        } else if (exception instanceof CredentialsExpiredException) {
                            respBean.setMsg("抱歉 该账户密码已过期 请联系管理员进行处理!");

                        } else if (exception instanceof AccountExpiredException) {
                            respBean.setMsg("抱歉 您使用的账户已过期 请联系管理员进行处理!");

                        } else if (exception instanceof DisabledException) {
                            respBean.setMsg("抱歉 您使用的账户已被禁用 请联系管理员进行处理!");

                        } else if (exception instanceof BadCredentialsException) {
                            respBean.setMsg("抱歉 您输入的账户名或密码有误,请核对后重新输入!");
                        }
                        out.write(new ObjectMapper().writeValueAsString(respBean));
                        out.flush();
                        out.close();


                    }
                })
                .permitAll()
                .and()
                .logout()
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
                        /*注销登录操作*/
                        resp.setContentType("application/json;charset=utf-8");
                        PrintWriter out = resp.getWriter();
                        out.write(new ObjectMapper().writeValueAsString(RespBean.ok("您的账户注销成功(●'◡'●)")));
                        out.flush();
                        out.close();
                    }
                })
                .permitAll()
                .and()
                .csrf().disable().exceptionHandling()

                //没有认证访问接口,不要去重定向,直接处理结果
                .authenticationEntryPoint((req, resp, authException) -> {
                    //请求失败的二次处理
                    resp.setContentType("application/json;charset=utf-8");
                    resp.setStatus(401);
                    PrintWriter out = resp.getWriter();
                    RespBean respBean = RespBean.error("抱歉,您尚未进行登录,系统访问失败(；′⌒`)");
                    if (authException instanceof InsufficientAuthenticationException) {
                        respBean.setMsg("抱歉 您尚未进行登录 请完成登录后在进行操作!");
                    }
                    out.write(new ObjectMapper().writeValueAsString(respBean));
                    out.flush();
                    out.close();

                });


    }
}
