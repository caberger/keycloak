package at.aberger.demo.features.user;

import java.util.UUID;


public record User(
    UUID id,
    String firstName,
    String lastName
) {
}
