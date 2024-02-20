package de.hsbo.kommonitor.datamanagement.auth.provider;

import de.hsbo.kommonitor.datamanagement.auth.token.TokenParser;
import de.hsbo.kommonitor.datamanagement.auth.token.TokenParserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
@ConditionalOnProperty(
        value = "kommonitor.access-control.profile",
        havingValue = "group-based",
        matchIfMissing = false)
public class GroupBasedAuthInfoProviderFactory implements AuthInfoProviderFactory {


    @Autowired
    private TokenParserFactory tokenParserFactory;


    public AuthInfoProvider createAuthInfoProvider() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return createAuthInfoProvider(auth, tokenParserFactory.createTokenParser(auth));
    }

    public AuthInfoProvider createAuthInfoProvider(Principal principal, TokenParser parser) {
        return new GroupBasedAuthInfoProvider(principal, parser);
    }
}
