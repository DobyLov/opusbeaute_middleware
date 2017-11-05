package fr.labonbonniere.opusbeaute.middleware.objetmetier.methodeprospection;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@XmlRootElement
@Table(name = "T_METHODEPROSPECTION")
public class MethodeProspection implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer idMethodeProspection;
	private String methodeProspection;

	public MethodeProspection() {

		super();
	}

	public MethodeProspection(Integer idMethodeProspection, String methodeProspection) {

		this.idMethodeProspection = idMethodeProspection;
		this.methodeProspection = methodeProspection;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "METHODEPROSPECTION_IDMETHODEPROSPECTION", nullable = false, length = 2)
	public Integer getIdMethodeProspection() {
		return idMethodeProspection;
	}

	public void setIdMethodeProspection(Integer idMethodeProspection) {
		this.idMethodeProspection = idMethodeProspection;
	}

	@Column(name = "METHODEPROSPECTION_METHODEPROSPECTION", nullable = true, length = 30)
	public String getMethodeProspection() {
		return methodeProspection;
	}

	public void setMethodeProspection(String methodeProspection) {
		this.methodeProspection = methodeProspection;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
				.append("idMethodeProspection", this.idMethodeProspection)
				.append("methodeProspection", this.methodeProspection).build();

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idMethodeProspection == null) ? 0 : idMethodeProspection.hashCode());
		result = prime * result + ((methodeProspection == null) ? 0 : methodeProspection.hashCode());
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
		MethodeProspection other = (MethodeProspection) obj;
		if (idMethodeProspection == null) {
			if (other.idMethodeProspection != null)
				return false;
		} else if (!idMethodeProspection.equals(other.idMethodeProspection))
			return false;
		if (methodeProspection == null) {
			if (other.methodeProspection != null)
				return false;
		} else if (!methodeProspection.equals(other.methodeProspection))
			return false;
		return true;
	}

}

