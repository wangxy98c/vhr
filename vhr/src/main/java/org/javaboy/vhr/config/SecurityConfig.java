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
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    HrService hrService;
    @Autowired
    CustomFilterInvocationSecurityMetadataSource customFilterInvocationSecurityMetadataSource;
    @Autowired
    CustomUrlDecisionManager customUrlDecisionManager;
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(hrService);
    }

    //配置前后端不干扰，即后端只范围成功或失败，不作跳转
    //整个security配置已过时，但仍可用。待更新升级
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("--SecurityConfig");
        http.authorizeRequests()
                //.anyRequest().authenticated()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    //注意！！上面那个不是authorizeHttpRequests（这个是更新的方式）。否则此方法失效
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        System.out.println("--配置Custom");
                        object.setSecurityMetadataSource(customFilterInvocationSecurityMetadataSource);
                        object.setAccessDecisionManager(customUrlDecisionManager);

                        return object;
                    }
                })//用于对请求地址的权限(角色)满足性判断
                .and().formLogin().usernameParameter("username").passwordParameter("password")
                .loginProcessingUrl("/doLogin").loginPage("/login")
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        response.setContentType("application/json;charset=utf-8");
                        PrintWriter out= response.getWriter();
                        //response是有基于流的概念，你从response拿到一个流，然后输出信息
                        Hr hr= (Hr)authentication.getPrincipal();
                        hr.setPassword(null);
                        //为了不把加密后的密码返回前端，或者在Hr类中GetPassword方法上加@JsonIgnore
                        RespBean ok=RespBean.ok("登陆成功!",hr);
                        System.out.println("登录成功!");
                        String s = new ObjectMapper().writeValueAsString(ok);
                        out.write(s);
                        out.flush();
                        out.close();
                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                        response.setContentType("application/json;charset=utf-8");
                        PrintWriter out= response.getWriter();
                        RespBean respBean=RespBean.error("登陆失败!");
                        if(exception instanceof LockedException){//失败类型可以从findusage中看同级的一场
                            respBean.setMsg("账户被锁定");
                        } else if (exception instanceof CredentialsExpiredException) {
                            respBean.setMsg("密码过期");
                        } else if (exception instanceof AccountExpiredException) {
                            respBean.setMsg("账户过期");
                        } else if (exception instanceof DisabledException) {
                            respBean.setMsg("账户被禁用");
                        } else if (exception instanceof BadCredentialsException) {
                            respBean.setMsg("用户名或密码输入错误");
                        }
                        System.out.println("总之，登录失败了！");
                        String s = new ObjectMapper().writeValueAsString(respBean);
                        out.write(s);
                        out.flush();
                        out.close();
                    }
                }).permitAll().and()
                        .logout().logoutUrl("/logout").logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        response.setContentType("application/json;charset=utf-8");
                        PrintWriter out = response.getWriter();
                        out.write(new ObjectMapper().writeValueAsString( RespBean.ok("注销成功")));
                        out.flush();
                        out.close();
                    }
                })
                .permitAll().and().csrf().disable()
                ////默认配置是重定向，此处返回错误信息，同登陆失败类似的处理
                .exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

                        response.setContentType("application/json;charset=utf-8");
                        response.setStatus(401);
                        PrintWriter out= response.getWriter();
                        RespBean respBean=RespBean.error("访问失败!");
                        if(exception instanceof InsufficientAuthenticationException){//失败类型可以从findusage中看同级的异常
                            respBean.setMsg("尚未登陆，请登录后再访问-ht");
                        }
                        String s = new ObjectMapper().writeValueAsString(respBean);
                        out.write(s);
                        out.flush();
                        out.close();
                    }//用于设置请求失败后是重定向还是返回结果（解决未登陆产生的cros问题）
                });
    }
}
