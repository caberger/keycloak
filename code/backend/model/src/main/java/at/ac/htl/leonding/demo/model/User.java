package at.ac.htl.leonding.demo.model;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** We mirror keycloak users in our database.
 * User data is stored in keycloak only and fetched with the API if required.
 * @param id the keycloak user id
 * @param posts the posts created by this user.
*/
public record User(
    UUID id,
    List<Post> posts
) {
    public User(UUID id) {
        this(id, new ArrayList<>());
    }
}
