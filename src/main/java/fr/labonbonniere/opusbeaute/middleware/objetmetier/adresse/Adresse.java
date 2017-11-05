package fr.labonbonniere.opusbeaute.middleware.objetmetier.adresse;

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

@XmlRootElement
@Entity
@Table(name = "T_ADRESSE")
public class Adresse implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer idAdresse;
	private String numero;
	private String rue;
	private Integer zipCode;
	private String ville;
	private String pays;
	
	public Adresse() {
		super();
	}

	public Adresse(Integer idAdresse, String numero, String rue, 
					Integer zipCode, String ville, String pays) {

		this.idAdresse = idAdresse;
		this.numero = numero;
		this.rue = rue;
		this.zipCode = zipCode;
		this.ville = ville;
		this.pays = pays;


	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column( name = "ADRESSE_IDADRESSE", unique = true, nullable = false, length = 4)
	public Integer getIdAdresse() {
		return idAdresse;
	}

	public void setIdAdresse(Integer idAdresse) {
		this.idAdresse = idAdresse;
	}
	@Column( name = "ADRESSE_NUMERO", nullable = true, length = 3)
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Column( name = "ADRESSE_RUE", nullable = false, length = 30)
	public String getRue() {
		return rue;
	}

	public void setRue(String rue) {
		this.rue = rue;
	}
	@Column( name = "ADRESSE_ZIPCODE", nullable = false, length = 10)
	public Integer getZipCode() {
		return zipCode;
	}

	public void setZipCode(Integer zipCode) {
		this.zipCode = zipCode;
	}
	
	@Column( name = "ADRESSE_VILLE", nullable = false, length = 20)
	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	
	@Column( name = "ADRESSE_PAYS", nullable = true, length = 15)
	public String getPays() {
		return pays;
	}

	public void setPays(String pays) {
		this.pays = pays;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
				.append("idAdresse",this.idAdresse)
				.append("numero", this.numero)
				.append("rue", this.rue)
				.append("zipCode", this.zipCode)
				.append("pays", this.pays)
				.build();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idAdresse == null) ? 0 : idAdresse.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		result = prime * result + ((pays == null) ? 0 : pays.hashCode());
		result = prime * result + ((rue == null) ? 0 : rue.hashCode());
		result = prime * result + ((zipCode == null) ? 0 : zipCode.hashCode());
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
		Adresse other = (Adresse) obj;		
		if (idAdresse == null) {
			if (other.idAdresse != null)
				return false;
		} else if (!idAdresse.equals(other.idAdresse))
			return false;		
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		if (pays == null) {
			if (other.pays != null)
				return false;
		} else if (!pays.equals(other.pays))
			return false;
		if (rue == null) {
			if (other.rue != null)
				return false;
		} else if (!rue.equals(other.rue))
			return false;
		if (zipCode == null) {
			if (other.zipCode != null)
				return false;
		} else if (!zipCode.equals(other.zipCode))
			return false;
		return true;
	}

}
