package fr.labonbonniere.opusbeaute.middleware.objetmetier.rgpd;

import java.sql.Timestamp;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@XmlRootElement
public class Rgpd {
	private static final long serialVersionUID = 1L;

	private Integer rgpdCliId;
	private String rgpdCliPrenom;
	private String rgpdCliEmail;
	private String rgpdCliToken;
	private String rgpdSubsComm;
	private String rgpdSubsNLetter;
	private String rgpdSubsMailRem;
	private String rgpdSubsSmsRem;
	private String rgpdInfoCliVal; // boolean T=true / f=false 
	private Timestamp rgpdDateCliVal;
	private String rgpdCliCanModifyRgpdSettings; // boolean T=true / f=false 


	public Rgpd() {
		super();
	}
	

	public Rgpd(Integer rgpdCliId, String rgpdCliPrenom, String rgpdCliEmail, String rgpdCliToken, String rgpdSubsComm,
			String rgpdSubsNLetter, String rgpdSubsMailRem, String rgpdSubsSmsRem, String rgpdInfoCliVal,
			Timestamp rgpdDateCliVal, String rgpdCliCanModifyRgpdSettings) {
		
		super();
		this.rgpdCliId = rgpdCliId;
		this.rgpdCliPrenom = rgpdCliPrenom;
		this.rgpdCliEmail = rgpdCliEmail;
		this.rgpdCliToken = rgpdCliToken;
		this.rgpdSubsComm = rgpdSubsComm;
		this.rgpdSubsNLetter = rgpdSubsNLetter;
		this.rgpdSubsMailRem = rgpdSubsMailRem;
		this.rgpdSubsSmsRem = rgpdSubsSmsRem;
		this.rgpdInfoCliVal = rgpdInfoCliVal;
		this.rgpdDateCliVal = rgpdDateCliVal;
		this.rgpdCliCanModifyRgpdSettings = rgpdCliCanModifyRgpdSettings;
		
	}

	public Integer getRgpdCliId() {
		return rgpdCliId;
	}

	public void setRgpdCliId(Integer rgpdCliId) {
		this.rgpdCliId = rgpdCliId;
	}

	public String getRgpdCliPrenom() {
		return rgpdCliPrenom;
	}

	public void setRgpdCliPrenom(String rgpdCliPrenom) {
		this.rgpdCliPrenom = rgpdCliPrenom;
	}

	public String getRgpdCliEmail() {
		return rgpdCliEmail;
	}

	public void setRgpdCliEmail(String rgpdCliEmail) {
		this.rgpdCliEmail = rgpdCliEmail;
	}

	public String getRgpdCliToken() {
		return rgpdCliToken;
	}

	public void setRgpdCliToken(String rgpdCliToken) {
		this.rgpdCliToken = rgpdCliToken;
	}

	public String getRgpdSubsComm() {
		return rgpdSubsComm;
	}

	public void setRgpdSubsComm(String rgpdSubsComm) {
		this.rgpdSubsComm = rgpdSubsComm;
	}

	public String getRgpdSubsNLetter() {
		return rgpdSubsNLetter;
	}

	public void setRgpdSubsNLetter(String rgpdSubsNLetter) {
		this.rgpdSubsNLetter = rgpdSubsNLetter;
	}

	public String getRgpdSubsMailRem() {
		return rgpdSubsMailRem;
	}

	public void setRgpdSubsMailRem(String rgpdSubsMailRem) {
		this.rgpdSubsMailRem = rgpdSubsMailRem;
	}

	public String getRgpdSubsSmsRem() {
		return rgpdSubsSmsRem;
	}

	public void setRgpdSubsSmsRem(String rgpdSubsSmsRem) {
		this.rgpdSubsSmsRem = rgpdSubsSmsRem;
	}

	public String getRgpdInfoCliVal() {
		return rgpdInfoCliVal;
	}

	public void setRgpdInfoCliVal(String rgpdInfoCliVal) {
		this.rgpdInfoCliVal = rgpdInfoCliVal;
	}

	public Timestamp getRgpdDateCliVal() {
		return rgpdDateCliVal;
	}

	public void setRgpdDateCliVal(Timestamp rgpdDateCliVal) {
		this.rgpdDateCliVal = rgpdDateCliVal;
	}

	public String getRgpdCliCanModifyRgpdSettings() {
		return rgpdCliCanModifyRgpdSettings;
	}

	public void setRgpdCliCanModifyRgpdSettings(String rgpdCliCanModifyRgpdSettings) {
		this.rgpdCliCanModifyRgpdSettings = rgpdCliCanModifyRgpdSettings;
	}
	

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
				.append("rgpdCliId", this.rgpdCliId)
				.append("rgpdCliPrenom", this.rgpdCliPrenom)
				.append("rgpdCliEmail", this.rgpdCliEmail)
				.append("rgpdCliToken", this.rgpdCliToken)
				.append("rgpdSubsComm", this.rgpdSubsComm)
				.append("rgpdSubsNLetter", this.rgpdSubsNLetter)
				.append("rgpdSubsMailRem", this.rgpdSubsMailRem)
				.append("rgpdSubsSmsRem", this.rgpdSubsSmsRem)
				.append("rgpdDateCliVal", this.rgpdDateCliVal)
				.append("rgpdCliCanModifyRgpdSettings", this.rgpdCliCanModifyRgpdSettings)
				.build();
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rgpdCliId == null) ? 0 : rgpdCliId.hashCode());
		result = prime * result + ((rgpdCliPrenom == null) ? 0 : rgpdCliPrenom.hashCode());
		result = prime * result + ((rgpdCliEmail == null) ? 0 : rgpdCliEmail.hashCode());
		result = prime * result + ((rgpdCliToken == null) ? 0 : rgpdCliToken.hashCode());
		result = prime * result + ((rgpdSubsComm == null) ? 0 : rgpdSubsComm.hashCode());
		result = prime * result + ((rgpdSubsNLetter == null) ? 0 : rgpdSubsNLetter.hashCode());
		result = prime * result + ((rgpdSubsMailRem == null) ? 0 : rgpdSubsMailRem.hashCode());
		result = prime * result + ((rgpdSubsSmsRem == null) ? 0 : rgpdSubsSmsRem.hashCode());
		result = prime * result + ((rgpdInfoCliVal == null) ? 0 : rgpdInfoCliVal.hashCode());
		result = prime * result + ((rgpdDateCliVal == null) ? 0 : rgpdDateCliVal.hashCode());
		result = prime * result + ((rgpdCliCanModifyRgpdSettings == null) ? 0 : rgpdCliCanModifyRgpdSettings.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object candidat) {
		if (candidat == this)
			return true;

		if (candidat == null)
			return false;

		if (!(candidat instanceof Rgpd))
			return false;

		final Rgpd autre = (Rgpd) candidat;

		return new EqualsBuilder()
				.append(this.rgpdCliId, autre.rgpdCliId)
				.append(this.rgpdCliPrenom, autre.rgpdCliPrenom)
				.append(this.rgpdCliEmail, autre.rgpdCliEmail)
				.append(this.rgpdCliToken, autre.rgpdCliToken)
				.append(this.rgpdSubsComm, autre.rgpdSubsComm)
				.append(this.rgpdSubsNLetter, autre.rgpdSubsNLetter)
				.append(this.rgpdSubsMailRem, autre.rgpdSubsMailRem)
				.append(this.rgpdSubsSmsRem, autre.rgpdSubsSmsRem)
				.append(this.rgpdInfoCliVal, autre.rgpdInfoCliVal)
				.append(this.rgpdDateCliVal, autre.rgpdDateCliVal)
				.append(this.rgpdCliCanModifyRgpdSettings, autre.rgpdCliCanModifyRgpdSettings)
				.build();
	}
}
