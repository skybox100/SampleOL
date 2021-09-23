package com.SampleOL;

public class MobileEquip {
	String MobileNumber;
	String Regiment;
	String RegimentCode;
	String MobileType;
	String Name;
	String ServiceNumber;
	String JoinDate;
	String RegimCompany;
	String RegimCompanyCode;
	String Rank;
	String RankCode;
	public MobileEquip(String mobileNumber, String regiment, String regimentCode, String mobileType, String name,
			String serviceNumber, String joinDate, String regimCompany, String regimCompanyCode, String rank,
			String rankCode) {
		super();
		MobileNumber = mobileNumber;
		Regiment = regiment;
		RegimentCode = regimentCode;
		MobileType = mobileType;
		Name = name;
		ServiceNumber = serviceNumber;
		JoinDate = joinDate;
		RegimCompany = regimCompany;
		RegimCompanyCode = regimCompanyCode;
		Rank = rank;
		RankCode = rankCode;
	}
	public String getMobileNumber() {
		return MobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		MobileNumber = mobileNumber;
	}
	public String getRegiment() {
		return Regiment;
	}
	public void setRegiment(String regiment) {
		Regiment = regiment;
	}
	public String getRegimentCode() {
		return RegimentCode;
	}
	public void setRegimentCode(String regimentCode) {
		RegimentCode = regimentCode;
	}
	public String getMobileType() {
		return MobileType;
	}
	public void setMobileType(String mobileType) {
		MobileType = mobileType;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getServiceNumber() {
		return ServiceNumber;
	}
	public void setServiceNumber(String serviceNumber) {
		ServiceNumber = serviceNumber;
	}
	public String getJoinDate() {
		return JoinDate;
	}
	public void setJoinDate(String joinDate) {
		JoinDate = joinDate;
	}
	public String getRegimCompany() {
		return RegimCompany;
	}
	public void setRegimCompany(String regimCompany) {
		RegimCompany = regimCompany;
	}
	public String getRegimCompanyCode() {
		return RegimCompanyCode;
	}
	public void setRegimCompanyCode(String regimCompanyCode) {
		RegimCompanyCode = regimCompanyCode;
	}
	public String getRank() {
		return Rank;
	}
	public void setRank(String rank) {
		Rank = rank;
	}
	public String getRankCode() {
		return RankCode;
	}
	public void setRankCode(String rankCode) {
		RankCode = rankCode;
	}
	
	
	
	
}
