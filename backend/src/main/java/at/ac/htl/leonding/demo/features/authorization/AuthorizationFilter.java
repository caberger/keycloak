package at.ac.htl.leonding.demo.features.authorization;

import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.UUID;

import org.eclipse.microprofile.jwt.JsonWebToken;

import at.ac.htl.leonding.demo.features.user.User;
import at.ac.htl.leonding.demo.features.user.UserRepository;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

/**
 * Users are created by keycloak and when we see them for the first time we store them.
 * If we see such users, they have already been authenticated by keycloak, so we trust and welcome them in our database.
 */
@Provider
@Priority(Priorities.USER)
public class AuthorizationFilter implements ContainerRequestFilter {
    @Inject JsonWebToken jwt;
    @Inject Logger log;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (jwt.getSubject() != null) {
            final var id = UUID.fromString(jwt.getSubject());
            var existingUser = UserRepository.find(id);
            if (!existingUser.isPresent()) {
                var user = new User(id);
                log.log(Level.INFO, "we welcome our new user {0}", user.id());
                UserRepository.add(user);
            }
        }
    }
    void addUser() {
        final var id = UUID.fromString(jwt.getSubject());
        var existingUser = UserRepository.find(id);
        if (!existingUser.isPresent()) {
            var user = new User(id);
            log.log(Level.INFO, "we welcome our new user {0}", user.id());
            UserRepository.add(user);
        }
    }
}
