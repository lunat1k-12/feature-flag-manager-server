package com.ech.ff.featureflagmanager.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.oauth2.jwt.Jwt;

@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class CognitoUser {

    private String id;
    private String username;
    private String email;

    public static CognitoUser fromJwt(Jwt jwt) {
        return new CognitoUser(jwt.getSubject(), jwt.getClaimAsString("cognito:username"), jwt.getClaimAsString("email"));
    }
}
