package fr.labonbonniere.opusbeaute.middleware.objetmetier.adresse;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.builder.EqualsBuilder;
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
	private String zipCode;
	private String ville;
	private String pays;

	public Adresse() {
		super();
	}

	public Adresse(Integer idAdresse, String numero, String rue, String zipCode, String ville, String pays) {

		this.idAdresse = idAdresse;
		this.numero = numero;
		this.rue = rue;
		this.zipCode = zipCode;
		this.ville = ville;
		this.pays = pays;
		

	}

	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue
	@Column(name = "CLIENT_IDADRESSE", unique = true, nullable = false)
	public Integer getIdAdresse() {
		return idAdresse;
	}

	public void setIdAdresse(Integer idAdresse) {
		this.idAdresse = idAdresse;
	}

	@Column(name = "ADRESSE_NUMERO", nullable = true, length = 3)
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Column(name = "ADRESSE_RUE", nullable = true, length = 30)
	public String getRue() {
		return rue;
	}

	public void setRue(String rue) {
		this.rue = rue;
	}

	@Column(name = "ADRESSE_ZIPCODE", nullable = true, length = 5)
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Column(name = "ADRESSE_VILLE", nullable = true, length = 30)
	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	@Column(name = "ADRESSE_PAYS", nullable = true, length = 15)
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
		return new ToStringBuilder(this, ToStringStyle.JSON_STYLE).append("idAdresse", this.idAdresse)
				.append("numero", this.numero).append("rue", this.rue).append("zipCode", this.zipCode)
				.append("ville", this.ville).append("pays", this.pays).build();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idAdresse == null) ? 0 : idAdresse.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		result = prime * result + ((pays == null) ? 0 : pays.hashCode());
		result = prime * result + ((rue == null) ? 0 : rue.hashCode());
		result = prime * result + ((ville == null) ? 0 : ville.hashCode());
		result = prime * result + ((zipCode == null) ? 0 : zipCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object candidat) {
		if (candidat == this)
			return true;

		if (candidat == null)
			return false;

		if (!(candidat instanceof Adresse))
			return false;

		final Adresse autre = (Adresse) candidat;

		return new EqualsBuilder().append(this.idAdresse, autre.idAdresse)
				// .append(this.client, autre.client)
				.append(this.numero, autre.numero).append(this.rue, autre.rue).append(this.ville, autre.ville)
				.append(this.zipCode, autre.zipCode).append(this.pays, autre.pays).build();
	}
	

}
