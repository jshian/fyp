package com.dl2.fyp.config;

import com.dl2.fyp.domain.JwtAuthenticationEntryPoint;
import com.dl2.fyp.filter.JwtAuthenticationTokenFilter;
import com.dl2.fyp.service.impl.JwtUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private JwtUserDetailsServiceImpl jwtUserDetailsService;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.route.authentication.path}")
    private String authenticationPath;

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(this.jwtUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // we don't need CSRF because our token is invulnerable
            .csrf().disable()
            .cors().and()
            .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
            // don't create session
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests()
                .antMatchers("/data/**").permitAll()
                .antMatchers("/image/**").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/plugins/**").permitAll()
            .anyRequest().authenticated();
        http.headers().cacheControl();
        http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // AuthenticationTokenFilter will ignore the below path
        web
            .ignoring()
            .antMatchers(
                    HttpMethod.POST,
                    authenticationPath+"/**"
            )

            // allow anonymous resource requests
            .and()
            .ignoring()
            .antMatchers(
                    HttpMethod.GET,
                    "/",
                    "/*.html",
                    "/favicon.ico",
                    "/**/favicon.ico",
                    "/**/*.html",
                    "/**/*.css",
                    "/**/*.js"
            );
    }
}
