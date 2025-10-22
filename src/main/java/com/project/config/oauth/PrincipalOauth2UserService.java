package com.project.config.oauth;

import com.project.config.auth.PrincipalDetails;
import com.project.model.User;
import com.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    // 해당 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println ("userRequest : " + userRequest.getClientRegistration ()); //registrationId로 어떤 Oauth로 로그인 했는지
        System.out.println ("getAccessToken = " + userRequest.getAccessToken ());

        OAuth2User oAuth2User = super.loadUser (userRequest);
        System.out.println ("getAttributes = " + oAuth2User.getAttributes ());

        String provider = userRequest.getClientRegistration ().getClientId ();
        String providerId = oAuth2User.getAttribute ("sub");
        String username = provider+"_"+providerId;
        String email = oAuth2User.getAttribute ("email");
        String password = bCryptPasswordEncoder.encode ("겟인데어");
        String role = "ROLE_USER";

        User userEntity = userRepository.findByUsername (username);

        if (userEntity == null) {
            userEntity = User.builder ()
                    .username (username)
                    .password (password)
                    .email (email)
                    .role (role)
                    .provider (provider)
                    .providerId (providerId)
                    .build ();
            userRepository.save (userEntity);
        }

        return new PrincipalDetails (userEntity,oAuth2User.getAttributes ());
    }
}
