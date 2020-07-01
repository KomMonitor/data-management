package de.hsbo.kommonitor.datamanagement.config;

import org.springframework.web.util.UriComponentsBuilder;
import springfox.documentation.spring.web.paths.Paths;
import springfox.documentation.spring.web.paths.RelativePathProvider;

import javax.servlet.ServletContext;

/**
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
public class BasePathAwareRelativePathProvider extends RelativePathProvider {
    private String basePath;

    public BasePathAwareRelativePathProvider(ServletContext servletContext, String basePath) {
        super(servletContext);
        this.basePath = basePath;
    }

    @Override
    protected String applicationPath() {
        return Paths.removeAdjacentForwardSlashes(UriComponentsBuilder.fromPath(super.applicationPath()).path(basePath).build().toString());
    }

    @Override
    public String getOperationPath(String operationPath) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath("/");
        return Paths.removeAdjacentForwardSlashes(
                uriComponentsBuilder.path(operationPath.replaceFirst("^" + basePath, "")).build().toString());
    }
}
