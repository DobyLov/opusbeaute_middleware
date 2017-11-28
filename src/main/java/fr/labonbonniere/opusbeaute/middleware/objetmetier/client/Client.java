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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresse.Adresse;


@XmlRootElement
@Entity
@Table(name = "T_CLIENT")
public class Client implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer idClient;
	private String nomClient;
	private String prenomClient;
	private String telephoneClient;
	private Adresse adresse;
//	private Genre genreClient; // table Client mappee en OneTone
	private String adresseMailClient; // table Genre mappee en OneTone
	private Timestamp dateAnniversaireClient;

	public Client() {
		super();
	}

	public Client(Integer idClient, String nomClient, String prenomClient, String telephoneClient,
			Adresse adresse, String adresseMailClient, Timestamp dateAnniversaireClient) {

		super();
		this.idClient = idClient;
		this.nomClient = nomClient;
		this.prenomClient = prenomClient;
		this.telephoneClient = telephoneClient;
//		this.genreClient = genreClient;
		this.adresse = adresse;
		this.adresseMailClient = adresseMailClient;
		this.dateAnniversaireClient = dateAnniversaireClient;

	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	@Column(name = "CLIENT_TELEPHONE", nullable = false, length = 30)
	public String getTelephoneClient() {
		return telephoneClient;
	}

	public void setTelephoneClient(String telephoneClient) {
		this.telephoneClient = telephoneClient;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "CLIENT_IDADRESSE_fk",
				referencedColumnName = "CLIENT_IDADRESSE",
//				nullable= false,
				insertable = false,
				updatable = false)
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
	public Timestamp getDateAnniversaireClient() {
		return dateAnniversaireClient;
	}

	public void setDateAnniversaireClient(Timestamp dateAnniversaireClient) {
		this.dateAnniversaireClient = dateAnniversaireClient;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.JSON_STYLE).append("idClient", this.idClient)
				.append("nomClient", this.nomClient).append("prenomClient", this.prenomClient)
				.append("telephoneClient", this.telephoneClient)
//				.append("genreClient", this.genreClient)
				.append("adresse", this.adresse)
				.append("adresseMailClient", this.adresseMailClient)
				.append("dateAnniversaireClient", this.dateAnniversaireClient).build();

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((adresse == null) ? 0 : adresse.hashCode());
		result = prime * result + ((adresseMailClient == null) ? 0 : adresseMailClient.hashCode());
		result = prime * result + ((dateAnniversaireClient == null) ? 0 : dateAnniversaireClient.hashCode());
		result = prime * result + ((idClient == null) ? 0 : idClient.hashCode());
//		result = prime * result + ((genreClient == null) ? 0 : genreClient.hashCode());
		result = prime * result + ((nomClient == null) ? 0 : nomClient.hashCode());
		result = prime * result + ((prenomClient == null) ? 0 : prenomClient.hashCode());
		result = prime * result + ((telephoneClient == null) ? 0 : telephoneClient.hashCode());
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

		return new EqualsBuilder()
				.append(this.idClient, autre.idClient)
				.append(this.nomClient, autre.nomClient)
				.append(this.prenomClient, autre.prenomClient)
				.append(this.telephoneClient, autre.telephoneClient)
				.append(this.adresse, autre.adresse)
//				.append(this.genreClient, autre.genreClient)
				.append(this.adresseMailClient, autre.adresseMailClient)
				.append(this.dateAnniversaireClient, autre.dateAnniversaireClient)
				.build();

	}

}
