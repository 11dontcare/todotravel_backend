package org.example.todotravel.global.oauth2.userinfo;

import java.util.Map;
import org.example.todotravel.domain.user.entity.Gender;

public abstract class OAuth2UserInfo {

    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getId(); //소셜 식별 값 : 구글 - "sub", 카카오 - "id", 네이버 - "id"

    public abstract String getNickname();

    public abstract String getName();

    public abstract String getEmail();

    public abstract Gender getGender();
}