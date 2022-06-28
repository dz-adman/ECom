package com.ad.ecom.core.security;

import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

public class CookieCsrfTokenRepositoryBuilder {

    private final CookieCsrfTokenRepository cookieCsrfTokenRepository = new CookieCsrfTokenRepository();

    public CookieCsrfTokenRepositoryBuilder setParameterName(String parameterName) {
        this.cookieCsrfTokenRepository.setParameterName(parameterName);
        return this;
    }

    public CookieCsrfTokenRepositoryBuilder setHeaderName(String headerName) {
        this.cookieCsrfTokenRepository.setHeaderName(headerName);
        return this;
    }

    public CookieCsrfTokenRepositoryBuilder setCookieName(String cookieName) {
        this.cookieCsrfTokenRepository.setCookieName(cookieName);
        return this;
    }

    public CookieCsrfTokenRepositoryBuilder setCookieHttpOnly(boolean cookieHttpOnly) {
        this.cookieCsrfTokenRepository.setCookieHttpOnly(cookieHttpOnly);
        return this;
    }

    public CookieCsrfTokenRepositoryBuilder setCookiePath(String cookiePath) {
        this.cookieCsrfTokenRepository.setCookiePath(cookiePath);
        return this;
    }

    public CookieCsrfTokenRepositoryBuilder setCookieDomain(String cookieDomain) {
        this.cookieCsrfTokenRepository.setCookieDomain(cookieDomain);
        return this;
    }

    public CookieCsrfTokenRepositoryBuilder setSecure(Boolean secure) {
        this.cookieCsrfTokenRepository.setSecure(secure);
        return this;
    }

    public CookieCsrfTokenRepositoryBuilder setCookieMaxAge(int seconds) {
        this.cookieCsrfTokenRepository.setCookieMaxAge(seconds);
        return this;
    }

    public CookieCsrfTokenRepository build() {
        return this.cookieCsrfTokenRepository;
    }
}
