package fr.labonbonniere.opusbeaute.middleware.objetmetier.genre;

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

/**
 * Objet Metier Genre
 * 
 * Lors de l intervention de L ORM Hibernate / JPA, 
 * l instance devient une entite
 * 
 * Surcharge des Methodes
 * 
 *          toString Equals HashCode
 * 
 * @author fred
 *
 */
@XmlRootElement
@Entity
@Table(name = "T_GENRE")
public class Genre implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer idGenre;
	private String genreHum;

	public Genre() {
		super();

	}

	public Genre(Integer idGenre, String genreHum) {
		super();
		this.idGenre = idGenre;
		this.genreHum = genreHum;

	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "GENRE_IDGENRE", nullable = false, length = 1)
	public Integer getIdGenre() {
		return idGenre;
	}

	public void setIdGenre(Integer idGenre) {
		this.idGenre = idGenre;
	}

	@Column(name = "GENRE_GENREHUM", length = 11)
	public String getGenreHum() {
		return genreHum;
	}

	public void setGenreHum(String genreHum) {
		this.genreHum = genreHum;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.JSON_STYLE).append("idGenre", this.idGenre)
				.append("genreHum", this.genreHum).build();

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((genreHum == null) ? 0 : genreHum.hashCode());
		result = prime * result + ((idGenre == null) ? 0 : idGenre.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object candidat) {
		if (candidat == this)
			return true;

		if (candidat == null)
			return false;

		if (!(candidat instanceof Genre))
			return false;

		final Genre autre = (Genre) candidat;

		return new EqualsBuilder().append(this.idGenre, autre.idGenre).append(this.genreHum, autre.genreHum).build();
	}
}
