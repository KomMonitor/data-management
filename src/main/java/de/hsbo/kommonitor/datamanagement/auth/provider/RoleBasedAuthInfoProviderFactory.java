package de.hsbo.kommonitor.datamanagement.auth.provider;

import de.hsbo.kommonitor.datamanagement.auth.token.TokenParser;
import de.hsbo.kommonitor.datamanagement.auth.token.TokenParserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
@ConditionalOnProperty
public class RoleBasedAuthInfoProviderFactory implements AuthInfoProviderFactory{
    @Value("${kommonitor.access-control.authenticated-users.organizationalUnit:kommonitor}")
    private String adminRolePrefix;

    @Value("${kommonitor.access-control.anonymous-users.organizationalUnit:public}")
    private String publicRole;

    @Autowired
    private TokenParserFactory tokenParserFactory;


    public AuthInfoProvider createAuthInfoProvider() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return createAuthInfoProvider(auth, tokenParserFactory.createTokenParser(auth));
    }

    public AuthInfoProvider createAuthInfoProvider(Principal principal, TokenParser parser) {
        return new RoleBasedAuthInfoProvider(principal, parser, adminRolePrefix, publicRole);
    }
}
