package com.SampleOL;



public class Beacons {
	
	String Uuid;
	String Latitude;
	String Longitude;
	String EquipTypeName;
	String EquipType;
	String EquipId;
	String ModelName;
	String Manufacturer;
	String RegimentName;
	String Regiment;
	String RegimCompanyName;
	String RegimCompany;
	String EquipLocation;
	String RoomName;
	String RoomNumber;
	String Remark;
	String Mgrs;

	
	



	@Override
	public String toString() {
		return "Beacons [Uuid=" + Uuid + ", Latitude=" + Latitude + ", Longitude=" + Longitude + ", EquipTypeName="
				+ EquipTypeName + ", EquipType=" + EquipType + ", EquipId=" + EquipId + ", ModelName=" + ModelName
				+ ", Manufacturer=" + Manufacturer + ", RegimentName=" + RegimentName + ", Regiment=" + Regiment
				+ ", RegimCompanyName=" + RegimCompanyName + ", RegimCompany=" + RegimCompany + ", EquipLocation="
				+ EquipLocation + ", RoomName=" + RoomName + ", RoomNumber=" + RoomNumber + ", Remark=" + Remark
				+ ", Mgrs=" + Mgrs + "]";
	}



	public Beacons(String uuid, String latitude, String longitude, String equipTypeName, String equipType,
			String equipId, String modelName, String manufacturer, String regiment, String regimentCode,
			String regimCompanyName, String regimCompany, String equipLocation, String roomName, String roomNumber,
			String remark,String mgrs) {
		super();
		Uuid = uuid;
		Latitude = latitude;
		Longitude = longitude;
		EquipTypeName = equipTypeName;
		EquipType = equipType;
		EquipId = equipId;
		ModelName = modelName;
		Manufacturer = manufacturer;
		RegimentName = regiment;
		Regiment = regimentCode;
		RegimCompanyName = regimCompanyName;
		RegimCompany = regimCompany;
		EquipLocation = equipLocation;
		RoomName = roomName;
		RoomNumber = roomNumber;
		Remark = remark;
		Mgrs = mgrs;
	}
	
	
	
	public String getUuid() {
		return Uuid;
	}
	public void setUuid(String uuid) {
		Uuid = uuid;
	}
	public String getLatitude() {
		return Latitude;
	}
	public void setLatitude(String latitude) {
		Latitude = latitude;
	}
	public String getLongitude() {
		return Longitude;
	}
	public void setLongitude(String longitude) {
		Longitude = longitude;
	}
	public String getEquipTypeName() {
		return EquipTypeName;
	}
	public void setEquipTypeName(String equipTypeName) {
		EquipTypeName = equipTypeName;
	}
	public String getEquipType() {
		return EquipType;
	}
	public void setEquipType(String equipType) {
		EquipType = equipType;
	}
	public String getEquipId() {
		return EquipId;
	}
	public void setEquipId(String equipId) {
		EquipId = equipId;
	}
	public String getModelName() {
		return ModelName;
	}
	public void setModelName(String modelName) {
		ModelName = modelName;
	}
	public String getManufacturer() {
		return Manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		Manufacturer = manufacturer;
	}
	public String getRegimentName() {
		return RegimentName;
	}
	public void setRegimentName(String regiment) {
		RegimentName = regiment;
	}
	public String getRegiment() {
		return Regiment;
	}
	public void setRegiment(String regimentCode) {
		Regiment = regimentCode;
	}
	public String getRegimCompanyName() {
		return RegimCompanyName;
	}
	public void setRegimCompanyName(String regimCompanyName) {
		RegimCompanyName = regimCompanyName;
	}
	public String getRegimCompany() {
		return RegimCompany;
	}
	public void setRegimCompany(String regimCompany) {
		RegimCompany = regimCompany;
	}
	public String getEquipLocation() {
		return EquipLocation;
	}
	public void setEquipLocation(String equipLocation) {
		EquipLocation = equipLocation;
	}
	public String getRoomName() {
		return RoomName;
	}
	public void setRoomName(String roomName) {
		RoomName = roomName;
	}
	public String getRoomNumber() {
		return RoomNumber;
	}
	public void setRoomNumber(String roomNumber) {
		RoomNumber = roomNumber;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}



	public String getMgrs() {
		return Mgrs;
	}



	public void setMgrs(String mgrs) {
		Mgrs = mgrs;
	}


	
}