package at.ac.htl.leonding.demo.features.importexport;

import java.io.OutputStream;
import java.util.Collection;
import java.util.function.Consumer;

import at.ac.htl.leonding.demo.model.Category;
import at.ac.htl.leonding.demo.model.User;

public interface ImportProcessor {
    Consumer<OutputStream> export(Collection<User> users, Collection<Category> categories);
}
