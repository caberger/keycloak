package at.aberger.demo.auth;

import java.util.logging.Level;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.security.Authenticator;

public class KeycloakAuthenticator extends Authenticator {
    public KeycloakAuthenticator(Context context) {
        super(context);
    }

    @Override
    protected boolean authenticate(Request request, Response response) {
        getLogger().log(Level.INFO, "authenticator " + KeycloakAuthenticator.class.getName());
        return true;
    }
}
