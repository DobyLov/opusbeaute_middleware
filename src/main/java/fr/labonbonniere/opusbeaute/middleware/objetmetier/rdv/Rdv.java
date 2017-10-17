package fr.labonbonniere.opusbeaute.middleware.objetmetier.rdv;
import java.io.Serializable;
import java.sql.Timestamp;
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
@Table(name = "T_RDV")
public class Rdv implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer idRdv;
	private String nom;
	private String prenom;
	private Timestamp dateHeureDebut;
	private Timestamp dateDeSaisie;
	private Timestamp dateHeureFin;
	private Integer prestation;
	private Integer compteurSeances;
	private Integer produit;
	private Integer lieuRdv;
	private Integer devis;
	private Integer facture;

	public Rdv() {
		super();
	}

	public Rdv(Integer idRdv, String nom, String prenom,
			Timestamp dateHeureDebut,Timestamp dateHeureFin, Timestamp dateDeSaisie,
			Integer prestation, Integer compteurSeances, Integer produit, 
			Integer lieuRdv, Integer devis,
			Integer facture) {
		super();
		this.idRdv = idRdv;
		this.nom = nom;
		this.prenom = prenom;
		this.dateHeureDebut = dateHeureDebut;
		this.dateHeureFin = dateHeureFin;
		this.dateDeSaisie = dateDeSaisie;
		this.prestation = prestation;
		this.compteurSeances = compteurSeances;
		this.produit = produit;
		this.lieuRdv = lieuRdv;
		this.devis = devis;
		this.facture = facture;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RDV_IDRDV", nullable = false, length = 2)
	//@NotNull
	public Integer getIdRdv() {
		return idRdv;
	}

	public void setIdRdv(Integer idRdv) {
		this.idRdv = idRdv;
	}

	@Column(name = "RDV_NOM", nullable = false, length = 20)
	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
	//@Size(max = 20)
	@Column(name = "RDV_PRENOM", nullable = false, length = 20)
	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	
	@Column(name = "RDV_DHDEBUT", nullable = true)
	//@Temporal(TemporalType.TIMESTAMP)
	public Timestamp getdateHeureDebut() {
		return dateHeureDebut;
	}
		
	public void setdateHeureDebut(Timestamp dateHeureDebut) {
		this.dateHeureDebut = dateHeureDebut;
	}
	
	@Column(name = "RDV_DHFIN", nullable = true)
	//@Temporal(TemporalType.TIMESTAMP)
	public Timestamp getdateHeureFin() {
		return dateHeureFin;
	}
	
	public void setdateHeureFin(Timestamp dateHeureFin) {
		this.dateHeureFin = dateHeureFin;
	}
	
	
	@Column(name = "RDV_DATEDESAISIE", nullable = true)
	//@Temporal(TemporalType.TIMESTAMP)
	public Timestamp getDateDeSaisie() {
		return dateDeSaisie;
	}

	public void setDateDeSaisie(Timestamp dateDeSaisie) {
		this.dateDeSaisie = dateDeSaisie;
	}
	
	@Column(name = "RDV_IDPRESTATION" )
	public Integer getPrestation() {
		return prestation;
	}

	public void setPrestation(Integer prestation) {
		this.prestation = prestation;
	}
	
	@Column(name = "RDV_COMPTEURSEANCE")
	public Integer getCompteurSeances() {
		return compteurSeances;
	}

	public void setCompteurSeances(Integer compteurSeances) {
		this.compteurSeances = compteurSeances;
	}
	
	@Column(name = "RDV_IDPRODUIT")
	public Integer getProduit() {
		return produit;
	}

	public void setProduit(Integer produit) {
		this.produit = produit;
	}
	
	@Column(name = "RDV_LIEURDV")
	public Integer getLieuRdv() {
		return lieuRdv;
	}

	public void setLieuRdv(Integer lieuRdv) {
		this.lieuRdv = lieuRdv;
	}
	
	@Column(name = "RDV_DEVIS")
	public Integer getDevis() {
		return devis;
	}

	public void setDevis(Integer devis) {
		this.devis = devis;
	}
	
	@Column(name = "RDV_FACTURE")
	public Integer getFacture() {
		return facture;
	}

	public void setFacture(Integer facture) {
		this.facture = facture;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
				.append("idRdv", this.idRdv)
				.append("nom", this.nom)
				.append("prenom", this.prenom)
				.append("dateHeureDebut", this.dateHeureDebut)
				.append("dateHeureFin", this.dateHeureFin)				
				.append("dateDeSaisie", this.dateDeSaisie)
				.append(prestation)
				.append(compteurSeances)
				.append(lieuRdv)
				.append(devis)
				.append(facture)
				.build();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idRdv == null) ? 0 : idRdv.hashCode());
		result = prime * result + ((nom == null) ? 0 : nom.hashCode());
		result = prime * result + ((prenom == null) ? 0 : prenom.hashCode());
		result = prime * result + ((dateHeureDebut == null) ? 0 : dateHeureDebut.hashCode());
		result = prime * result + ((dateHeureFin == null) ? 0 : dateHeureFin.hashCode());	
		result = prime * result + ((dateDeSaisie == null) ? 0 : dateDeSaisie.hashCode());
		result = prime * result + ((prestation == null) ? 0 : prestation.hashCode());
		result = prime * result + ((compteurSeances == null) ? 0 : compteurSeances.hashCode());
		result = prime * result + ((produit == null) ? 0 : produit.hashCode());
		result = prime * result + ((lieuRdv == null) ? 0 : lieuRdv.hashCode());
		result = prime * result + ((devis == null) ? 0 : devis.hashCode());
		result = prime * result + ((facture == null) ? 0 : facture.hashCode());
		
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
		Rdv other = (Rdv) obj;
		if (idRdv == null) {
			if (other.idRdv != null)
				return false;
		} else if (!idRdv.equals(other.idRdv))
			return false;
		return true;
	}
}