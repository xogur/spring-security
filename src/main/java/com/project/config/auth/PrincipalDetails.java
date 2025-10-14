package com.project.config.auth;


//시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행
// 로그인 진행이 완료가 되면 시큐리티 session을 만들어 줌 (Security ContentHolder)
// 오브젝트 => Authentication타입 객체

import com.project.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PrincipalDetails implements UserDetails {

    private User user;

    public PrincipalDetails(User user) {
        this.user = user;
    }

    //유저의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println ("호출됨");
        Collection<GrantedAuthority> collet = new ArrayList<> ();
        collet.add (new GrantedAuthority () {
            @Override
            public String getAuthority() {
                return user.getRole ();
            }
        });
        return collet;
    }

    @Override
    public String getPassword() {
        return user.getPassword ();
    }

    @Override
    public String getUsername() {
        return user.getUsername ();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        //휴먼계정 같은 로직
        return true;
    }
}
