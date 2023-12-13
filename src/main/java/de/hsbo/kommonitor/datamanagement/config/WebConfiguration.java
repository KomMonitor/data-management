package de.hsbo.kommonitor.datamanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
@Deprecated
public class WebConfiguration {

    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedHeaders("*")
                        .allowedMethods("*");
//                        .allowCredentials(true);
            }
        };
    }
}
