package fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import fr.labonbonniere.opusbeaute.middleware.objetmetier.roles.Roles;

/**
 * Objet Metier Utilisateur
 * 
 * Lors de l intervention de L ORM Hibernate / JPA, 
 * l instance devient une entite
 * 
 * Surcharges des Methodes
 * 
 *          toString Equals HashCode
 * 
 * @author fred
 *
 */
@XmlRootElement
@Entity
@Table(name = "T_UTILISATEUR")
public class Utilisateur implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer idUtilisateur;
	private String nomUtilisateur;
	private String prenomUtilisateur;
	private String teleMobileUtilisateur;
	private String adresseMailUtilisateur;
	private String motDePasse;
	private Roles rolesUtilisateur;
	private boolean isLogged;
	private boolean suscribedSmsReminder;
	private boolean suscribedMailReminder;
	private Timestamp pwdExpirationDateTime;
	private Boolean compteEffacable;
	
	public Utilisateur() {
		super();
	}

	public Utilisateur(Integer idUtilisateur, String nomUtilisateur, String prenomUtilisateur,
			String teleMobileUtilisateur, String adresseMailUtilisateur, String motDePasse,
			Roles rolesUtilisateur, boolean isLogged, boolean suscribedSmsReminder, boolean suscribedMailReminder,
			Timestamp pwdExpirationDateTime, boolean compteEffacable) {

		super();
		this.idUtilisateur = idUtilisateur;
		this.nomUtilisateur = nomUtilisateur;
		this.prenomUtilisateur = prenomUtilisateur;
		this.teleMobileUtilisateur = teleMobileUtilisateur;
		this.adresseMailUtilisateur = adresseMailUtilisateur;
		this.motDePasse = motDePasse;
		this.rolesUtilisateur = rolesUtilisateur;
		this.isLogged = isLogged;
		this.suscribedSmsReminder = suscribedSmsReminder;
		this.suscribedMailReminder = suscribedMailReminder;
		this.pwdExpirationDateTime = pwdExpirationDateTime;
		this.compteEffacable = compteEffacable;

	}

	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@SequenceGenerator(name="seq",sequenceName="UTILISATEUR_SEQ",
	initialValue = 5, allocationSize = 5)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq")
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
	@Column(name = "UTILISATEUR_TELMOBILE", nullable = true, length = 10)
	public String getTeleMobileUtilisateur() {
		return teleMobileUtilisateur;
	}

	public void setTeleMobileUtilisateur(String teleMobileUtilisateur) {
		this.teleMobileUtilisateur = teleMobileUtilisateur;
	}

	@Column(name = "UTILISATEUR_MAIL", nullable = true, length = 30)
	public String getAdresseMailUtilisateur() {
		return adresseMailUtilisateur;
	}

	public void setAdresseMailUtilisateur(String adresseMailUtilisateur) {
		this.adresseMailUtilisateur = adresseMailUtilisateur;
	}
	
	@Column(name = "UTILISATEUR_MOTDEPASSE", nullable = false, length = 70)
	public String getMotDePasse() {
		return motDePasse;
	}

	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}

	@OneToOne
	@JoinColumn(name = "UTILISATEUR_IDROLES_fk", referencedColumnName = "ROLES_IDROLES", 
	nullable = true, updatable = true, insertable = true)
	public Roles getRolesUtilisateur() {
		return rolesUtilisateur;
	}

	public void setRolesUtilisateur(Roles rolesUtilisateur) {
		this.rolesUtilisateur = rolesUtilisateur;
	}


	@Column(name = "UTILISATEUR_ISLOGGED", nullable = true)
	public boolean getIsLogged() {
		return isLogged;
	}


	public void setIsLogged(boolean isLogged) {
		this.isLogged = isLogged;
	}

	@Column(name = "UTILISATEUR_SUSCRIBEDSMSREMINDER", nullable = false)
	public boolean getSuscribedSmsReminder() {
		return suscribedSmsReminder;
	}

	public void setSuscribedSmsReminder(boolean suscribedSmsReminder) {
		this.suscribedSmsReminder = suscribedSmsReminder;
	}

	@Column(name = "UTILISATEUR_SUSCRIBEDMAILREMINDER", nullable = false)
	public boolean getSuscribedMailReminder() {
		return suscribedMailReminder;
	}

	public void setSuscribedMailReminder(boolean suscribedMailReminder) {
		this.suscribedMailReminder = suscribedMailReminder;
	}

	@Column(name = "UTILISATEUR_PWDEXPIRATIONDATETIME", nullable = true, length = 13)
	public Timestamp getPwdExpirationDateTime() {
		return pwdExpirationDateTime;
	}
	
	public void setPwdExpirationDateTime(Timestamp pwdExpirationDateTime) {
		this.pwdExpirationDateTime = pwdExpirationDateTime;
	}
	
	@Column(name = "UTILISATEUR_COMPTEEFFACABLE", nullable = false, length = 1)
	public Boolean getCompteEffacable() {
		return compteEffacable;
	}

	public void setCompteEffacable(Boolean compteEffacable) {
		this.compteEffacable = compteEffacable;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
				.append("idUtilisateur", this.idUtilisateur)
				.append("nomUtilisateur", this.nomUtilisateur)
				.append("prenomUtilisateur", this.prenomUtilisateur)
				.append("teleMobileUtilisateur", this.teleMobileUtilisateur)
				.append("adresseMailUtilisateur", this.adresseMailUtilisateur)
				.append("motdePasse", this.motDePasse)
				.append("rolesUtilisateur", this.rolesUtilisateur)
				.append("isLogged", this.isLogged)
				.append("suscribedSmsReminder",this.suscribedSmsReminder)
				.append("suscribedMailReminder",this.suscribedMailReminder)
				.append("pwdExpirationDateTime",this.pwdExpirationDateTime)
				.append("compteEffacable",this.compteEffacable)
				.build();

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((adresseMailUtilisateur == null) ? 0 : adresseMailUtilisateur.hashCode());
		result = prime * result + ((idUtilisateur == null) ? 0 : idUtilisateur.hashCode());
//		result = prime * result + ((isLogged == null) ? 0 : isLogged.hashCode());
		result = prime * result + (Boolean.hashCode(isLogged));
		result = prime * result + ((motDePasse == null) ? 0 : motDePasse.hashCode());
		result = prime * result + ((rolesUtilisateur == null) ? 0 : rolesUtilisateur.hashCode());
		result = prime * result + ((nomUtilisateur == null) ? 0 : nomUtilisateur.hashCode());
		result = prime * result + ((prenomUtilisateur == null) ? 0 : prenomUtilisateur.hashCode());
		result = prime * result + ((teleMobileUtilisateur == null) ? 0 : teleMobileUtilisateur.hashCode());		
//		result = prime * result + ((suscribedSmsReminder == null) ? 0 : suscribedSmsReminder.hashCode());
		result = prime * result + (Boolean.hashCode(suscribedSmsReminder));
//		result = prime * result + ((suscribedMailReminder == null) ? 0 : suscribedMailReminder.hashCode(suscribedMailReminder));
		result = prime * result + (Boolean.hashCode(suscribedMailReminder));
		result = prime * result + ((pwdExpirationDateTime == null) ? 0 : pwdExpirationDateTime.hashCode());
		result = prime * result + ((compteEffacable == null) ? 0 : compteEffacable.hashCode());
		
		return result;
	}

	@Override
	public boolean equals(Object candidat) {
		if (candidat == this)
			return true;

		if (candidat == null)
			return false;

		if (!(candidat instanceof Utilisateur))
			return false;

		final Utilisateur autre = (Utilisateur) candidat;

		return new EqualsBuilder()
				.append(this.idUtilisateur, autre.idUtilisateur)
				.append(this.nomUtilisateur, autre.nomUtilisateur)
				.append(this.prenomUtilisateur, autre.prenomUtilisateur)
				.append(this.teleMobileUtilisateur, autre.teleMobileUtilisateur)
				.append(this.adresseMailUtilisateur, autre.adresseMailUtilisateur)
				.append(this.motDePasse, autre.motDePasse)
				.append(this.rolesUtilisateur, autre.rolesUtilisateur)
				.append(this.isLogged, autre.isLogged)
				.append(this.suscribedMailReminder, autre.suscribedMailReminder)
				.append(this.suscribedSmsReminder, autre.suscribedSmsReminder)
				.append(this.pwdExpirationDateTime, autre.pwdExpirationDateTime)
				.append(this.compteEffacable, autre.compteEffacable)
				.build();

	}
}
