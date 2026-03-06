package ressources;

import entities.Credentials;
import utilities.TokenStore;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationEndPoint {

    private static final Map<String, String> USERS = new HashMap<>();

    static {
        USERS.put("admin", "admin");
        USERS.put("enseignant", "enseignant");
    }

    @POST
    @Path("/login")
    public Response authenticate(Credentials credentials) {
        if (credentials == null
                || credentials.getLogin() == null
                || credentials.getPassword() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Login et mot de passe obligatoires").build();
        }

        String expectedPassword = USERS.get(credentials.getLogin());
        if (expectedPassword == null || !expectedPassword.equals(credentials.getPassword())) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Identifiants incorrects").build();
        }

        String token = TokenStore.getInstance().generateToken(credentials.getLogin());
        Map<String, Object> payload = new HashMap<>();
        payload.put("token", token);
        payload.put("expiresIn", 30 * 60); // seconds

        return Response.ok(payload).build();
    }
}

