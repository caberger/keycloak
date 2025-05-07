package at.ac.htl.leonding.demo.importexport;

import java.io.InputStream;

public interface ImportProcessor {
    ImportResult parse(InputStream is);
}
