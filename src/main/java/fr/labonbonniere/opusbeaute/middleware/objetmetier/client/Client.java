package fr.labonbonniere.opusbeaute.middleware.objetmetier.client;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresse.Adresse;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.genre.Genre;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.methodeprospection.MethodeProspection;


@XmlRootElement
@Entity
@Table(name = "T_CLIENT")
public class Client implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idClient;
	private String nomClient;
	private String prenomClient;
	private String telephoneClient;
	private Adresse adresse;
	private Genre genreClient; 		// table Client mappee en OneTone
	private String adresseMailClient;	// table Genre mappee en OneTone
	private Date dateAnniversaireClient;
	private MethodeProspection methodeProspection;
	private String parrainDe;
	private Boolean abonneNewsLetter;

	public Client() {
		super();
	}

	public Client(Long idClient, String nomClient, String prenomClient, 
			String telephoneClient, Genre genreClient, Adresse adresse, 
			String adresseMailClient, Date dateAnniversaireClient,
			MethodeProspection methodeProspection, String parrainDe, boolean abonneNewsLetter) {

		super();
		this.idClient = idClient;
		this.nomClient = nomClient;
		this.prenomClient = prenomClient;
		this.telephoneClient = telephoneClient;
		this.genreClient = genreClient;
		this.adresse = adresse;
		this.adresseMailClient = adresseMailClient;
		this.dateAnniversaireClient = dateAnniversaireClient;
		this.methodeProspection = methodeProspection;
		this.parrainDe = parrainDe;
		this.abonneNewsLetter = abonneNewsLetter;

	}

	@Id
	@Basic(optional = false)	
	@Column(name = "CLIENT_IDCLIENT")
	@GeneratedValue(generator ="gen")
	@GenericGenerator(name = "gen", strategy = "foreign",
	parameters=@Parameter(name = "property", value = "adresse"))
	public Long getIdClient() {
		return idClient;
	}

	public void setIdClient(Long idClient) {
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

	@Column(name = "CLIENT_TELEPHONE", nullable = false, length = 30)
	public String getTelephoneClient() {
		return telephoneClient;
	}
	
	public void setTelephoneClient(String telephoneClient) {
		this.telephoneClient = telephoneClient;
	}
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="GENRE_IDGENRE", foreignKey = @ForeignKey(name = "CLIENT_IDGENRE_FK"))
	public Genre getGenreClient() {
		return genreClient;
	}

	public void setGenreClient(Genre genreClient) {
		this.genreClient = genreClient;
	}

	// Declaration d'une Relation O2O sur id partagé avec adresse (cascade) 
	@JoinColumn(name = "CLIENT_IDADRESSE", referencedColumnName = "ADRESSE_IDADRESSE",
			insertable = false, updatable = false,
			foreignKey = @ForeignKey(name = "CLIENT_IDADRESSE_FK"))
	@OneToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.LAZY)	
	public Adresse getAdresse() {
		return adresse;
	}

	public void setAdresse(Adresse adresse) {
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
	@Temporal(TemporalType.DATE)
	public Date getDateAnniversaireClient() {
		return dateAnniversaireClient;
	}

	public void setDateAnniversaireClient(Date dateAnniversaireClient) {
		this.dateAnniversaireClient = dateAnniversaireClient;
	}
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "METHODEPROSPECTION_IDMETHODEPROSPECTION",foreignKey = @ForeignKey(name = "CLIENT_IDMETHODEPROSPECTION_FK"))
	public MethodeProspection getMethodeProspection() {
		return methodeProspection;
	}

	public void setMethodeProspection(MethodeProspection methodeProspection) {
		this.methodeProspection = methodeProspection;
	}

	@Column(name = "CLIENT_PARAINDE", nullable = true, length = 30)
	public String getParrainDe() {
		return parrainDe;
	}

	public void setParrainDe(String parrainDe) {
		this.parrainDe = parrainDe;
	}

	@Column(name = "CLIENT_ABONNENEWSLETTER", nullable = true)
	public Boolean getAbonneNewsLetter() {
		return abonneNewsLetter;
	}

	public void setAbonneNewsLetter(Boolean abonneNewsLetter) {
		this.abonneNewsLetter = abonneNewsLetter;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.JSON_STYLE).append("idClient", this.idClient)
				.append("nomClient", this.nomClient)
				.append("prenomClient", this.prenomClient)
				.append("telephoneClient", this.telephoneClient)
				.append("genreClient", this.genreClient)
				.append("adresse", this.adresse)
				.append("adresseMailClient", this.adresseMailClient)
				.append("dateAnniversaireClient", this.dateAnniversaireClient)
				.append("methodeProspection", this.methodeProspection)
				.append("parrainDe", this.parrainDe)
				.append("abonneNewsLetter", this.abonneNewsLetter)
				.build();

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
		result = prime * result + ((methodeProspection == null) ? 0 : methodeProspection.hashCode());
		result = prime * result + ((nomClient == null) ? 0 : nomClient.hashCode());
		result = prime * result + ((parrainDe == null) ? 0 : parrainDe.hashCode());
		result = prime * result + ((prenomClient == null) ? 0 : prenomClient.hashCode());
		result = prime * result + ((telephoneClient == null) ? 0 : telephoneClient.hashCode());
		result = prime * result + ((abonneNewsLetter == null) ? 0 : abonneNewsLetter.hashCode());		
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
		Client other = (Client) obj;
		if (adresse == null) {
			if (other.adresse != null)
				return false;
		} else if (!adresse.equals(other.adresse))
			return false;
		if (genreClient == null) {
			if (other.genreClient != null)
				return false;
		} else if (!genreClient.equals(other.genreClient))
			return false;
		if (adresseMailClient == null) {
			if (other.adresseMailClient != null)
				return false;
		} else if (!adresseMailClient.equals(other.adresseMailClient))
			return false;
		if (dateAnniversaireClient == null) {
			if (other.dateAnniversaireClient != null)
				return false;
		} else if (!dateAnniversaireClient.equals(other.dateAnniversaireClient))
			return false;
		if (idClient == null) {
			if (other.idClient != null)
				return false;
		} else if (!idClient.equals(other.idClient))
			return false;
		if (methodeProspection == null) {
			if (other.methodeProspection != null)
				return false;
		} else if (!methodeProspection.equals(other.methodeProspection))
			return false;
		if (nomClient == null) {
			if (other.nomClient != null)
				return false;
		} else if (!nomClient.equals(other.nomClient))
			return false;
		if (parrainDe == null) {
			if (other.parrainDe != null)
				return false;
		} else if (!parrainDe.equals(other.parrainDe))
			return false;
		if (prenomClient == null) {
			if (other.prenomClient != null)
				return false;
		} else if (!prenomClient.equals(other.prenomClient))
			return false;
		if (telephoneClient == null) {
			if (other.telephoneClient != null)
				return false;
		} else if (!telephoneClient.equals(other.telephoneClient))
			return false;
		if (abonneNewsLetter == null) {
			if (other.abonneNewsLetter != null)
				return false;
		} else if (!abonneNewsLetter.equals(other.abonneNewsLetter))
			return false;
		return true;
	}

}
