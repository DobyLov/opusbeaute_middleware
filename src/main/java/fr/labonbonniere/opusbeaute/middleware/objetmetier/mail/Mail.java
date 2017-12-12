package fr.labonbonniere.opusbeaute.middleware.objetmetier.mail;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;



public class Mail implements Serializable {

	private static final long serialVersionUID = 1L;

	private String adresseEMail;
	private String corpsEMAil;

	public Mail() {
		super();

	}

	public Mail(String adresseMail, String corpsEMAil) {
		super();

		this.adresseEMail = adresseMail;
		this.corpsEMAil = corpsEMAil;
	}

	public String getAdresseEMail() {
		return adresseEMail;
	}

	public void setAdresseEMail(String adresseEMail) {
		this.adresseEMail = adresseEMail;
	}

	public String getCorpsEMAil() {
		return corpsEMAil;
	}

	public void setCorpsEMAil(String corpsEMAil) {
		this.corpsEMAil = corpsEMAil;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.JSON_STYLE).append("adresseEMail", this.adresseEMail)
				.append("corpsEMAil", this.corpsEMAil).build();

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((adresseEMail == null) ? 0 : adresseEMail.hashCode());
		result = prime * result + ((corpsEMAil == null) ? 0 : corpsEMAil.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object candidat) {
		if (candidat == this)
			return true;

		if (candidat == null)
			return false;

		if (!(candidat instanceof Mail))
			return false;

		final Mail autre = (Mail) candidat;

		return new EqualsBuilder().append(this.adresseEMail, autre.adresseEMail)
				.append(this.corpsEMAil, autre.corpsEMAil).build();

	}
}
