package org.example.todotravel.global.oauth2.userinfo;


import java.util.Map;
import org.example.todotravel.domain.user.entity.Gender;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getNickname() {
        return (String) attributes.get("name");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }
    @Override
    public Gender getGender() {
        return (Gender) attributes.get("gender");
    }
}