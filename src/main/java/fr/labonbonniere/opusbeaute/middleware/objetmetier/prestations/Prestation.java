package fr.labonbonniere.opusbeaute.middleware.objetmetier.prestations;

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
@Table(name = "T_PRESTATION")
public class Prestation implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer idPrestation;
	private String activite;
	private String soin;
	private String genre;
	private Boolean forfait;
	private Integer nbSeance; 
	private Integer dureeSeance;
	private Integer prix;
	private String description;


	public Prestation() {
		super();
	}
	
	public Prestation( Integer idPrestation, String activite, 
			String soin, String genre, boolean forfait, Integer nbSeance,
			Integer dureeSeance, Integer prix, String description) {
		
		super();
		this.idPrestation = idPrestation;
		this.activite = activite;
		this.soin = soin;
		this.genre = genre;
		this.forfait = forfait;
		this.nbSeance = nbSeance;
		this.dureeSeance = dureeSeance;
		this.prix = prix;
		this.description = description;
	}
	
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY)
	@Column(name = "PRESTATION_IDPRESTAION", nullable = false, length = 3)
	public Integer getIdPrestation() {
		return idPrestation;
	}

	public void setIdPrestation(Integer idPrestation) {
		this.idPrestation = idPrestation;
	}

	@Column(name = "PRESTATION_ACTIVITE", nullable = false, length = 50)
	public String getActivite() {
		return activite;
	}

	public void setActivite(String activite) {
		this.activite = activite;
	}
	@Column(name = "PRESTATION_SOIN", nullable = false, length = 50)
	public String getSoin() {
		return soin;
	}

	public void setSoin(String soin) {
		this.soin = soin;
	}
	@Column(name = "PRESTATION_GENRE", nullable = true, length = 10)
	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}
	@Column(name = "PRESTATION_FORFAIT", nullable = false, length = 3)
	public Boolean getForfait() {
		return forfait;
	}

	public void setForfait(Boolean forfait) {
		this.forfait = forfait;
	}
	@Column(name = "PRESTATION_NBSEANCE", nullable = false, length = 3)
	public Integer getNbSeance() {
		return nbSeance;
	}

	public void setNbSeance(Integer nbSeance) {
		this.nbSeance = nbSeance;
	}
	@Column(name = "PRESTATION_DUREESEANCE", nullable = false, length = 3)
	public Integer getDureeSeance() {
		return dureeSeance;
	}

	public void setDureeSeance(Integer dureeSeance) {
		this.dureeSeance = dureeSeance;
	}

	@Column(name = "PRESTATION_PRIX", nullable = true, length = 3)
	public Integer getPrix() {
		return prix;
	}

	public void setPrix(Integer prix) {
		this.prix = prix;
	}
	@Column(name = "PRESTATION_DESCRIPTION", nullable = true, length = 500 )
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this,  ToStringStyle.JSON_STYLE)
				.append("idPrestation", this.idPrestation)
				.append("activite", this.activite)
				.append("soin=", this.soin)
				.append("genre", this.genre)
				.append("forfait", this.forfait)
				.append("nsSeance",this.nbSeance)
				.append("dureeSeance",this.dureeSeance)
				.append("prix", this.prix)
				.append("description", this.description)
				.build();
	
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((activite == null) ? 0 : activite.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((dureeSeance == null) ? 0 : dureeSeance.hashCode());
		result = prime * result + ((forfait == null) ? 0 : forfait.hashCode());
		result = prime * result + ((genre == null) ? 0 : genre.hashCode());
		result = prime * result + ((idPrestation == null) ? 0 : idPrestation.hashCode());
		result = prime * result + ((nbSeance == null) ? 0 : nbSeance.hashCode());
		result = prime * result + ((prix == null) ? 0 : prix.hashCode());
		result = prime * result + ((soin == null) ? 0 : soin.hashCode());
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
		Prestation other = (Prestation) obj;
		if (activite == null) {
			if (other.activite != null)
				return false;
		} else if (!activite.equals(other.activite))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (dureeSeance == null) {
			if (other.dureeSeance != null)
				return false;
		} else if (!dureeSeance.equals(other.dureeSeance))
			return false;
		if (forfait == null) {
			if (other.forfait != null)
				return false;
		} else if (!forfait.equals(other.forfait))
			return false;
		if (genre == null) {
			if (other.genre != null)
				return false;
		} else if (!genre.equals(other.genre))
			return false;
		if (idPrestation == null) {
			if (other.idPrestation != null)
				return false;
		} else if (!idPrestation.equals(other.idPrestation))
			return false;
		if (nbSeance == null) {
			if (other.nbSeance != null)
				return false;
		} else if (!nbSeance.equals(other.nbSeance))
			return false;
		if (prix == null) {
			if (other.prix != null)
				return false;
		} else if (!prix.equals(other.prix))
			return false;
		if (soin == null) {
			if (other.soin != null)
				return false;
		} else if (!soin.equals(other.soin))
			return false;
		return true;
	}

}
