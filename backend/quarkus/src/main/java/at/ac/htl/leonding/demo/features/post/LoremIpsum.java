package at.ac.htl.leonding.demo.features.post;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import com.github.javafaker.Faker;

import at.ac.htl.leonding.demo.features.store.DataRoot;
import at.ac.htl.leonding.demo.features.user.User;

/** create some demo data records.  
* numberOfDemoRecordsToCreate should be zero in production, of course.
*/
public interface LoremIpsum {
    static DataRoot createDemoData(int numberOfDemoRecordsToCreate) {
        return new DataRoot(demoData(numberOfDemoRecordsToCreate));
    }
    private static List<User> demoData(int numberOfDemoRecordsToCreate) { 
        var faker = new Faker();
        var emails = List.of(
            "joe.sixpack@example.com"
        );
        var users = new LinkedList<User>();
        users.addAll(
            emails
            .stream()
            .map(index -> new User(UUID.randomUUID().toString()))
            .toList()
        );

        for (var user : users) {
            for (var i = 0; i < numberOfDemoRecordsToCreate; i++) {
                user.posts().add(new Post(faker.harryPotter().quote(), faker.chuckNorris().fact(), i % 2 == 0));
            }
        }
        return users;
    }
}
