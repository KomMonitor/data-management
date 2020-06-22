package de.hsbo.kommonitor.datamanagement.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * Repository that provides {@link AuthInfoProvider} implementations
 *
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
@Component
public class AuthInfoProviderRepository {

    @Autowired
    private List<AuthInfoProvider> authInfoProviderList;

    /**
     * Retrieve a {@link AuthInfoProvider} implementation dependent on {@link Principal} provided by the
     * {@link RequestContextHolder}.
     *
     * @return A {@link AuthInfoProvider} implementation
     */
    public Optional<AuthInfoProvider> getAuthInfoProvider() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Principal principal = attributes.getRequest().getUserPrincipal();
        return authInfoProviderList.stream()
                .filter(a -> a.supportsPrincipal(principal))
                .findFirst();
    }
}
