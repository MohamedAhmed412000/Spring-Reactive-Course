package com.project.webflux.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthorizationTypesEnum {
    STANDARD("STANDARD", "secret123"),
    PREMIUM("PREMIUM", "secret456"),;

    private final String code;
    private final String value;

    public static AuthorizationTypesEnum fromValue(String value) {
        if (value == null) return STANDARD;
        for (AuthorizationTypesEnum authorizationTypesEnum : AuthorizationTypesEnum.values()) {
            if (authorizationTypesEnum.getValue().equals(value)) {
                return authorizationTypesEnum;
            }
        }
        return null;
    }

}
