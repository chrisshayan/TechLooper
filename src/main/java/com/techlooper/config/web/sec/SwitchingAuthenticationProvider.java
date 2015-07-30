package com.techlooper.config.web.sec;

import com.techlooper.model.SocialProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Map;

public class SwitchingAuthenticationProvider implements AuthenticationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwitchingAuthenticationProvider.class);

    private Map<SocialProvider, AuthenticationProvider> providers;

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            SocialProvider socialProvider = SocialProvider.valueOf(authentication.getCredentials().toString());
            if (socialProvider != null) {
                AuthenticationProvider delegateTo = providers.get(socialProvider);
                return delegateTo.authenticate(authentication);
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        return providers.get(SocialProvider.VIETNAMWORKS).authenticate(authentication);
    }

    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public Map<SocialProvider, AuthenticationProvider> getProviders() {
        return providers;
    }

    public void setProviders(Map<SocialProvider, AuthenticationProvider> providers) {
        this.providers = providers;
    }
}
