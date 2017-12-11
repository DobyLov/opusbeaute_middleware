package fr.labonbonniere.opusbeaute.middleware.objetmetier.rdv;

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

import fr.labonbonniere.opusbeaute.middleware.objetmetier.client.Client;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.lieurdv.LieuRdv;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.praticien.Praticien;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.prestations.Prestation;
import fr.labonbonniere.opusbeaute.middleware.objetmetier.utilisateurs.Utilisateur;

@XmlRootElement
@Entity
@Table(name = "T_RDV")
public class Rdv implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer idRdv;
	private Timestamp dateHeureDebut;
	private Timestamp dateDeSaisie;
	private Timestamp dateHeureFin;
	private Prestation prestation;
	private Praticien praticien;
	private Client client;
	private LieuRdv lieuRdv;
	private Utilisateur utilisateur;

	public Rdv() {
		super();
	}

	public Rdv(Integer idRdv, String nom, String prenom, Timestamp dateHeureDebut, 
			Timestamp dateHeureFin,	Timestamp dateDeSaisie, Prestation prestation, 
			Praticien praticien, Client client, LieuRdv lieuRdv, Utilisateur utilisateur) {

		super();
		this.idRdv = idRdv;
		this.dateHeureDebut = dateHeureDebut;
		this.dateHeureFin = dateHeureFin;
		this.dateDeSaisie = dateDeSaisie;
		this.prestation = prestation;
		this.praticien = praticien;
		this.client = client;
		this.lieuRdv = lieuRdv;
		this.utilisateur = utilisateur;

	}

	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@SequenceGenerator(name="seq",sequenceName="RDV_SEQ",
	initialValue = 2, allocationSize = 99999)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq")
	@Column(name = "RDV_IDRDV", nullable = false, length = 8)
	public Integer getIdRdv() {
		return idRdv;
	}

	public void setIdRdv(Integer idRdv) {
		this.idRdv = idRdv;
	}

	@Column(name = "RDV_DHDEBUT", nullable = false)
	public Timestamp getDateHeureDebut() {
		return dateHeureDebut;
	}

	public void setDateHeureDebut(Timestamp dateHeureDebut) {
		this.dateHeureDebut = dateHeureDebut;
	}

	@Column(name = "RDV_DHFIN", nullable = true)
	public Timestamp getDateHeureFin() {
		return dateHeureFin;
	}

	public void setDateHeureFin(Timestamp dateHeureFin) {
		this.dateHeureFin = dateHeureFin;
	}

	@Column(name = "RDV_DATEDESAISIE", nullable = true)
	public Timestamp getDateDeSaisie() {
		return dateDeSaisie;
	}

	public void setDateDeSaisie(Timestamp dateDeSaisie) {
		this.dateDeSaisie = dateDeSaisie;
	}

	@OneToOne
	@JoinColumn(name = "RDV_IDPRESTATION_fk", referencedColumnName = "PRESTATION_IDPRESTATION", nullable = true, updatable = true, insertable = true)
	public Prestation getPrestation() {
		return prestation;
	}

	public void setPrestation(Prestation prestation) {
		this.prestation = prestation;
	}

	@OneToOne
	@JoinColumn(name = "RDV_IDPRATICIEN_fk", referencedColumnName = "PRATICIEN_IDPRATICIEN", nullable = true, updatable = true, insertable = true)
	public Praticien getPraticien() {
		return praticien;
	}

	public void setPraticien(Praticien praticien) {
		this.praticien = praticien;
	}

	@OneToOne
	@JoinColumn(name = "RDV_IDCLIENT_fk", referencedColumnName = "CLIENT_IDCLIENT", 
		nullable = true, updatable = true, insertable = true)
	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	@OneToOne
	@JoinColumn(name = "RDV_IDLIEURDV_fk", referencedColumnName = "LIEURDV_IDLIEURDV", nullable = true, updatable = true, insertable = true)
	public LieuRdv getLieuRdv() {
		return lieuRdv;
	}

	public void setLieuRdv(LieuRdv lieuRdv) {
		this.lieuRdv = lieuRdv;
	}

	@OneToOne
	@JoinColumn(name = "RDV_SAISIPAR_IDUTILISATEUR_fk", referencedColumnName = "UTILISATEUR_IDUTILISATEUR", nullable = true, updatable = true, insertable = true)
	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
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
				.append(praticien)
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
		result = prime * result + ((praticien == null) ? 0 : praticien.hashCode());
		result = prime * result + ((client == null) ? 0 : client.hashCode());
		result = prime * result + ((lieuRdv == null) ? 0 : lieuRdv.hashCode());

		return result;
	}

	@Override
	public boolean equals(Object candidat) {
		if (candidat == this)
			return true;

		if (candidat == null)
			return false;

		if (!(candidat instanceof Rdv))
			return false;

		final Rdv autre = (Rdv) candidat;

		return new EqualsBuilder()
				.append(this.idRdv, autre.idRdv)
				.append(this.dateHeureDebut, autre.dateHeureDebut)
				.append(this.dateDeSaisie, autre.dateDeSaisie)
				.append(this.dateHeureFin, autre.dateHeureFin)
				.append(this.prestation, autre.prestation)
				.append(this.praticien, autre.praticien)
				.append(this.lieuRdv, autre.lieuRdv)
				.append(this.client, autre.client)
				.build();
	}
}