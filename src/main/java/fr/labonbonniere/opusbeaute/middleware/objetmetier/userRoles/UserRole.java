package fr.labonbonniere.opusbeaute.middleware.objetmetier.userRoles;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserRole {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String prenom;
	private String roleDelcared;
	
	public UserRole() {
		super();
	}

	public UserRole(Integer id, String prenom, String roleDelcared) {
		super();
		this.id = id;
		this.prenom = prenom;
		this.roleDelcared = roleDelcared;
	}

	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getRoleDelcared() {
		return roleDelcared;
	}

	public void setRoleDelcared(String roleDelcared) {
		this.roleDelcared = roleDelcared;
	}

	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((prenom == null) ? 0 : prenom.hashCode());
		result = prime * result + ((roleDelcared == null) ? 0 : roleDelcared.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserRole other = (UserRole) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (prenom == null) {
			if (other.prenom != null)
				return false;
		} else if (!prenom.equals(other.prenom))
			return false;
		if (roleDelcared == null) {
			if (other.roleDelcared != null)
				return false;
		} else if (!roleDelcared.equals(other.roleDelcared))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserRole [id=" + id + ", prenom=" + prenom + ", roleDelcared=" + roleDelcared + "]";
	}

	

	
	


}
