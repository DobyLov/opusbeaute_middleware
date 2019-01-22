package fr.labonbonniere.opusbeaute.middleware.objetmetier.roles;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Objet Metier Roles
 * 
 * Lors de l intervention de L ORM Hibernate / JPA, 
 * l instance devient une entite
 * 
 * "@Overide" des Methodes toString Equals HashCode
 * 
 * @author fred
 *
 */
@XmlRootElement
@Entity
@Table(name = "T_ROLES")
public class Roles implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer idRoles;
	private String rolesName;
	
	public Roles() {
		super();
	}
	
	public Roles (Integer idRoles, String rolesName) {	
		
		super();
		this.idRoles = idRoles;
		this.rolesName = rolesName;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ROLES_IDROLES", unique = true, nullable = false, length = 1)
	public Integer getIdRoles() {
		return idRoles;
	}

	public void setIdRoles(Integer idRoles) {
		this.idRoles = idRoles;
	}

	@Column(name ="ROLES_ROLESNAME", length = 20)
	public String getRolesName() {
		return rolesName;
	}

	public void setRolesName(String rolesName) {
		this.rolesName = rolesName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
				.append("idRoles", this.idRoles)
				.append("rolesName", this.rolesName)
				.build();

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idRoles == null) ? 0 : idRoles.hashCode());
		result = prime * result + ((rolesName == null) ? 0 : rolesName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object candidat) {
		if (candidat == this)
			return true;

		if (candidat == null)
			return false;

		if (!(candidat instanceof Roles))
			return false;

		final Roles autre = (Roles) candidat;

		return new EqualsBuilder()
				.append(this.idRoles, autre.idRoles)
				.append(this.rolesName, autre.rolesName)
				
				.build();

	}
	
}
