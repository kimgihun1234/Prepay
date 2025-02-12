package com.d111.PrePay.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
public class KaKaoUserInfo {
    private Long id;
    private String connected_at;
    private Properties properties;
    private KakaoAccount kakao_account;

    @Getter
    @NoArgsConstructor
    public static class Properties {
        private String nickname;
        private String profile_image;
        private String thumbnail_image;
    }

    @Getter
    @NoArgsConstructor
    public static class KakaoAccount {
        private boolean profile_nickname_needs_agreement;
        private boolean profile_image_needs_agreement;
        private Profile profile;
    }

    @Getter
    @NoArgsConstructor
    public static class Profile {
        private String nickname;
        private String thumbnail_image_url;
        private String profile_image_url;
        private boolean is_default_image;
        private boolean is_default_nickname;
    }

}
