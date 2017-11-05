package fr.labonbonniere.opusbeaute.middleware.objetmetier.genre;

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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Genre other = (Genre) obj;
		if (genre == null) {
			if (other.genre != null)
				return false;
		} else if (!genre.equals(other.genre))
			return false;
		if (idGenre == null) {
			if (other.idGenre != null)
				return false;
		} else if (!idGenre.equals(other.idGenre))
			return false;
		return true;
	}

}
