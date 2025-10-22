package com.project.config;

import com.project.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록됨.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/**").authenticated()
                        .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/loginForm")
                        .loginProcessingUrl ("/login") // /login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행
                        .defaultSuccessUrl ("/")
                )
                .oauth2Login(oauth2 -> oauth2
                                // 커스텀 로그인 페이지
                                .loginPage("/loginForm")

                                // 사용자 정보 엔드포인트에 커스텀 OAuth2UserService 연결
                                .userInfoEndpoint(userInfo -> userInfo
                                        .userService(principalOauth2UserService)
                                )

                         .defaultSuccessUrl("/", true) // 위 successHandler 대신 단순 리다이렉트만 할 경우 이 줄을 사용

                        // 로그인 실패 시 이동 페이지 필요하면 주석 해제
                        // .failureUrl("/loginForm?error")
                );

        return http.build();
    }
}
