package fr.labonbonniere.opusbeaute.middleware.objetmetier.lieurdv;

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
@Table( name = "T_LIEURDV")
public class LieuRdv implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Integer idLieuRdv;
	private String LieuRdv;
	
	public LieuRdv() {
	super();
	
	}

	public LieuRdv(Integer idLieuRdv, String LieurRdv) {
		
		this.idLieuRdv = idLieuRdv;
		this.LieuRdv = LieurRdv;		
		
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "LIEURDV_IDLIEURDV", nullable = false, length = 1)
	public Integer getIdLieuRdv() {
		return idLieuRdv;
	}

	public void setIdLieuRdv(Integer idLieuRdv) {
		this.idLieuRdv = idLieuRdv;
	}

	@Column(name = "LIEURDV_LIEURDV", nullable = true, length = 30)
	public String getLieuRdv() {
		return LieuRdv;
	}

	public void setLieuRdv(String lieuRdv) {
		LieuRdv = lieuRdv;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
				.append("idLieuRdv", this.LieuRdv)
				.append("LieuRdv", this.idLieuRdv)
				.build();
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
		
		return new EqualsBuilder()
				.append(this.idLieuRdv, autre.idLieuRdv)
				.append(this.LieuRdv, autre.LieuRdv)
				.build();
	}
	
	
	
}
