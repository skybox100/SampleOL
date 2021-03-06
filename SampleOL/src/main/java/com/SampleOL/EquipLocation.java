package com.SampleOL;

public class EquipLocation {

	String equipId;
	String regiment;
	String equipType;
	String equipLocation;
	String longitude;
	String latitude;
	String mgrs;
	

	
	 @Override
	public String toString() {
		return "EquipLocation [equipId=" + equipId + ", regiment=" + regiment + ", equipType=" + equipType
				+ ", equipLocation=" + equipLocation + ", longitude=" + longitude + ", latitude=" + latitude + ", mgrs="
				+ mgrs + "]";
	}

	public EquipLocation() {
		 super();
	 }
	 
	public EquipLocation(String equipId, String regiment, String equipType, String equipLocation, String longitude,
			String latitude,String mgrs) {
		super();
		this.equipId = equipId;
		this.regiment = regiment;
		this.equipType = equipType;
		this.equipLocation = equipLocation;
		this.longitude = longitude;
		this.latitude = latitude;
		this.mgrs = mgrs;
	}

	public String getEquipId() {
		return equipId;
	}

	public void setEquipId(String equipId) {
		this.equipId = equipId;
	}

	public String getRegiment() {
		return regiment;
	}

	public void setRegiment(String regiment) {
		this.regiment = regiment;
	}

	public String getEquipType() {
		return equipType;
	}

	public void setEquipType(String equipType) {
		this.equipType = equipType;
	}

	public String getEquipLocation() {
		return equipLocation;
	}

	public void setEquipLocation(String equipLocation) {
		this.equipLocation = equipLocation;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getMgrs() {
		return mgrs;
	}

	public void setMgrs(String mgrs) {
		this.mgrs = mgrs;
	}



	 
	
}