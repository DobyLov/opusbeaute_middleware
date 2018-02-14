package fr.labonbonniere.opusbeaute.middleware.objetmetier.roles;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.ws.rs.NameBinding;
/**
 * Interface des Roles
 * Liaison avec RoleEnum
 * 
 * @author fred
 *
 */
@NameBinding
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface DefineUserRole {
	String[] value();
}
