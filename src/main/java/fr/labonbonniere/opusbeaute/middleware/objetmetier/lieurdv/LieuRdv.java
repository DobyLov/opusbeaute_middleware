package fr.labonbonniere.opusbeaute.middleware.objetmetier.lieurdv;

import java.io.Serializable;

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

import fr.labonbonniere.opusbeaute.middleware.objetmetier.adresselieurdv.AdresseLieuRdv;

/**
 * Objet Metier LieuRdv
 * 
 * Lors de l intervention de L ORM Hibernate / JPA, 
 * l instance devient une entite
 * 
 * "@Overide" des Methodes
 * 
 *          toString Equals HashCode
 * 
 * @author fred
 *
 */
@XmlRootElement
@Entity
@Table(name = "T_LIEURDV")
public class LieuRdv implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer idLieuRdv;
	private String lieuRdv;
	private AdresseLieuRdv adresseLieuRdv;

	public LieuRdv() {
		super();

	}

	public LieuRdv(Integer idLieuRdv, String lieurRdv, AdresseLieuRdv adresseLieuRdv) {

		this.idLieuRdv = idLieuRdv;
		this.lieuRdv = lieurRdv;
		this.adresseLieuRdv = adresseLieuRdv;
	}



	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@GeneratedValue(generator="increment")
//	@GenericGenerator(name="increment", strategy = "increment", generator="seq")
	@SequenceGenerator(name="seq",sequenceName="LIEURRDV_SEQ",
						initialValue = 4, allocationSize = 50)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq")
	@Column(name = "LIEURDV_IDLIEURDV", nullable = false)
	public Integer getIdLieuRdv() {
		return idLieuRdv;
	}

	public void setIdLieuRdv(Integer idLieuRdv) {
		this.idLieuRdv = idLieuRdv;
	}

	@Column(name = "LIEURDV_LIEURDV", nullable = false, length = 30)
	public String getLieuRdv() {
		return lieuRdv;
	}

	public void setLieuRdv(String lieuRdv) {
		this.lieuRdv = lieuRdv;
	}

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "LIEURDV_IDADRESSELIEURDV_fk", referencedColumnName = "ADRESSELIEURDV_IDADRESSELIEURDV", 
	nullable = true, updatable = true, insertable = true)
	public AdresseLieuRdv getAdresseLieuRdv() {
		return adresseLieuRdv;
	}

	public void setAdresseLieuRdv(AdresseLieuRdv adresseLieuRdv) {
		this.adresseLieuRdv = adresseLieuRdv;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.JSON_STYLE).append("idLieuRdv", this.idLieuRdv)
				.append("lieuRdv", this.lieuRdv).append("adresseLieuRdv", this.adresseLieuRdv).build();
	}

	@Override
	public boolean equals(Object candidat) {
		if (candidat == this)
			return true;

		if (candidat == null)
			return false;

		if (!(candidat instanceof LieuRdv))
			return false;

		final LieuRdv autre = (LieuRdv) candidat;

		return new EqualsBuilder().append(this.idLieuRdv, autre.idLieuRdv).append(this.lieuRdv, autre.lieuRdv)
				.append(this.adresseLieuRdv, autre.adresseLieuRdv).build();
	}
	
//	@PrePersist
//	public void beforeSave(){
//		adresseLieuRdv.setIdAdresseLieuRdv(idLieuRdv);
//	}

}
