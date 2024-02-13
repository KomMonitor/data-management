package de.hsbo.kommonitor.datamanagement.auth.token;

import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.BadJWTException;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import de.hsbo.kommonitor.datamanagement.auth.Group;
import org.junit.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.text.ParseException;
import java.util.Set;

public class JwtTokenParserTest {

    // Set a JWT token for local testing purpose. Beware of committing the token!!1!
    private static final  String JWT_TOKEN = "YOUR_JWT_TOKEN_HERE";

    private static JwtAuthenticationToken token;

    @BeforeAll
    static void init() {
        JwtDecoder jwtDecoder = new NimbusJwtDecoder(new MockJwtProcessor());
        Jwt jwt = jwtDecoder.decode(JWT_TOKEN);
        token = new JwtAuthenticationToken(jwt);
    }

    @Test
    @Ignore
    void getOwnedRolesTest() {
        JwtTokenParser parser = new JwtTokenParser();
        Set<Group> groups = parser.getGroupMemberships(token);

        Assertions.assertFalse(groups.isEmpty());
    }


    /**
     * This snippet is inspired by https://github.com/spring-projects/spring-security/blob/e681d79bc34341a1d887c75c3aeb7469cdb3f8a1/oauth2/oauth2-jose/src/test/java/org/springframework/security/oauth2/jwt/NimbusJwtDecoderTests.java#L885-L898
     */
    private static class MockJwtProcessor extends DefaultJWTProcessor<SecurityContext> {

        @Override
        public JWTClaimsSet process(SignedJWT signedJWT, SecurityContext context) throws BadJOSEException {
            try {
                return signedJWT.getJWTClaimsSet();
            }
            catch (ParseException ex) {
                throw new BadJWTException(ex.getMessage(), ex);
            }
        }

    }

}
