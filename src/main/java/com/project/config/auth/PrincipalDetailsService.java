package com.project.config.auth;

import com.project.model.User;
import com.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // 해당 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User userEntity = userRepository.findByUsername (username);
        System.out.println ("호출됨" + userEntity);
        if(userEntity != null) {
            return new PrincipalDetails (userEntity);
        }
        return null;
    }
}
