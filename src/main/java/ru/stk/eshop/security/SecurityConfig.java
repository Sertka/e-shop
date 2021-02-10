package ru.stk.eshop.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    public void authConfigure(AuthenticationManagerBuilder authManager,
                              UserAuthService userAuthService,
                              PasswordEncoder passwordEncoder) throws Exception {
        authManager.inMemoryAuthentication()
                .withUser("mem")
                .password(passwordEncoder.encode("pass"))
                .roles("ADMIN");

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userAuthService);
        provider.setPasswordEncoder(passwordEncoder);
        authManager.authenticationProvider(provider);
    }

//    @Configuration
//    @Order(1)
//    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
//
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http
//                    .antMatcher("/api/**")
//                    .authorizeRequests()
//                    .anyRequest()
//                    .hasAnyRole("ADMIN", "GUEST")
//                    .and()
//                    .httpBasic()
//                    .authenticationEntryPoint((req, resp, exception) -> {
//                        resp.setContentType("application/json");
//                        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                        resp.setCharacterEncoding("UTF-8");
//                        resp.getWriter().println("{ \"error\": \"" + exception.getMessage() + "\" }");
//                    })
//                    .and()
//                    .csrf().disable()
//                    .sessionManagement()
//                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        }
//    }

    @Configuration
    @Order(2)
    public static class UiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/*.css", "/*.js").anonymous()
                    //.antMatchers("/user/**").hasAnyRole("ADMIN")
                    .anyRequest().permitAll()
                    .and()
                    .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/authUser")
                    .and()
                    .logout()
                    .logoutSuccessUrl("/")
                    .permitAll();}
    }
}
