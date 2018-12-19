package fr.labonbonniere.opusbeaute.middleware.objetmetier.activite;

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
 * Objet Metier Activite
 * 
 * Lors de l intervention de L ORM Hibernate / JPA, 
 * l instance devient une entite
 * 
 * "@Override" des Methodes
 * 
 *          toString Equals HashCode
 * 
 * @author fred
 *
 */

@XmlRootElement
@Entity
@Table(name = "T_ACTIVITE")
public class Activite {
	
	private static final long serialVersionUID = 1L;
	
	private Integer idActivite;
	private String activiteNom;
	
	public Activite() {
		
		super();
		
	}
	
	public Activite( String activiteNom, Integer idActivite) {
		
		super();
		this.idActivite = idActivite;
		this.activiteNom = activiteNom;
		
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ACTIVITE_IDACTIVITE", nullable = false, length = 3)
	public Integer getIdActivite() {
		return idActivite;
	}

	public void setIdActivite(Integer idActivite) {
		this.idActivite = idActivite;
	}

	@Column(name = "ACTIVITE_ACTIVITENOM", length = 50)
	public String getActiviteNom() {
		return activiteNom;
	}

	public void setActiviteNom(String activiteNom) {
		this.activiteNom = activiteNom;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
				.append("idActivite", this.idActivite)
				.append("activite", this.activiteNom).build();

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((activiteNom == null) ? 0 : activiteNom.hashCode());
		result = prime * result + ((idActivite == null) ? 0 : idActivite.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object candidat) {
		if (candidat == this)
			return true;

		if (candidat == null)
			return false;

		if (!(candidat instanceof Activite))
			return false;

		final Activite autre = (Activite) candidat;

		return new EqualsBuilder()
				.append(this.idActivite, autre.idActivite)
				.append(this.activiteNom, autre.activiteNom).build();
	}
}
