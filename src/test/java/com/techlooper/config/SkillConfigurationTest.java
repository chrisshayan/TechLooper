package com.techlooper.config;

import com.techlooper.service.SuggestionService;
import com.techlooper.service.impl.SuggestionServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration

@PropertySources({@PropertySource("classpath:techlooper.properties")})
public class SkillConfigurationTest {

    @Bean
    public SuggestionService skillSuggestionService() {
        return new SuggestionServiceImpl();
    }

}
