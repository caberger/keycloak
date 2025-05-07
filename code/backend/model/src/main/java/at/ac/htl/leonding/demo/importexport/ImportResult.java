package at.ac.htl.leonding.demo.importexport;

import java.util.List;

import at.ac.htl.leonding.demo.model.Category;
import at.ac.htl.leonding.demo.model.User;

public sealed interface ImportResult permits ImportResult.Success, ImportResult.Failed {
    record Success(List<User> users, List<Category> categories) implements ImportResult {}
    record Failed(Exception exception) implements ImportResult {}
}    
