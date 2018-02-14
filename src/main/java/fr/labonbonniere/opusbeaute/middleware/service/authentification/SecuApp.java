package fr.labonbonniere.opusbeaute.middleware.service.authentification;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.ws.rs.NameBinding;

import fr.labonbonniere.opusbeaute.middleware.objetmetier.roles.RoleEnum;


/**
 * interface pour Lister les Roles enumeres
 * dans la Classe Enum (Rolesenum)
 * 
 * @author fred
 *
 */
@NameBinding
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface SecuApp {
	RoleEnum[] value() default {};
}
