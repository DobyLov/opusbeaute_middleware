package fr.labonbonniere.opusbeaute.middleware.objetmetier.praticien;

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
@Table(name = "T_PRATICIEN")
public class Praticien implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer idPraticien;
	private String nomPraticien;
	private String prenomPraticien;
	private String teleMobilePraticien;
	private String adresseMailPraticien;
	private String suscribedSmsReminder;
	private String suscribedMailReminder;

	public Praticien() {
		super();
	}

	public Praticien(Integer idPraticien, String nomPraticien, String prenomPraticien, String teleMobilePraticien,
			String adresseMailPraticien, String suscribedSmsReminder, String suscribedMailReminder) {

		this.idPraticien = idPraticien;
		this.nomPraticien = nomPraticien;
		this.prenomPraticien = prenomPraticien;
		this.teleMobilePraticien = teleMobilePraticien;
		this.adresseMailPraticien = adresseMailPraticien;
		this.suscribedSmsReminder = suscribedSmsReminder;
		this.suscribedMailReminder = suscribedMailReminder;

	}

	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@SequenceGenerator(name="seq",sequenceName="PRATICIEN_SEQ",
	initialValue = 1, allocationSize = 10)
@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq")
	@Column(name = "PRATICIEN_IDPRATICIEN", nullable = false, length = 4)
	public Integer getIdPraticien() {
		return idPraticien;
	}

	public void setIdPraticien(Integer idPraticien) {
		this.idPraticien = idPraticien;
	}

	@Column(name = "PRATICIEN_NOM", nullable = false, length = 30)
	public String getNomPraticien() {
		return nomPraticien;
	}

	public void setNomPraticien(String nomPraticien) {
		this.nomPraticien = nomPraticien;
	}

	@Column(name = "UTILISATEUR_PRENOM", nullable = false, length = 30)
	public String getPrenomPraticien() {
		return prenomPraticien;
	}

	public void setPrenomPraticien(String prenomPraticien) {
		this.prenomPraticien = prenomPraticien;
	}

	@Column(name = "UTILISATEUR_TELMOBILE", nullable = true, length = 10)
	public String getTeleMobilePraticien() {
		return teleMobilePraticien;
	}

	public void setTeleMobilePraticien(String teleMobilePraticien) {
		this.teleMobilePraticien = teleMobilePraticien;
	}

	@Column(name = "UTILISATEUR_MAIL", nullable = true, length = 30)
	public String getAdresseMailPraticien() {
		return adresseMailPraticien;
	}

	public void setAdresseMailPraticien(String adresseMailPraticien) {
		this.adresseMailPraticien = adresseMailPraticien;
	}

	@Column(name = "UTILISATEUR_SUSCRIBEDSMSREMINDER", nullable = false, length = 1)
	public String getSuscribedSmsReminder() {
		return suscribedSmsReminder;
	}

	public void setSuscribedSmsReminder(String suscribedSmsReminder) {
		this.suscribedSmsReminder = suscribedSmsReminder;
	}

	@Column(name = "UTILISATEUR_SUSCRIBEDMAILREMINDER", nullable = false, length = 1)
	public String getSuscribedMailReminder() {
		return suscribedMailReminder;
	}

	public void setSuscribedMailReminder(String suscribedMailReminder) {
		this.suscribedMailReminder = suscribedMailReminder;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.JSON_STYLE).append("idPraticien", this.idPraticien)
				.append("nomPraticien", this.nomPraticien).append("prenomPraticien", this.prenomPraticien)
				.append("teleMobilePraticien", this.teleMobilePraticien)
				.append("adresseMailPraticien", this.adresseMailPraticien)
				.append("suscribedSmsReminder", this.suscribedSmsReminder).build();

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((adresseMailPraticien == null) ? 0 : adresseMailPraticien.hashCode());
		result = prime * result + ((idPraticien == null) ? 0 : idPraticien.hashCode());
		result = prime * result + ((nomPraticien == null) ? 0 : nomPraticien.hashCode());
		result = prime * result + ((prenomPraticien == null) ? 0 : prenomPraticien.hashCode());
		result = prime * result + ((teleMobilePraticien == null) ? 0 : teleMobilePraticien.hashCode());
		result = prime * result + ((suscribedSmsReminder == null) ? 0 : suscribedSmsReminder.hashCode());
		result = prime * result + ((suscribedMailReminder == null) ? 0 : suscribedMailReminder.hashCode());

		return result;
	}

	@Override
	public boolean equals(Object candidat) {
		if (candidat == this)
			return true;

		if (candidat == null)
			return false;

		if (!(candidat instanceof Praticien))
			return false;

		final Praticien autre = (Praticien) candidat;

		return new EqualsBuilder().append(this.idPraticien, autre.idPraticien)
				.append(this.nomPraticien, autre.nomPraticien).append(this.prenomPraticien, autre.prenomPraticien)
				.append(this.teleMobilePraticien, autre.teleMobilePraticien)
				.append(this.adresseMailPraticien, autre.adresseMailPraticien)
				.append(this.suscribedMailReminder, autre.suscribedMailReminder)
				.append(this.suscribedSmsReminder, autre.suscribedSmsReminder)

				.build();

	}
}
