package at.ac.htl.leonding.demo.lib;

import java.lang.System.Logger;

public interface Log {
    static Logger log(Class<?> clazz) {
        return System.getLogger(clazz.getName());
    }
}
