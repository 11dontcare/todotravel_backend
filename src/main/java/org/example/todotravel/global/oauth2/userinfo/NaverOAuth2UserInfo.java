package org.example.todotravel.global.oauth2.userinfo;

import java.util.Map;
import org.example.todotravel.domain.user.entity.Gender;

public class NaverOAuth2UserInfo extends OAuth2UserInfo {

    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        if (response == null) {
            return null;
        }
        return (String) response.get("id");
    }

    @Override
    public String getNickname() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        if (response == null) {
            return null;
        }

        return (String) response.get("nickname");
    }

@Override
    public String getName() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        if (response == null) {
            return null;
        }

        return (String) response.get("name");
    }

    @Override
    public String getEmail() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        if (response == null) {
            return null;
        }

        return (String) response.get("email");
    }


    public Gender getGender() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        if (response == null) {
            return null;
        }
        String gender = (String) response.get("gender");
        if (gender == null) {
            return null;
        }
        return gender.equalsIgnoreCase("F") ? Gender.WOMAN : Gender.MAN;
    }


//    @Override
//    public String getImageUrl() {
//        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
//
//        if (response == null) {
//            return null;
//        }
//
//        return (String) response.get("profile_image");
//    }
}

