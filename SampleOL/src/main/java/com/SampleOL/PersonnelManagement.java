package com.SampleOL;

public class PersonnelManagement {

	String ServiceNumber;
	String MissionType;
	String Rank;
	String Name;
	String Regiment;
	String RegimCompany;
	String MOS;
	String Duty;
	String HelpCare;
	String BirthDate;
	String JoinDate;
	String PromotionDate;
	String MovingDate;
	String RetireDate;
	String MobileNumber;
	String MyPhoneNumber;
	String ParentsNumber;
	String Remark;
	String Picture;
	String Password;
	String RegimPlatoon;
	String RegimSquad;
	String LeaderType;
	String BloodType;
	String Goout;
	String Reserve01;
	String Reserve02;
	String Reserve03;
	String Reserve04;
	
	
	
	@Override
	public String toString() {
		return "PersonnelManagement [ServiceNumber=" + ServiceNumber + ", MissionType=" + MissionType + ", Rank=" + Rank
				+ ", Name=" + Name + ", Regiment=" + Regiment + ", RegimCompany=" + RegimCompany + ", MOS=" + MOS
				+ ", Duty=" + Duty + ", HelpCare=" + HelpCare + ", BirthDate=" + BirthDate + ", JoinDate=" + JoinDate
				+ ", PromotionDate=" + PromotionDate + ", MovingDate=" + MovingDate + ", RetireDate=" + RetireDate
				+ ", MobileNumber=" + MobileNumber + ", MyPhoneNumber=" + MyPhoneNumber + ", ParentsNumber="
				+ ParentsNumber + ", Remark=" + Remark + ", Picture=" + Picture + ", Password=" + Password
				+ ", RegimPlatoon=" + RegimPlatoon + ", RegimSquad=" + RegimSquad + ", LeaderType=" + LeaderType
				+ ", BloodType=" + BloodType + ", Goout=" + Goout + ", Reserve01=" + Reserve01 + ", Reserve02="
				+ Reserve02 + ", Reserve03=" + Reserve03 + ", Reserve04=" + Reserve04 + "]";
	}
	
	
	
	public PersonnelManagement(String serviceNumber, String missionType, String rank, String name, String regiment,
			String regimCompany, String mOS, String duty, String helpCare, String birthDate, String joinDate,
			String promotionDate, String movingDate, String retireDate, String mobileNumber, String myPhoneNumber,
			String parentsNumber, String remark, String picture, String password, String regimPlatoon, String regimSquad,
			String leaderType, String bloodType, String goout, String reserve01, String reserve02, String reserve03,
			String reserve04) {
		super();
		ServiceNumber = serviceNumber;
		MissionType = missionType;
		Rank = rank;
		Name = name;
		Regiment = regiment;
		RegimCompany = regimCompany;
		MOS = mOS;
		Duty = duty;
		HelpCare = helpCare;
		BirthDate = birthDate;
		JoinDate = joinDate;
		PromotionDate = promotionDate;
		MovingDate = movingDate;
		RetireDate = retireDate;
		MobileNumber = mobileNumber;
		MyPhoneNumber = myPhoneNumber;
		ParentsNumber = parentsNumber;
		Remark = remark;
		Picture = picture;
		Password = password;
		RegimPlatoon = regimPlatoon;
		RegimSquad = regimSquad;
		LeaderType = leaderType;
		BloodType = bloodType;
		Goout = goout;
		Reserve01 = reserve01;
		Reserve02 = reserve02;
		Reserve03 = reserve03;
		Reserve04 = reserve04;
	}



	public String getServiceNumber() {
		return ServiceNumber;
	}
	public void setServiceNumber(String serviceNumber) {
		ServiceNumber = serviceNumber;
	}
	public String getMissionType() {
		return MissionType;
	}
	public void setMissionType(String missionType) {
		MissionType = missionType;
	}
	public String getRank() {
		return Rank;
	}
	public void setRank(String rank) {
		Rank = rank;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getRegiment() {
		return Regiment;
	}
	public void setRegiment(String regiment) {
		Regiment = regiment;
	}
	public String getRegimCompany() {
		return RegimCompany;
	}
	public void setRegimCompany(String regimCompany) {
		RegimCompany = regimCompany;
	}
	public String getMOS() {
		return MOS;
	}
	public void setMOS(String mOS) {
		MOS = mOS;
	}
	public String getDuty() {
		return Duty;
	}
	public void setDuty(String duty) {
		Duty = duty;
	}
	public String getHelpCare() {
		return HelpCare;
	}
	public void setHelpCare(String helpCare) {
		HelpCare = helpCare;
	}
	public String getBirthDate() {
		return BirthDate;
	}
	public void setBirthDate(String birthDate) {
		BirthDate = birthDate;
	}
	public String getJoinDate() {
		return JoinDate;
	}
	public void setJoinDate(String joinDate) {
		JoinDate = joinDate;
	}
	public String getPromotionDate() {
		return PromotionDate;
	}
	public void setPromotionDate(String promotionDate) {
		PromotionDate = promotionDate;
	}
	public String getMovingDate() {
		return MovingDate;
	}
	public void setMovingDate(String movingDate) {
		MovingDate = movingDate;
	}
	public String getRetireDate() {
		return RetireDate;
	}
	public void setRetireDate(String retireDate) {
		RetireDate = retireDate;
	}
	public String getMobileNumber() {
		return MobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		MobileNumber = mobileNumber;
	}
	public String getMyPhoneNumber() {
		return MyPhoneNumber;
	}
	public void setMyPhoneNumber(String myPhoneNumber) {
		MyPhoneNumber = myPhoneNumber;
	}
	public String getParentsNumber() {
		return ParentsNumber;
	}
	public void setParentsNumber(String parentsNumber) {
		ParentsNumber = parentsNumber;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}
	public String getPicture() {
		return Picture;
	}
	public void setPicture(String picture) {
		Picture = picture;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getRegimPlatoon() {
		return RegimPlatoon;
	}
	public void setRegimPlatoon(String regimPlatoon) {
		RegimPlatoon = regimPlatoon;
	}
	public String getRegimSquad() {
		return RegimSquad;
	}
	public void setRegimSquad(String regimSquad) {
		RegimSquad = regimSquad;
	}
	public String getLeaderType() {
		return LeaderType;
	}
	public void setLeaderType(String leaderType) {
		LeaderType = leaderType;
	}
	public String getBloodType() {
		return BloodType;
	}
	public void setBloodType(String bloodType) {
		BloodType = bloodType;
	}
	public String getGoout() {
		return Goout;
	}
	public void setGoout(String goout) {
		Goout = goout;
	}
	public String getReserve01() {
		return Reserve01;
	}
	public void setReserve01(String reserve01) {
		Reserve01 = reserve01;
	}
	public String getReserve02() {
		return Reserve02;
	}
	public void setReserve02(String reserve02) {
		Reserve02 = reserve02;
	}
	public String getReserve03() {
		return Reserve03;
	}
	public void setReserve03(String reserve03) {
		Reserve03 = reserve03;
	}
	public String getReserve04() {
		return Reserve04;
	}
	public void setReserve04(String reserve04) {
		Reserve04 = reserve04;
	}
	

	
	
	 
	
}

	