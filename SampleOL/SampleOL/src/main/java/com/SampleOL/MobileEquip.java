package com.SampleOL;

public class MobileEquip {
	String MobileNumber;
	String Regiment;
	String RegimentCode;
	String Rank;
	String RankCode;
	String Name;
	String ServiceNumber;
	String MobileType;
	String JoinDate;
	String RegimCompany;
	String RegimCompanyCode;
	public MobileEquip(String mobileNumber, String regiment, String rank,  String name,
			String serviceNumber, String mobileType,String joinDate) {
		super();
		MobileNumber = mobileNumber;
		Regiment = regiment;
		Rank = rank;
		Name = name;
		ServiceNumber = serviceNumber;
		MobileType = mobileType;
		JoinDate = joinDate;

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
