package me.bmordue.lgm.security;

import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface IAuthenticationFacade {
    Authentication getAuthentication();
    Optional<String> getCurrentUserLogin();
}
