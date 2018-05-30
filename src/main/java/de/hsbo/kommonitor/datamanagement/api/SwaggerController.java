package de.hsbo.kommonitor.datamanagement.api;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 *
 * @author dewall
 */
@Controller
public class SwaggerController {

    @RequestMapping(method = GET, path = "/api-docs", produces = "application/json")
    public @ResponseBody
    Resource apiDocs() {
        return new ClassPathResource("swagger.json");
    }

    @RequestMapping(method = GET, path = "/swagger-resources", produces = "application/json")
    public @ResponseBody
    Object resources() {
        return ImmutableList.of(ImmutableMap.of(
                "name", "default",
                "location", "/api-docs",
                "swaggerVersion", "2.0"
        ));
    }

    @RequestMapping(method = GET, path = "/swagger-resources/configuration/ui", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object uiConfig() {
        return ImmutableMap.builder()
                .put("jsonEditor", Boolean.FALSE)
                .build();
    }

    @RequestMapping(method = GET, path = "/swagger-resources/configuration/security", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object securityConfig() {
        return ImmutableList.of(ImmutableMap.of(
                "apiKeyVehicle", "header",
                "scopeSeparator", ",",
                "apiKeyName", "api_key"));
    }

}
