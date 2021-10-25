package com.SampleOL;



public class Beacons {
	
	String Uuid;
	String Latitude;
	String Longitude;
	String EquipType;
	String EquipTypeCode;
	String EquipId;
	String ModelName;
	String Manufacturer;
	String Regiment;
	String RegimentCode;
	String RegimCompany;
	String RegimCompanyCode;
	String EquipLocation;
	String RoomName;
	String RoomNumber;
	String Remark;
	public Beacons(String uuid, String latitude, String longitude, String equipType, String equipTypeCode,
			String equipId, String modelName, String manufacturer, String regiment, String regimentCode,
			String regimCompany, String regimCompanyCode, String equipLocation, String roomName, String roomNumber,
			String remark) {
		super();
		Uuid = uuid;
		Latitude = latitude;
		Longitude = longitude;
		EquipType = equipType;
		EquipTypeCode = equipTypeCode;
		EquipId = equipId;
		ModelName = modelName;
		Manufacturer = manufacturer;
		Regiment = regiment;
		RegimentCode = regimentCode;
		RegimCompany = regimCompany;
		RegimCompanyCode = regimCompanyCode;
		EquipLocation = equipLocation;
		RoomName = roomName;
		RoomNumber = roomNumber;
		Remark = remark;
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
	public String getEquipType() {
		return EquipType;
	}
	public void setEquipType(String equipType) {
		EquipType = equipType;
	}
	public String getEquipTypeCode() {
		return EquipTypeCode;
	}
	public void setEquipTypeCode(String equipTypeCode) {
		EquipTypeCode = equipTypeCode;
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


	
}