package filtres;

import utilities.TokenStore;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Request filter that validates the Authorization bearer token header
 * before giving access to secured endpoints.
 */
@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authorizationHeader = requestContext.getHeaderString(AUTHORIZATION_HEADER);

        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            abortRequest(requestContext);
            return;
        }

        String token = authorizationHeader.substring(BEARER_PREFIX.length()).trim();
        TokenStore tokenStore = TokenStore.getInstance();

        if (!tokenStore.isTokenValid(token)) {
            abortRequest(requestContext);
        }
    }

    private void abortRequest(ContainerRequestContext requestContext) {
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                .entity("Accès refusé : jeton invalide ou manquant").build());
    }
}


