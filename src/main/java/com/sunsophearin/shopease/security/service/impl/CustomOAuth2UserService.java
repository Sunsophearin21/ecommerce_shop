package com.sunsophearin.shopease.security.service.impl;

import com.sunsophearin.shopease.security.dto.Oauth2UserInfoDto;
import com.sunsophearin.shopease.security.entities.Role;
import com.sunsophearin.shopease.security.entities.RoleEnum;
import com.sunsophearin.shopease.security.entities.User;
import com.sunsophearin.shopease.security.oauth2.UserPrincipal;
import com.sunsophearin.shopease.security.repository.RoleRepository;
import com.sunsophearin.shopease.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        log.info("OAuth2 login with provider: {}", request.getClientRegistration().getRegistrationId());
        OAuth2User oAuth2User = super.loadUser(request);
        return processOAuth2User(request, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        try {
            Oauth2UserInfoDto userInfo = extractUserInfo(oAuth2User, oAuth2UserRequest);

            Optional<User> userOptional = userRepository.findByUsername(userInfo.getEmail());
            User user = userOptional
                    .map(existing -> updateUser(existing, userInfo))
                    .orElseGet(() -> registerUser(oAuth2UserRequest,userInfo));

            return new UserPrincipal(user, oAuth2User.getAttributes());

        } catch (Exception e) {
            log.error("OAuth2 user processing failed: {}", e.getMessage(), e);
            throw new OAuth2AuthenticationException("Error during OAuth2 user processing");
        }
    }

    private Oauth2UserInfoDto extractUserInfo(OAuth2User oAuth2User, OAuth2UserRequest request) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        if (email == null || email.isEmpty()) {
            throw new OAuth2AuthenticationException("Email not found from provider");
        }

        return Oauth2UserInfoDto.builder()
                .email(email)
                .name((String) attributes.getOrDefault("name", "Unknown"))
                .picture((String) attributes.getOrDefault("picture", ""))
                .provider(request.getClientRegistration().getRegistrationId())
                .build();
    }

    private User registerUser(OAuth2UserRequest oAuth2UserRequest,Oauth2UserInfoDto userInfoDto) {
        Role userRole = roleRepository.findByName(RoleEnum.USER)
                .orElseThrow(() -> new RuntimeException("USER role not found in database"));

        User newUser = new User();
        newUser.setProvider(oAuth2UserRequest.getClientRegistration().getRegistrationId());
        newUser.setName(userInfoDto.getName());
        newUser.setUsername(userInfoDto.getEmail());
        newUser.setPicture(userInfoDto.getPicture());
        newUser.setRoles(Set.of(userRole));

        log.info("Registering new user: {}", userInfoDto.getEmail());
        return userRepository.save(newUser);
    }

    private User updateUser(User user, Oauth2UserInfoDto userInfo) {
        user.setName(userInfo.getName());
//        user.setPicture(userInfo.getPicture());
        user.setProvider(userInfo.getProvider());

        log.info("Updating existing user: {}", userInfo.getEmail());
        return userRepository.save(user);
    }
}
