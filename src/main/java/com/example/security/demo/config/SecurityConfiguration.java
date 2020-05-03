package com.example.security.demo.config;

//import com.example.security.demo.filter.ImageValidateCodeFilter;
import com.example.security.demo.filter.MyAuthenticationFailureHandler;
import com.example.security.demo.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        ImageValidateCodeFilter imageValidateCodeFilter = new ImageValidateCodeFilter();
//        imageValidateCodeFilter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);

        http.csrf().disable()
                // 配置需要认证的请求
                .authorizeRequests()
                .antMatchers("/login", "/code/image").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                // 登录表单相关配置
//                .addFilterBefore(imageValidateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(myAuthenticationSuccessHandler)
                .failureUrl("/error")
                .permitAll()
                .and()
                .rememberMe()
                .userDetailsService(myUserDetailsService)
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(60 * 60 * 60 * 30)
                .and()
                // 登出相关配置
                .logout()
                .permitAll();

    }

    private PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        System.out.println(tokenRepository.getDataSource());
        return tokenRepository;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/static/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

