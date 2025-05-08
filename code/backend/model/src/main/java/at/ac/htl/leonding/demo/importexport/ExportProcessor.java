package at.ac.htl.leonding.demo.importexport;

import java.io.OutputStream;
import java.util.Collection;
import java.util.function.Consumer;

import at.ac.htl.leonding.demo.model.Category;
import at.ac.htl.leonding.demo.model.User;

/** Service Provider Interface for exporting our database. */
public interface ExportProcessor extends Portation {
    /* write our data to the stream */
    public Consumer<OutputStream> export(Collection<User> users, Collection<Category> categories);
}
