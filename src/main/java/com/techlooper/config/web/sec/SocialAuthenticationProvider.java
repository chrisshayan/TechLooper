package com.techlooper.config.web.sec;

import com.techlooper.entity.VnwUserProfile;
import com.techlooper.entity.vnw.RoleName;
import com.techlooper.model.SocialConfig;
import com.techlooper.model.SocialProvider;
import com.techlooper.repository.JsonConfigRepository;
import com.techlooper.service.SocialService;
import com.techlooper.service.VietnamWorksUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.security.SocialAuthenticationToken;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * Created by phuonghqh on 7/28/15.
 */
public class SocialAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private ApplicationContext applicationContext;

    private static final Logger LOGGER = LoggerFactory.getLogger(SocialAuthenticationProvider.class);

    @Resource
    private JsonConfigRepository jsonConfigRepository;

    @Resource
    private VietnamWorksUserService vietnamWorksUserService;

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken socialAuthenticationToken = (UsernamePasswordAuthenticationToken) authentication;
        SocialProvider socialProvider = SocialProvider.valueOf(socialAuthenticationToken.getCredentials().toString());
        String code = authentication.getPrincipal().toString();
        SocialService socialService = (SocialService) applicationContext.getBean(socialProvider.name() + "Service");

        SocialConfig socialConfig = jsonConfigRepository.getSocialConfig().stream()
                .filter(config -> socialProvider == config.getProvider()).findFirst().get();

        UserProfile userProfile = null;
        try {
            userProfile = socialService.getUserProfile(code, socialConfig);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        if (userProfile != null) {
            try {
                VnwUserProfile vnwUserProfile = VnwUserProfile.VnwUserProfileBuilder.vnwUserProfile()
                        .withEmail(userProfile.getEmail())
                        .withFirstname(userProfile.getFirstName())
                        .withLastname(userProfile.getLastName()).build();
                vietnamWorksUserService.register(vnwUserProfile);
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
            return new UsernamePasswordAuthenticationToken(userProfile.getName(), null,
                    Arrays.asList(new SimpleGrantedAuthority(RoleName.JOB_SEEKER.name())));
        }

        return null;
    }

    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
