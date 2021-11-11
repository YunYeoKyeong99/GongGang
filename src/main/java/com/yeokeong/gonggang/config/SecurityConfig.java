package com.yeokeong.gonggang.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeokeong.gonggang.common.Constant;
import com.yeokeong.gonggang.log.ApiLogFilter;
import com.yeokeong.gonggang.security.DefaultAccessDeniedHandler;
import com.yeokeong.gonggang.security.DefaultAuthenticationEntryPoint;
import com.yeokeong.gonggang.services.UserSignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@SuppressWarnings("unused")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserSignService userSignService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Environment env;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userSignService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        final String activeProfiles = String.join(",", env.getActiveProfiles());

        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .addFilterBefore(new ApiLogFilter(activeProfiles, objectMapper), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(Constant.PERMIT_ALL_PATHS).permitAll()
                .antMatchers(Constant.AUTHENTICATED_PATHS).authenticated()
                .antMatchers(Constant.ROLE_USER_PATHS).hasRole(Constant.ROLE_USER)
                .anyRequest().permitAll()
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling()
                .authenticationEntryPoint(new DefaultAuthenticationEntryPoint())
                .accessDeniedHandler(new DefaultAccessDeniedHandler())
                .and()
                .logout()
                .logoutUrl("/api/v1/users/signout")
                .deleteCookies("JSESSIONID");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    @SuppressWarnings("unused")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
