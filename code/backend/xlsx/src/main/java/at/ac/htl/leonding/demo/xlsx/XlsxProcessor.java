package at.ac.htl.leonding.demo.xlsx;

import at.ac.htl.leonding.demo.importexport.Portation;

public interface XlsxProcessor extends Portation{
    default String fileType() {
        return Type.xlsx.name();
    }
}
