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

@XmlRootElement
@Entity
@Table(name = "T_ROLES")
public class Roles implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer idRoles;
	private String roles;
	
	public Roles() {
		super();
	}
	
	public Roles (Integer idRoles, String roles) {		
		this.idRoles = idRoles;
		this.roles = roles;
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

	@Column(name ="ROLES_ROLES", length = 20)
	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
				.append("idRoles", this.idRoles)
				.append("roles", this.roles)
				.build();

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idRoles == null) ? 0 : idRoles.hashCode());
		result = prime * result + ((roles == null) ? 0 : roles.hashCode());
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
				.append(this.roles, autre.roles)
				
				.build();

	}
	
}
