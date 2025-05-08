package at.ac.htl.leonding.demo.importexport;

import java.io.InputStream;

public interface ImportProcessor extends Portation {
    ImportResult parse(InputStream is);
}
