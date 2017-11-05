package fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@XmlRootElement
@Entity
@Table(name = "T_UTILISATEUR")
public class Utilisateur implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer idUtilisateur;
	private String nomUtilisateur;
	private String prenomUtilisateur;
	private String telephoneUtilisateur;
	private String adresseUtilisateur;
	private String adresseMailUtilisateur;
	private String motDePasse;
	private Date dateAnniversaireUtilisateur;
	private boolean isLogged;

	public Utilisateur() {
		super();
	}

	public Utilisateur(Integer idUtilisateur, String nomUtilisateur, String prenomUtilisateur,
			String telephoneUtilisateur, String adresseUtilisateur, String adresseMailUtilisateur, String motDePasse,
			Date dateAnniversaireUtilisateur, boolean isLogged) {

		this.idUtilisateur = idUtilisateur;
		this.nomUtilisateur = nomUtilisateur;
		this.prenomUtilisateur = prenomUtilisateur;
		this.telephoneUtilisateur = telephoneUtilisateur;
		this.adresseUtilisateur = adresseUtilisateur;
		this.adresseMailUtilisateur = adresseMailUtilisateur;
		this.motDePasse = motDePasse;
		this.dateAnniversaireUtilisateur = dateAnniversaireUtilisateur;
		this.isLogged = isLogged;

	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "UTILISATEUR_IDUTILISATEUR", nullable = false, length = 4)
	public Integer getIdUtilisateur() {
		return idUtilisateur;
	}

	public void setIdUtilisateur(Integer idUtilisateur) {
		this.idUtilisateur = idUtilisateur;
	}

	@Column(name = "UTILISATEUR_NOM", nullable = false, length = 30)
	public String getNomUtilisateur() {
		return nomUtilisateur;
	}

	public void setNomUtilisateur(String nomUtilisateur) {
		this.nomUtilisateur = nomUtilisateur;
	}
	@Column(name = "UTILISATEUR_PRENOM", nullable = false, length = 30)
	public String getPrenomUtilisateur() {
		return prenomUtilisateur;
	}

	public void setPrenomUtilisateur(String prenomUtilisateur) {
		this.prenomUtilisateur = prenomUtilisateur;
	}
	@Column(name = "UTILISATEUR_TELEPHONE", nullable = true, length = 10)
	public String getTelephoneUtilisateur() {
		return telephoneUtilisateur;
	}

	public void setTelephoneUtilisateur(String telephoneUtilisateur) {
		this.telephoneUtilisateur = telephoneUtilisateur;
	}
	@Column(name = "UTILISATEUR_ADRESSE", nullable = true, length = 30)
	public String getAdresseUtilisateur() {
		return adresseUtilisateur;
	}

	public void setAdresseUtilisateur(String adresseUtilisateur) {
		this.adresseUtilisateur = adresseUtilisateur;
	}
	@Column(name = "UTILISATEUR_MAIL", nullable = true, length = 30)
	public String getAdresseMailUtilisateur() {
		return adresseMailUtilisateur;
	}

	public void setAdresseMailUtilisateur(String adresseMailUtilisateur) {
		this.adresseMailUtilisateur = adresseMailUtilisateur;
	}
	@Column(name = "UTILISATEUR_MOTDEPASSE", nullable = false, length = 30)
	public String getMotDePasse() {
		return motDePasse;
	}

	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}
	@Column(name = "UTILISATEUR_DATEANNIVERSAIRE", nullable = false, length = 30)
	@Temporal(TemporalType.DATE)
	public Date getDateAnniversaireUtilisateur() {
		return dateAnniversaireUtilisateur;
	}

	public void setDateAnniversaireUtilisateur(Date dateAnniversaireUtilisateur) {
		this.dateAnniversaireUtilisateur = dateAnniversaireUtilisateur;
	}
	@Column(name = "UTILISATEUR_ISLOGGED", nullable = false)
	public boolean isLogged() {
		return isLogged;
	}

	public void setLogged(boolean isLogged) {
		this.isLogged = isLogged;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.JSON_STYLE).append("idUtilisateur", this.idUtilisateur)
				.append("nomUtilisateur", this.nomUtilisateur).append("prenomUtilisateur", this.prenomUtilisateur)
				.append("telephoneUtilisateur", this.telephoneUtilisateur)
				.append("adressUtilisateur", this.adresseMailUtilisateur)
				.append("adresseMailUtilisateur", this.adresseMailUtilisateur).append("isLogged", this.isLogged)
				.append("dateAnniversaireUtilisateur", this.dateAnniversaireUtilisateur).build();

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((adresseMailUtilisateur == null) ? 0 : adresseMailUtilisateur.hashCode());
		result = prime * result + ((adresseUtilisateur == null) ? 0 : adresseUtilisateur.hashCode());
		result = prime * result + ((dateAnniversaireUtilisateur == null) ? 0 : dateAnniversaireUtilisateur.hashCode());
		result = prime * result + ((idUtilisateur == null) ? 0 : idUtilisateur.hashCode());
		result = prime * result + (isLogged ? 1231 : 1237);
		result = prime * result + ((motDePasse == null) ? 0 : motDePasse.hashCode());
		result = prime * result + ((nomUtilisateur == null) ? 0 : nomUtilisateur.hashCode());
		result = prime * result + ((prenomUtilisateur == null) ? 0 : prenomUtilisateur.hashCode());
		result = prime * result + ((telephoneUtilisateur == null) ? 0 : telephoneUtilisateur.hashCode());
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
		Utilisateur other = (Utilisateur) obj;
		if (adresseMailUtilisateur == null) {
			if (other.adresseMailUtilisateur != null)
				return false;
		} else if (!adresseMailUtilisateur.equals(other.adresseMailUtilisateur))
			return false;
		if (adresseUtilisateur == null) {
			if (other.adresseUtilisateur != null)
				return false;
		} else if (!adresseUtilisateur.equals(other.adresseUtilisateur))
			return false;
		if (dateAnniversaireUtilisateur == null) {
			if (other.dateAnniversaireUtilisateur != null)
				return false;
		} else if (!dateAnniversaireUtilisateur.equals(other.dateAnniversaireUtilisateur))
			return false;
		if (idUtilisateur == null) {
			if (other.idUtilisateur != null)
				return false;
		} else if (!idUtilisateur.equals(other.idUtilisateur))
			return false;
		if (isLogged != other.isLogged)
			return false;
		if (motDePasse == null) {
			if (other.motDePasse != null)
				return false;
		} else if (!motDePasse.equals(other.motDePasse))
			return false;
		if (nomUtilisateur == null) {
			if (other.nomUtilisateur != null)
				return false;
		} else if (!nomUtilisateur.equals(other.nomUtilisateur))
			return false;
		if (prenomUtilisateur == null) {
			if (other.prenomUtilisateur != null)
				return false;
		} else if (!prenomUtilisateur.equals(other.prenomUtilisateur))
			return false;
		if (telephoneUtilisateur == null) {
			if (other.telephoneUtilisateur != null)
				return false;
		} else if (!telephoneUtilisateur.equals(other.telephoneUtilisateur))
			return false;
		return true;
	}

}
