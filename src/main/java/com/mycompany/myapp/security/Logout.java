package com.mycompany.myapp.security;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.NotBlank;

@Introspected
public class Logout {

    @NonNull
    @NotBlank
    private String logoutUrl;

    public Logout() {}

    @NonNull
    public String getLogoutUrl() {
        return logoutUrl;
    }

    public void setLogoutUrl(@NonNull String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }
}
