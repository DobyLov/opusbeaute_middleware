package fr.labonbonniere.opusbeaute.middleware.service.authentification;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.ws.rs.NameBinding;

@NameBinding
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface SecuApp {

}
