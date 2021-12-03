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
	String ModelnAME;
	String ManufacturerName;
	String Remark;

	public MobileEquip(String mobileNumber, String regiment,  String name,
			String serviceNumber, String mobileType,String joinDate,String modelnAME,String manufacturerName, String remark) {
		super();
		MobileNumber = mobileNumber;
		Regiment = regiment;
		Name = name;
		ServiceNumber = serviceNumber;
		MobileType = mobileType;
		JoinDate = joinDate;
		ModelnAME = modelnAME;
		ManufacturerName=manufacturerName;
		Remark=remark;
	}
	
	public MobileEquip(String mobileNumber, String regiment, String regimentcode, String regimCompany,String regimCompanyCode,  String name,
			String serviceNumber, String mobileType,String joinDate,String modelnAME,String manufacturerName, String remark) {
		super();
		MobileNumber = mobileNumber;
		Regiment = regiment;
		RegimentCode = regimentcode;
		RegimCompany = regimCompany;
		RegimCompanyCode = regimCompanyCode;
		Name = name;
		ServiceNumber = serviceNumber;
		MobileType = mobileType;
		JoinDate = joinDate;
		ModelnAME = modelnAME;
		ManufacturerName=manufacturerName;
		Remark=remark;
	}
	
	public MobileEquip(String mobileNumber, String regiment, String regimentcode, String regimCompany,String regimCompanyCode,  String name,
			String serviceNumber, String mobileType,String joinDate,String modelnAME,String manufacturerName, String remark, String rank,String rankCode) {
		super();
		MobileNumber = mobileNumber;
		Regiment = regiment;
		RegimentCode = regimentcode;
		RegimCompany = regimCompany;
		RegimCompanyCode = regimCompanyCode;
		Name = name;
		ServiceNumber = serviceNumber;
		MobileType = mobileType;
		JoinDate = joinDate;
		ModelnAME = modelnAME;
		ManufacturerName=manufacturerName;
		Remark=remark;
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
	public String getModelnAME() {
		return ModelnAME;
	}
	public void setModelnAME(String modelnAME) {
		ModelnAME = modelnAME;
	}
	public String getManufacturerName() {
		return ManufacturerName;
	}
	public void setManufacturerName(String manufacturerName) {
		ManufacturerName = manufacturerName;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}
	
	
	
	
}
