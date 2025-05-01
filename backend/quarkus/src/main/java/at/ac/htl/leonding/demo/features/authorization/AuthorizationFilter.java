package at.ac.htl.leonding.demo.features.authorization;

import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;

import org.eclipse.microprofile.jwt.JsonWebToken;
import at.ac.htl.leonding.demo.features.store.Database;
import at.ac.htl.leonding.demo.features.user.User;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

/**
 * Users are created by keycloak.
 * In this filter we see if we meet a new Users.
 * If so, they have already been authenticated by keycloak, so we trust and welcome them in our database.
 */
@Provider
@Priority(Priorities.USER)
public class AuthorizationFilter implements ContainerRequestFilter {
    @Inject JsonWebToken jwt;
    @Inject Database database;
    @Inject Logger log;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (jwt.getSubject() != null) {
            addUser();
        }
    }
    void addUser() {
        var existingUser = database
            .users()
            .stream()
            .filter(user -> user.id().equals(jwt.getSubject()))
            .findAny()
            ;
        if (!existingUser.isPresent()) {
            var user = new User(jwt.getSubject());
            log.log(Level.INFO, "we welcome our new user {0}", user.id());
            database.update(store -> {
                database.users().add(user);
                store.store(database.root());
            });
        }
    }
}
