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


@XmlRootElement
@Entity
@Table(name = "T_GENRE")
public class Genre implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer idGenre;
	private String genre;

	public Genre() {
		super();

	}

	public Genre(Integer idGenre, String genre) {

		this.idGenre = idGenre;
		this.genre = genre;

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

	@Column(name = "GENRE_GENRE", nullable = true, length = 5)
	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.JSON_STYLE).append("idGenre", this.idGenre)
				.append("genre", this.genre).build();

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((genre == null) ? 0 : genre.hashCode());
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
		
		return new EqualsBuilder()
				.append(this.idGenre, autre.idGenre)
				.append(this.genre, autre.genre)
				.build();
	}
}
