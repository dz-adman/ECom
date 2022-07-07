package com.ad.ecom.core.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.context.request.RequestContextListener;

@Configuration
@EnableWebSecurity
public class EcomSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final String[] AUTH_WHITELIST = {
            // ECom Application
            "/registration/**",
            // Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/actuator/**"
    };
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                // CookieCsrfTokenRepository : to make csrf token available as cookie
                .csrfTokenRepository(new CookieCsrfTokenRepository())
                .and()
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST)
                .permitAll()
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/user").hasRole("USER")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .successHandler(new EcomAuthSuccessHandler())
                .failureHandler(new EcomAuthFailureHandler())
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(new EcomLogoutSuccessHandler())
                .invalidateHttpSession(true)
                .deleteCookies("XSRF-TOKEN")
                .deleteCookies("JSESSIONID")
                .and().httpBasic();
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // default policy SessionCreationPolicy.IF_REQUIRED
                //.sessionFixation().migrateSession()
                .invalidSessionUrl("/login")
                .maximumSessions(1)
                .sessionRegistry(sessionRegistry())
                .expiredUrl("/login")
                .maxSessionsPreventsLogin(false);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        SessionRegistry sessionRegistry = new SessionRegistryImpl();
        return sessionRegistry;
    }

    // Register HttpSessionEventPublisher
    @Bean
    public static ServletListenerRegistrationBean httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
    }

    // RequestContextListener exposes state necessary to implement session scope
    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }
}
