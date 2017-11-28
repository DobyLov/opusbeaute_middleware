package fr.labonbonniere.opusbeaute.middleware.objetmetier.adresse;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
//	private Client client;
	private String numero;
	private String rue;
	private Integer zipCode;
	private String ville;
	private String pays;

	public Adresse() {
		super();
	}

	public Adresse(Integer idAdresse,String numero, String rue, Integer zipCode, String ville,
			String pays) {

		this.idAdresse = idAdresse;
//		this.client = client;
		this.numero = numero;
		this.rue = rue;
		this.zipCode = zipCode;
		this.ville = ville;
		this.pays = pays;

	}

	// declare le champs/propriete comme clee primaire
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Basic(optional = false)
//	// defini le nom de la colonne et autre params 
	@Column(name = "CLIENT_IDADRESSE", unique = true, nullable = false)
//	// Indique que la la gestion de lid et faite par un un generateur
//	@GeneratedValue(generator = "gen")
//	// Indique que l id de l entite T_CLIENT sera reporté sur l id de l entite T_ADRESSE
//	@GenericGenerator(name = "gen", strategy = "foreign",
//						parameters = @Parameter (name ="property", value = "CLIENT_IDCLIENT"))	
	public Integer getIdAdresse() {
		return idAdresse;
	}

	public void setIdAdresse(Integer idAdresse) {
		this.idAdresse = idAdresse;
	}

	
//	public Client getClient() {
//		return client;
//	}
//
//	public void setClient(Client client) {
//		this.client = client;
//	}

	@Column(name = "ADRESSE_NUMERO", nullable = true, length = 3)
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Column(name = "ADRESSE_RUE", nullable = true, length = 100)
	public String getRue() {
		return rue;
	}

	public void setRue(String rue) {
		this.rue = rue;
	}

	@Column(name = "ADRESSE_ZIPCODE", nullable = true, length = 10)
	public Integer getZipCode() {
		return zipCode;
	}

	public void setZipCode(Integer zipCode) {
		this.zipCode = zipCode;
	}

	@Column(name = "ADRESSE_VILLE", nullable = true, length = 20)
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
//				.append("client", this.client)
				.append("numero", this.numero).append("rue", this.rue)
				.append("zipCode", this.zipCode).append("ville", this.ville).append("pays", this.pays).build();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
//		result = prime * result + ((client == null) ? 0 : client.hashCode());
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
//				.append(this.client, autre.client)
				.append(this.numero, autre.numero).append(this.rue, autre.rue).append(this.ville, autre.ville)
				.append(this.zipCode, autre.zipCode).append(this.pays, autre.pays).build();
	}
//	@PrePersist
//	public void avantSauvegarde() {
//		this.idAdresse = client.getIdClient();
//	}
}
