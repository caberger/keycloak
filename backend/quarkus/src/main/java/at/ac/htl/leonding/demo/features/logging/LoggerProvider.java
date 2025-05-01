package at.ac.htl.leonding.demo.features.logging;

import java.lang.System.Logger;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;

public class LoggerProvider {
    @Produces
    @Dependent
    public Logger createLogger(final InjectionPoint injectionPoint) {
        Class<?> declaringClass = injectionPoint.getMember().getDeclaringClass();
        return System.getLogger(declaringClass.getSimpleName());
    }
}
