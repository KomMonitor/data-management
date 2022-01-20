package de.hsbo.kommonitor.datamanagement.config;

//import com.sun.org.apache.regexp.internal.RE;
import org.apache.catalina.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * OAuth2 enabled configuration or Swagger-UI
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
@Configuration
@EnableSwagger2
public class SwaggerSecurityConfig {

    @Value("${swagger.keycloak.auth-server-url}")
    private String AUTH_SERVER;

    @Value("${keycloak.realm}")
    private String REALM;

    @Value("${kommonitor.swagger-ui.security.client-id}")
    private String CLIENT_ID;

    @Value("${kommonitor.swagger-ui.security.secret}")
    private String SECRET;

    private static final String TITLE = "KomMonitor Data Access API";
    private static final String DESCRIPTION = "erster Entwurf einer Datenzugriffs-API, die den Zugriff auf die KomMonitor-Datenhaltungsschicht kapselt.";
    private static final String LICENSE = "Apache 2.0";
    private static final String LICENSE_URL = "http://www.apache.org/licenses/LICENSE-2.0.html";
    private static final String VERSION = "0.0.1";
    private static final String EMAIL = "christian.danowski-buhren@hs-bochum.de";
    private static final String GROUP_NAME = "kommonitor-data-access";
    private static final String OAUTH_NAME = "kommonitor-data-access_oauth";

    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(TITLE)
                .description(DESCRIPTION)
                .license(LICENSE)
                .licenseUrl(LICENSE_URL)
                .termsOfServiceUrl("")
                .version(VERSION)
                .contact(new Contact("", "", EMAIL))
                .build();
    }

    @Bean
    public Docket customImplementation(ServletContext servletContext, @Value("${kommonitor.datamanagement-api.swagger-ui.base-path:}") String basePath) {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(GROUP_NAME)
                .select()
                .apis(RequestHandlerSelectors.basePackage("de.hsbo.kommonitor.datamanagement.api"))
                .build()
                .pathProvider(new BasePathAwareRelativePathProvider(servletContext, basePath))
                .apiInfo(apiInfo())
                .securitySchemes(buildSecurityScheme())
                .securityContexts(Arrays.asList(securityContext()));
    }

    @Bean
    public SecurityConfiguration securityConfiguration() {
        return SecurityConfigurationBuilder.builder()
                .realm(REALM)
                .clientId(CLIENT_ID)
                .clientSecret(SECRET)
                .appName(GROUP_NAME)
                .scopeSeparator(" ")
                .build();
    }

    private List<? extends SecurityScheme> buildSecurityScheme() {
        List<SecurityScheme> lst = new ArrayList<>();
        GrantType grantType =
                new AuthorizationCodeGrantBuilder()
                        .tokenEndpoint(new TokenEndpoint(String.format("%s/realms/%s/protocol/openid-connect/token", AUTH_SERVER, REALM), GROUP_NAME))
                        .tokenRequestEndpoint(
                                new TokenRequestEndpoint(String.format("%s/realms/%s/protocol/openid-connect/auth", AUTH_SERVER, REALM), CLIENT_ID, SECRET))
                        .build();

        SecurityScheme oauth =
                new OAuthBuilder()
                        .name(OAUTH_NAME)
                        .grantTypes(Arrays.asList(grantType))
                        .scopes(Arrays.asList(scopes()))
                        .build();
        lst.add(oauth);
        return lst;
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(Arrays.asList(new SecurityReference(OAUTH_NAME, scopes())))
                .forPaths(PathSelectors.any())
                .build();
    }

    private AuthorizationScope[] scopes() {
        AuthorizationScope[] scopes = {};
        return scopes;
    }
}
