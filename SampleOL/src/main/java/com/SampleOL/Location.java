package com.SampleOL;



public class Location {
	
	String serviceNumber;
	String userKey;
	String name;
	String rank;
	String rankName;
	String regimentName;
	String regimCompanyName;
	String regiment;
	String regimCompany;
	String isDevice;
	String duty;
	String latitude;
	String longitude;
	String timestamp;
	String EventId;
	String EventDateTime;
	String MissionType;
	String EquipID;
	String EventType;
	String ObjectType;
	String Status;
	String ActionStartDate;
	String ActionEndDate;
	String Actioncontents;
	String ResultContents;
	String GroupCode;
	String IsSendOK;
	String EventRemark;
	String MobileNumber;
	String etc;
	String equipLocation;
	String roomName;
	String roomNumber;
	String mgrs;

	
	public Location() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Location(String serviceNumber, String userKey, String name, String rankName, String regimentName,
			String regimCompanyName, String isDevice, String duty, String latitude, String longitude, String timestamp) {
		super();
		this.serviceNumber = serviceNumber;
		this.userKey = userKey;
		this.name = name;
		this.rankName = rankName;
		this.regimentName = regimentName;
		this.regimCompanyName = regimCompanyName;
		this.isDevice = isDevice;
		this.duty = duty;
		this.latitude = latitude;
		this.longitude = longitude;
		this.timestamp = timestamp;
	}
	
	public Location(String serviceNumber, String userKey, String name, String rankName, String regimentName,
			String regimCompanyName, String isDevice, String duty, String latitude, String longitude, String timestamp, String etc) {
		super();
		this.serviceNumber = serviceNumber;
		this.userKey = userKey;
		this.name = name;
		this.rankName = rankName;
		this.regimentName = regimentName;
		this.regimCompanyName = regimCompanyName;
		this.isDevice = isDevice;
		this.duty = duty;
		this.latitude = latitude;
		this.longitude = longitude;
		this.timestamp = timestamp;
		this.etc = etc;
	}

	public Location(String serviceNumber, String userKey, String name, String rankName, String regimentName, String duty,
			String latitude, String longitude, String timestamp) {
		super();
		this.serviceNumber = serviceNumber;
		this.userKey = userKey;
		this.name = name;
		this.rankName = rankName;
		this.regimentName = regimentName;
		this.duty = duty;
		this.latitude = latitude;
		this.longitude = longitude;
		this.timestamp = timestamp;
	}

	

	
	public Location(String serviceNumber, String userKey, String name, String rank,String rankName, String regiment,String regimentName,
			String regimCompany,String regimCompanyName, String isDevice, String duty, String latitude, String longitude, String timestamp,String EventId,String EventDateTime,String MissionType,String EquipID
			 ,String EventType,String ObjectType,String EventRemark,String Status,String ActionStartDate,String ActionEndDate,String Actioncontents,String ResultContents,String GroupCode,String IsSendOK,String EquipLocation,String RoomName,String mobileNumber,String etc,String roomNumber,String mgrs) {
		super();
		this.serviceNumber = serviceNumber;
		this.userKey = userKey;
		this.name = name;
		this.rank = rank;
		this.rankName = rankName;
		this.regiment = regiment;
		this.regimentName = regimentName;
		this.regimCompany = regimCompany;
		this.regimCompanyName = regimCompanyName;
		this.isDevice = isDevice;
		this.duty = duty;
		this.latitude = latitude;
		this.longitude = longitude;
		this.timestamp = timestamp;
		this.EventId = EventId;
		this.EventDateTime = EventDateTime;
		this.MissionType = MissionType;
		this.EquipID = EquipID;
		this.EventType = EventType;
		this.ObjectType = ObjectType;
		this.Status = Status;
		this.ActionStartDate = ActionStartDate;
		this.ActionEndDate = ActionEndDate;
		this.Actioncontents = Actioncontents;
		this.ResultContents = ResultContents;
		this.GroupCode = GroupCode;
		this.IsSendOK = IsSendOK;
		this.EventRemark = EventRemark;
		this.equipLocation = EquipLocation;
		this.roomName = RoomName;
		this.MobileNumber = mobileNumber;
		this.etc = etc;
		this.roomNumber = roomNumber;
		this.mgrs = mgrs;

		
	}
	public String getRegimCompany() {
		return regimCompanyName;
	}

	public void setRegimCompany(String regimCompanyName) {
		this.regimCompanyName = regimCompanyName;
	}

	public String getIsDevice() {
		return isDevice;
	}

	public void setIsDevice(String isDevice) {
		this.isDevice = isDevice;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public Location(String userKey, String latitude, String longitude, String timestamp) {
		super();
		this.userKey = userKey;
		this.latitude = latitude;
		this.longitude = longitude;
		this.timestamp = timestamp;
	}



	public Location(String serviceNumber, String userKey, String name, String rankName, String latitude, String longitude,
			String timestamp) {
		super();
		this.serviceNumber = serviceNumber;
		this.userKey = userKey;
		this.name = name;
		this.rankName = rankName;
		this.latitude = latitude;
		this.longitude = longitude;
		this.timestamp = timestamp;
	}

	public Location(String serviceNumber, String userKey, String name, String rankName, String regimentName, String latitude,
			String longitude, String timestamp) {
		super();
		this.serviceNumber = serviceNumber;
		this.userKey = userKey;
		this.name = name;
		this.rankName = rankName;
		this.regimentName = regimentName;
		this.latitude = latitude;
		this.longitude = longitude;
		this.timestamp = timestamp;
	}



	public Location(String serviceNumber, String userKey, String name, String rankName, String regimentName,
			String regimCompanyName, String isDevice, String duty, String latitude, String longitude, String timestamp,
			String mobileNumber, String etc, String equipLocation, String roomName,String roomNumber,String Mgrs) {
		super();
		this.serviceNumber = serviceNumber;
		this.userKey = userKey;
		this.name = name;
		this.rankName = rankName;
		this.regimentName = regimentName;
		this.regimCompanyName = regimCompanyName;
		this.isDevice = isDevice;
		this.duty = duty;
		this.latitude = latitude;
		this.longitude = longitude;
		this.timestamp = timestamp;
		this.MobileNumber = mobileNumber;
		this.etc = etc;
		this.equipLocation = equipLocation;
		this.roomName = roomName;
		this.roomNumber = roomNumber;
		this.mgrs = Mgrs;

	}

	public Location(String serviceNumber, String userKey, String name, String rankName, String regimentName,
			String regimCompanyName, String isDevice, String duty, String latitude, String longitude, String timestamp,
			String mobileNumber, String equipLocation, String roomName,String roomNumber,String Mgrs) {
		super();
		this.serviceNumber = serviceNumber;
		this.userKey = userKey;
		this.name = name;
		this.rankName = rankName;
		this.regimentName = regimentName;
		this.regimCompanyName = regimCompanyName;
		this.isDevice = isDevice;
		this.duty = duty;
		this.latitude = latitude;
		this.longitude = longitude;
		this.timestamp = timestamp;
		this.MobileNumber = mobileNumber;
		this.equipLocation = equipLocation;
		this.roomName = roomName;
		this.roomNumber = roomNumber;
		this.mgrs = Mgrs;
	}
	
	public String getRegiment() {
		return regimentName;
	}

	public void setRegiment(String regimentName) {
		this.regimentName = regimentName;
	}

	
	

	@Override
	public String toString() {
		return "Location [serviceNumber=" + serviceNumber + ", userKey=" + userKey + ", name=" + name + ", rank=" + rank
				+ ", regimentName=" + regimentName + ", regimCompanyName=" + regimCompanyName + ", isDevice=" + isDevice + ", duty="
				+ duty + ", latitude=" + latitude + ", longitude=" + longitude + ", timestamp=" + timestamp + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	 public String getServiceNumber() {
		 return serviceNumber;
	 }

	 public void setServiceNumber(String serviceNumber) {
		 this.serviceNumber = serviceNumber;
	 }

	 public String getUserKey() {
		 return userKey;
	 }

	 public void setUserKey(String userKey) {
		 this.userKey = userKey;
	 }

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getEventId() {
		return EventId;
	}

	public void setEventId(String eventId) {
		EventId = eventId;
	}

	public String getEventDateTime() {
		return EventDateTime;
	}

	public void setEventDateTime(String eventDateTime) {
		EventDateTime = eventDateTime;
	}

	public String getMissionType() {
		return MissionType;
	}

	public void setMissionType(String missionType) {
		MissionType = missionType;
	}

	public String getEquipID() {
		return EquipID;
	}

	public void setEquipID(String equipID) {
		EquipID = equipID;
	}

	public String getEventType() {
		return EventType;
	}

	public void setEventType(String eventType) {
		EventType = eventType;
	}

	public String getObjectType() {
		return ObjectType;
	}

	public void setObjectType(String objectType) {
		ObjectType = objectType;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getActionStartDate() {
		return ActionStartDate;
	}

	public void setActionStartDate(String actionStartDate) {
		ActionStartDate = actionStartDate;
	}

	public String getActionEndDate() {
		return ActionEndDate;
	}

	public void setActionEndDate(String actionEndDate) {
		ActionEndDate = actionEndDate;
	}

	public String getActioncontents() {
		return Actioncontents;
	}

	public void setActioncontents(String actioncontents) {
		Actioncontents = actioncontents;
	}

	public String getResultContents() {
		return ResultContents;
	}

	public void setResultContents(String resultContents) {
		ResultContents = resultContents;
	}

	public String getGroupCode() {
		return GroupCode;
	}

	public void setGroupCode(String groupCode) {
		GroupCode = groupCode;
	}

	public String getIsSendOK() {
		return IsSendOK;
	}

	public void setIsSendOK(String isSendOK) {
		IsSendOK = isSendOK;
	}

	public String getEventRemark() {
		return EventRemark;
	}

	public void setEventRemark(String eventRemark) {
		EventRemark = eventRemark;
	}

	public String getMobileNumber() {
		return MobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		MobileNumber = mobileNumber;
	}

	public String getEtc() {
		return etc;
	}

	public void setEtc(String etc) {
		this.etc = etc;
	}

	public String getEquipLocation() {
		return equipLocation;
	}

	public void setEquipLocation(String equipLocation) {
		this.equipLocation = equipLocation;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public String getRankName() {
		return rankName;
	}

	public void setRankName(String rankName) {
		this.rankName = rankName;
	}

	public String getRegimentName() {
		return regimentName;
	}

	public void setRegimentName(String regimentName) {
		this.regimentName = regimentName;
	}

	public String getRegimCompanyName() {
		return regimCompanyName;
	}

	public void setRegimCompanyName(String regimCompanyName) {
		this.regimCompanyName = regimCompanyName;
	}
	 
	 
	
}