package fr.labonbonniere.opusbeaute.middleware.objetmetier.client;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
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

import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresseclient.AdresseClient;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.genre.Genre;

@XmlRootElement
@Entity
@Table(name = "T_CLIENT")
public class Client implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer idClient;
	private String nomClient;
	private String prenomClient;
	private String telephoneClient;
	private String telMobileClient;

	private AdresseClient adresse;
	private Genre genreClient;
	private String adresseMailClient;

	private Timestamp dateAnniversaireClient;
	private String suscribedCommercials;
	private String suscribedNewsLetter;
	private String suscribedMailReminder;
	private String suscribedSmsReminder;

	public Client() {
		super();
	}

	public Client(Integer idClient, String nomClient, String prenomClient, String telephoneClient,
			String telMobileClient, Genre genreClient, AdresseClient adresse, String adresseMailClient,
			Timestamp dateAnniversaireClient, String suscribedNewsLetter, String suscribedMailReminder,
			String suscribedSmsRemider, String suscribedCommercials) {

		super();
		this.idClient = idClient;
		this.nomClient = nomClient;
		this.prenomClient = prenomClient;
		this.telephoneClient = telephoneClient;
		this.telMobileClient = telMobileClient;
		this.genreClient = genreClient;
		this.adresse = adresse;
		this.adresseMailClient = adresseMailClient;
		this.dateAnniversaireClient = dateAnniversaireClient;
		this.suscribedNewsLetter = suscribedNewsLetter;
		this.suscribedMailReminder = suscribedMailReminder;
		this.suscribedSmsReminder = suscribedSmsRemider;
		this.suscribedCommercials = suscribedCommercials;

	}

	@Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@SequenceGenerator(name = "seq", sequenceName = "CLIENT_SEQ", initialValue = 2, allocationSize = 2000)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
	@Column(name = "CLIENT_IDCLIENT")
	public Integer getIdClient() {
		return idClient;
	}

	public void setIdClient(Integer idClient) {
		this.idClient = idClient;
	}

	@Column(name = "CLIENT_NOM", nullable = false, length = 30)
	public String getNomClient() {
		return nomClient;
	}

	public void setNomClient(String nomClient) {
		this.nomClient = nomClient;
	}

	@Column(name = "CLIENT_PRENOM", nullable = false, length = 30)
	public String getPrenomClient() {
		return prenomClient;
	}

	public void setPrenomClient(String prenomClient) {
		this.prenomClient = prenomClient;
	}

	@Column(name = "CLIENT_TELEPHONE", nullable = true, length = 10)
	public String getTelephoneClient() {
		return telephoneClient;
	}

	public void setTelephoneClient(String telephoneClient) {
		this.telephoneClient = telephoneClient;
	}

	@Column(name = "CLIENT_TELMOBILECLIENT", nullable = true, length = 10)
	public String getTelMobileClient() {
		return telMobileClient;
	}

	public void setTelMobileClient(String telMobileClient) {
		this.telMobileClient = telMobileClient;
	}

	@OneToOne
	@JoinColumn(name = "CLIENT_IDGENRE_fk", referencedColumnName = "GENRE_IDGENRE", nullable = true, updatable = true, insertable = true)
	public Genre getGenreClient() {
		return genreClient;
	}

	public void setGenreClient(Genre genreClient) {
		this.genreClient = genreClient;
	}

	// @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL,
	// orphanRemoval = true)
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "CLIENT_IDADRESSE_fk", referencedColumnName = "CLIENT_IDADRESSE", nullable = true, updatable = true, insertable = true)
	public AdresseClient getAdresse() {
		return adresse;
	}

	public void setAdresse(AdresseClient adresse) {
		this.adresse = adresse;
	}

	@Column(name = "CLIENT_MAIL", nullable = true, length = 50)
	public String getAdresseMailClient() {
		return adresseMailClient;
	}

	public void setAdresseMailClient(String adresseMailClient) {
		this.adresseMailClient = adresseMailClient;
	}

	@Column(name = "CLIENT_DATEANNIVERSAIRE", nullable = true)
	public Timestamp getDateAnniversaireClient() {
		return dateAnniversaireClient;
	}

	public void setDateAnniversaireClient(Timestamp dateAnniversaireClient) {
		this.dateAnniversaireClient = dateAnniversaireClient;
	}

	@Column(name = "CLIENT_SUSCRIBEDNEWSLETTER", nullable = false, length = 4)
	public String getSuscribedNewsLetter() {
		return suscribedNewsLetter;
	}

	public void setSuscribedNewsLetter(String suscribedNewsLetter) {
		this.suscribedNewsLetter = suscribedNewsLetter;
	}

	@Column(name = "CLIENT_SUSCRIBEDMAILREMINDER", nullable = false, length = 4)
	public String getSuscribedMailReminder() {
		return suscribedMailReminder;
	}

	public void setSuscribedMailReminder(String suscribedMailReminder) {
		this.suscribedMailReminder = suscribedMailReminder;
	}

	@Column(name = "CLIENT_SUSCRIBEDSMSREMINDER", nullable = false, length = 4)
	public String getSuscribedSmsReminder() {
		return suscribedSmsReminder;
	}

	public void setSuscribedSmsReminder(String suscribedSmsReminder) {
		this.suscribedSmsReminder = suscribedSmsReminder;
	}

	@Column(name = "CLIENT_SUSCRIBEDCOMMERCIALS", nullable = false, length = 4)
	public String getSuscribedCommercials() {
		return suscribedCommercials;
	}

	public void setSuscribedCommercials(String suscribedCommercials) {
		this.suscribedCommercials = suscribedCommercials;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.JSON_STYLE).append("idClient", this.idClient)
				.append("nomClient", this.nomClient).append("prenomClient", this.prenomClient)
				.append("telephoneClient", this.telephoneClient).append("telMobileClient", this.telMobileClient)
				.append("genreClient", this.genreClient).append("adresse", this.adresse)
				.append("adresseMailClient", this.adresseMailClient)
				.append("dateAnniversaireClient", this.dateAnniversaireClient)
				.append("suscribedNewsLetter", this.suscribedNewsLetter)
				.append("suscribedMailReminder", this.suscribedMailReminder)
				.append("suscribedSmsReminder", this.suscribedSmsReminder)
				.append("suscribedCommercials", this.suscribedCommercials).build();

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((adresse == null) ? 0 : adresse.hashCode());
		result = prime * result + ((adresseMailClient == null) ? 0 : adresseMailClient.hashCode());
		result = prime * result + ((dateAnniversaireClient == null) ? 0 : dateAnniversaireClient.hashCode());
		result = prime * result + ((idClient == null) ? 0 : idClient.hashCode());
		result = prime * result + ((genreClient == null) ? 0 : genreClient.hashCode());
		result = prime * result + ((nomClient == null) ? 0 : nomClient.hashCode());
		result = prime * result + ((prenomClient == null) ? 0 : prenomClient.hashCode());
		result = prime * result + ((telephoneClient == null) ? 0 : telephoneClient.hashCode());
		result = prime * result + ((telMobileClient == null) ? 0 : telMobileClient.hashCode());
		result = prime * result + ((suscribedNewsLetter == null) ? 0 : suscribedNewsLetter.hashCode());
		result = prime * result + ((suscribedMailReminder == null) ? 0 : suscribedMailReminder.hashCode());
		result = prime * result + ((suscribedSmsReminder == null) ? 0 : suscribedSmsReminder.hashCode());
		result = prime * result + ((suscribedCommercials == null) ? 0 : suscribedCommercials.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object candidat) {
		if (candidat == this)
			return true;

		if (candidat == null)
			return false;

		if (!(candidat instanceof Client))
			return false;

		final Client autre = (Client) candidat;

		return new EqualsBuilder().append(this.idClient, autre.idClient).append(this.nomClient, autre.nomClient)
				.append(this.prenomClient, autre.prenomClient).append(this.telephoneClient, autre.telephoneClient)
				.append(this.telMobileClient, autre.telMobileClient).append(this.adresse, autre.adresse)
				.append(this.genreClient, autre.genreClient).append(this.adresseMailClient, autre.adresseMailClient)
				.append(this.dateAnniversaireClient, autre.dateAnniversaireClient)
				.append(this.suscribedNewsLetter, autre.suscribedNewsLetter)
				.append(this.suscribedMailReminder, autre.suscribedMailReminder)
				.append(this.suscribedSmsReminder, autre.suscribedSmsReminder)
				.append(this.suscribedCommercials, autre.suscribedCommercials).build();

	}

	// @PreUpdate
	// @PrePersist
	// public void avantPersist() {
	// adresse.setIdAdresse(this.getIdClient());
	// }
}
