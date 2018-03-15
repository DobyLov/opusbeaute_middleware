package fr.labonbonniere.opusbeaute.middleware.objetmetier.prestations;

import java.io.Serializable;
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

import fr.labonbonniere.opusbeaute.middleware.objetmetier.genre.Genre;
/**
 * Objet Metier Prestation
 * 
 * Lors de l intervention de L ORM Hibernate / JPA, 
 * l instance devient une entite
 * 
 * "@Override" des Methodes
 * 
 *          toString Equals HashCode
 * 
 * @author fred
 *
 */
@XmlRootElement
@Entity
@Table(name = "T_PRESTATION")
public class Prestation implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer idPrestation;
	private String activite;
	private String soin;
	private Genre genre;
	private String forfait;
	private Integer nbSeance; 
	private Integer dureeSeance;
	private float prix;
	private String description;


	public Prestation() {
		super();
	}
	
	public Prestation( Integer idPrestation, String activite, 
			String soin, Genre genre, String forfait, Integer nbSeance,
			Integer dureeSeance, float prix, String description) {
		
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
//	@GeneratedValue( strategy = GenerationType.IDENTITY)
	@SequenceGenerator(name="seq",sequenceName="PRESTATION_SEQ",
	initialValue = 135, allocationSize = 300)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq")
	@Column(name = "PRESTATION_IDPRESTATION", nullable = false, length = 3)
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
	
	@OneToOne
	@JoinColumn(name = "PRESTATION_IDGENRE_fk", referencedColumnName = "GENRE_IDGENRE", 
	nullable = true, updatable = true, insertable = true)
	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}
	@Column(name = "PRESTATION_FORFAIT", nullable = false, length = 3)
	public String getForfait() {
		return forfait;
	}

	public void setForfait(String forfait) {
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
	public float getPrix() {
		return prix;
	}

	public void setPrix(float prix) {
		this.prix = prix;
	}
	@Column(name = "PRESTATION_DESCRIPTION", nullable = true, length = 500 )
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this,  ToStringStyle.JSON_STYLE)
				.append("idPrestation", this.idPrestation)
				.append("activite", this.activite)
				.append("soin", this.soin)
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
		result = prime * result + ((soin == null) ? 0 : soin.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object candidat) {
		if (candidat == this)
			return true;
		
		if (candidat == null)
			return false;
		
		if (!(candidat instanceof Prestation))
			return false;
		
		final Prestation autre = (Prestation) candidat; 
		
		return new EqualsBuilder()
				.append(this.idPrestation, autre.idPrestation)
				.append(this.activite, autre.activite)
				.append(this.description, autre.description)
				.append(this.dureeSeance, autre.dureeSeance)
				.append(this.forfait, autre.forfait)
				.append(this.genre, autre.genre)
				.append(this.nbSeance, autre.nbSeance)
				.append(this.soin, autre.soin)
				.build();
	}
}
