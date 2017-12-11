package fr.labonbonniere.opusbeaute.middleware.objetmetier.adresselieurdv;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


@XmlRootElement
@Entity
@Table(name = "T_ADRESSELIEURDV")
public class AdresseLieuRdv implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer idAdresseLieuRdv;
	private String numero;
	private String rue;
	private String zipCode;
	private String ville;
	private String pays;

	public AdresseLieuRdv() {
		super();
	}

	public AdresseLieuRdv(Integer idAdresseLieuRdv, String numero, String rue, 
			String zipCode, String ville, String pays) {

		this.idAdresseLieuRdv = idAdresseLieuRdv;
		this.numero = numero;
		this.rue = rue;
		this.zipCode = zipCode;
		this.ville = ville;
		this.pays = pays;
		

	}

	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@GeneratedValue
	@SequenceGenerator(name="seq",sequenceName="ADRESSELIEURRDV_SEQ",
	initialValue = 4, allocationSize = 50)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq")
	@Column(name = "ADRESSELIEURDV_IDADRESSELIEURDV", unique = true, nullable = false)
	public Integer getIdAdresseLieuRdv() {
		return idAdresseLieuRdv;
	}

	public void setIdAdresseLieuRdv(Integer idAdresseLieuRdv) {
		this.idAdresseLieuRdv = idAdresseLieuRdv;
	}

	@Column(name = "ADRESSELIEURDV_NUMERO", nullable = true, length = 3)
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Column(name = "ADRESSELIEURDV_RUE", nullable = true, length = 30)
	public String getRue() {
		return rue;
	}

	public void setRue(String rue) {
		this.rue = rue;
	}

	@Column(name = "ADRESSELIEURDV_ZIPCODE", nullable = true, length = 5)
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Column(name = "ADRESSELIEURDV_VILLE", nullable = true, length = 30)
	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	@Column(name = "ADRESSELIEURDV_PAYS", nullable = true, length = 15)
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
				.append("idAdresseLieuRdv", this.idAdresseLieuRdv)
				.append("numero", this.numero).append("rue", this.rue).append("zipCode", this.zipCode)
				.append("ville", this.ville).append("pays", this.pays).build();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idAdresseLieuRdv == null) ? 0 : idAdresseLieuRdv.hashCode());
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

		if (!(candidat instanceof AdresseLieuRdv))
			return false;

		final AdresseLieuRdv autre = (AdresseLieuRdv) candidat;

		return new EqualsBuilder().append(this.idAdresseLieuRdv, autre.idAdresseLieuRdv)
				// .append(this.client, autre.client)
				.append(this.numero, autre.numero).append(this.rue, autre.rue).append(this.ville, autre.ville)
				.append(this.zipCode, autre.zipCode).append(this.pays, autre.pays).build();
	}
	

}
