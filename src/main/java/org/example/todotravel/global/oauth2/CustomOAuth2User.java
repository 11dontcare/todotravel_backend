package org.example.todotravel.global.oauth2;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.todotravel.domain.user.entity.Gender;
import org.example.todotravel.domain.user.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

/**
 * DefaultOAuth2User를 상속하고, email과 role 필드를 추가로 가진다.
 */
@Getter
public class CustomOAuth2User extends DefaultOAuth2User {


// email과 role 따로 추가, 왜냐하면 client에서 resource server가 제공하지 않는 정보가 필요할 경우 확인할 수 있는 방법이 필요하기에.
    // 어떤 유저가 oauth로 로그인 한 상황인지 서버에서는 알 수 없음

    private String email;

    // 추가정보를 입력했는지, oauth 처음 로그인지 판단
    // 추가 정보 입력 회원가입 시 user로 업데이트
    // guest로 처음 로그인 시에 successhandler에서 추가 정보 입력하는 url로 리다이렉트
    // 이후 이메일로 토큰 발급 처리
    private Role role;

    /**
     * Constructs a {@code DefaultOAuth2User} using the provided parameters.
     *
     * @param authorities      the authorities granted to the user
     * @param attributes       the attributes about the user
     * @param nameAttributeKey the key used to access the user's &quot;name&quot; from
     *                         {@link #getAttributes()}
     */

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes, String nameAttributeKey,
                            String email, Role role) {
        super(authorities, attributes, nameAttributeKey);
        this.email = email;
        this.role = role;
    }
}
