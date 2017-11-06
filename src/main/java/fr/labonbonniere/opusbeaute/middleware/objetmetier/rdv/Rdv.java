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
import fr.labonbonniere.opusbeaute.middleware.objetmetier.client.Client;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.genre.Genre;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.lieurdv.LieuRdv;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.prestations.Prestation;



@XmlRootElement
@Entity
@Table(name = "T_RDV")
public class Rdv  implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer idRdv;
	private Timestamp dateHeureDebut;
	private Timestamp dateDeSaisie;
	private Timestamp dateHeureFin;
	private Prestation prestation;
	private Genre genre;
	private Client client;
	private LieuRdv lieuRdv;



	public Rdv() {
		super();
	}

	public Rdv(Integer idRdv, String nom, String prenom,
			Timestamp dateHeureDebut,Timestamp dateHeureFin, Timestamp dateDeSaisie,
			Prestation prestation, Genre genre, Client client, 
			LieuRdv lieuRdv) {
		
		super();
		this.idRdv = idRdv;
		this.dateHeureDebut = dateHeureDebut;
		this.dateHeureFin = dateHeureFin;
		this.dateDeSaisie = dateDeSaisie;
		this.prestation = prestation;
		this.genre = genre;
		this.client = client;
		this.lieuRdv = lieuRdv;
		
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RDV_IDRDV", nullable = false, length = 8)	
	public Integer getIdRdv() {
		return idRdv;
	}

	public void setIdRdv(Integer idRdv) {
		this.idRdv = idRdv;
	}
	
	@Column(name = "RDV_DHDEBUT", nullable = false)
//	@Temporal( TemporalType.TIMESTAMP)
	public Timestamp getdateHeureDebut() {
		return dateHeureDebut;
	}
		
	public void setdateHeureDebut(Timestamp dateHeureDebut) {
		this.dateHeureDebut = dateHeureDebut;
	}
	
	@Column(name = "RDV_DHFIN", nullable = true)
//	@Temporal( TemporalType.TIMESTAMP)
	public Timestamp getdateHeureFin() {
		return dateHeureFin;
	}
	
	public void setdateHeureFin(Timestamp dateHeureFin) {
		this.dateHeureFin = dateHeureFin;
	}
	
	
	@Column(name = "RDV_DATEDESAISIE", nullable = true)
//	@Temporal( TemporalType.TIMESTAMP)
	public Timestamp getDateDeSaisie() {
		return dateDeSaisie;
	}

	public void setDateDeSaisie(Timestamp dateDeSaisie) {
		this.dateDeSaisie = dateDeSaisie;
	}
	
	
	public Prestation getPrestation() {
		return prestation;
	}

	public void setPrestation(Prestation prestation) {
		this.prestation = prestation;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public LieuRdv getLieuRdv() {
		return lieuRdv;
	}

	public void setLieuRdv(LieuRdv lieuRdv) {
		this.lieuRdv = lieuRdv;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
				.append("idRdv", this.idRdv)
				.append("dateHeureDebut", this.dateHeureDebut)
				.append("dateHeureFin", this.dateHeureFin)				
				.append("dateDeSaisie", this.dateDeSaisie)
				.append(prestation)
				.append(client)
				.append(lieuRdv)
				.build();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idRdv == null) ? 0 : idRdv.hashCode());
		result = prime * result + ((dateHeureDebut == null) ? 0 : dateHeureDebut.hashCode());
		result = prime * result + ((dateHeureFin == null) ? 0 : dateHeureFin.hashCode());	
		result = prime * result + ((dateDeSaisie == null) ? 0 : dateDeSaisie.hashCode());
		result = prime * result + ((prestation == null) ? 0 : prestation.hashCode());
		result = prime * result + ((client == null) ? 0 : client.hashCode());
		result = prime * result + ((lieuRdv == null) ? 0 : lieuRdv.hashCode());
		
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