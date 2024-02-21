package com.example.matrixsystem.security.config;

import com.example.matrixsystem.beans.CommonDatabaseManager;
import com.example.matrixsystem.spring_data.exceptions.NoSuchUserInDB;
import com.example.matrixsystem.spring_data.entities.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private CommonDatabaseManager commonDatabaseManager;
    @Autowired
    public void setUserRepository(CommonDatabaseManager commonDatabaseManager) {
        this.commonDatabaseManager = commonDatabaseManager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/*/open-source/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/app", "/api").permitAll()
                .anyRequest().authenticated()).formLogin(login -> login
                .loginPage("/login")
                .failureUrl("/login?error=true")
                .permitAll());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
                Users user;
                try{
                    user = commonDatabaseManager.getUserByLogin(login);
                }catch (NoSuchUserInDB e){
                    throw new UsernameNotFoundException(e.getMessage());
                }

                GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().toString());
                List<GrantedAuthority> authorityList = new ArrayList<>();
                authorityList.add(authority);
                return new User(login, user.getPassword(), authorityList);
            }
        };
    }
}