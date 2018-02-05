package fr.labonbonniere.opusbeaute.middleware.service.authentification;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.ws.rs.NameBinding;

import fr.labonbonniere.opusbeaute.middleware.objetmetier.roles.RoleEnum;

@NameBinding
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface SecuApp {
	RoleEnum[] value() default {};
}
