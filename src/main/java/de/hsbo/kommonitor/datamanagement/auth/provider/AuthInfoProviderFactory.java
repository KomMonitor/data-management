package de.hsbo.kommonitor.datamanagement.auth.provider;

import de.hsbo.kommonitor.datamanagement.auth.token.TokenParser;
import de.hsbo.kommonitor.datamanagement.auth.token.TokenParserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Principal;

/**
 * @author <a href="mailto:s.drost@52north.org">Sebastian Drost</a>
 */
public interface AuthInfoProviderFactory {

    AuthInfoProvider createAuthInfoProvider();

    AuthInfoProvider createAuthInfoProvider(Principal principal, TokenParser parser);

}
