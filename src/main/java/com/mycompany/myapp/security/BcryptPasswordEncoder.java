package com.mycompany.myapp.security;

import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;
import org.mindrot.jbcrypt.BCrypt;

@Singleton
public class BcryptPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(@NotBlank String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt(16));
    }

    @Override
    public boolean matches(@NotBlank String rawPassword, @NotBlank String encodedPassword) {
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }
}
