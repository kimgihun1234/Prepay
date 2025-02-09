package com.d111.PrePay.security.service;

import com.d111.PrePay.model.User;
import com.d111.PrePay.repository.UserRepository;
import com.d111.PrePay.security.dto.*;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {


    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        Oauth2Response oauth2Response = null;

        if (registrationId.equals("naver")) {

            oauth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("google")) {

            oauth2Response = new GoogleResponse(oAuth2User.getAttributes());

        } else {
            return null;
        }


        User exitsData = userRepository.findUserByEmail(oauth2Response.getEmail());

        if (exitsData == null) {
            User user = new User();
            user.setEmail(oauth2Response.getEmail());
            user.setNickname(oAuth2User.getName());

            User savedUser = userRepository.save(user);

            UserDTO userDto = new UserDTO();
            userDto.setEmail(oauth2Response.getEmail());
            userDto.setName(oauth2Response.getName());
            userDto.setUserId(savedUser.getId());

            return new CustomOauth2User(userDto);

        }

        else {
            exitsData.setEmail(oauth2Response.getEmail());
            exitsData.setNickname(oauth2Response.getName());

            userRepository.save(exitsData);

            UserDTO userDto = new UserDTO();
            userDto.setEmail(exitsData.getEmail());
            userDto.setName(exitsData.getNickname());
            userDto.setUserId(exitsData.getId());
            return new CustomOauth2User(userDto);

        }
    }
}
