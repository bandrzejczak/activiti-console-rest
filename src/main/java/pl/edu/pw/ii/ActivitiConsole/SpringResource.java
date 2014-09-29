package pl.edu.pw.ii.ActivitiConsole;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
@Scope("prototype")
public @interface SpringResource {
    String value();
    boolean secured() default true;
}
