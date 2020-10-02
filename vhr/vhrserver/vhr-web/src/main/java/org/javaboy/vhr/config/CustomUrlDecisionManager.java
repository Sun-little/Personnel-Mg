package org.javaboy.vhr.config;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;


//该方法用于判断用户是否具备 MyFilter 分析出来的用户角色(具备的话就给过 反之不给过)
@Component
public class CustomUrlDecisionManager implements AccessDecisionManager {

    /**
     *
     * @param authentication: 保存着用户登录后的所有信息
     * @param object:请求对象
     * @param configAttributes： 是MyFilter (Collection)方法的返回值
     * @throws AccessDeniedException
     * @throws InsufficientAuthenticationException
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        //获取 MyFilter中分析出来的用户角色信息
        for (ConfigAttribute configAttribute : configAttributes) {
            //需要的角色
            String needRole = configAttribute.getAttribute();
            if("ROLE_LOGIN".equals(needRole)){
                //如果当前用户为匿名登录直接抛异常
                if(authentication instanceof AnonymousAuthenticationToken){
                    throw new AccessDeniedException("对不起 您尚未进行登录,请您完成登录后进行访问(ThankYou)!");
                }else {
                    return;
                }
            }
            //获取当前登录用户的角色
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                //如果这两个值相等,就意味着登录用户用户所需要的用户角色
                if(authority.getAuthority().equals(needRole)){
                    return;
                }
            }

        }
        throw new AccessDeniedException("抱歉,您的账户权限不足,无法进行其他操作(；′⌒`)!");

    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
