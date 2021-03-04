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

/**
 * Security configuration
 */
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Service role set-up
     */
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

    /**
     * Role settings
     */
    @Configuration
    public static class UiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/*.css", "/*.js").anonymous()
                    .antMatchers("/user/**").hasAnyRole("ADMIN")
                    .antMatchers("/product/**").hasAnyRole("ADMIN", "MANAGER")
                    .antMatchers("/order/admin/**").hasAnyRole("ADMIN", "MANAGER")
                    .antMatchers("/order/edit/**").hasAnyRole("ADMIN", "MANAGER")
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