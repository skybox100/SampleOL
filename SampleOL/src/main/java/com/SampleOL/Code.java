package com.SampleOL;



public class Code {
	
	String CodeType;
	String CodeID;
	String GroupCode;
	String CodeTypeName;
	String CodeName;
	String Remark;
	
	
	
	
	
	public Code(String codeType, String codeID, String groupCode, String codeTypeName, String codeName, String remark) {
		super();
		CodeType = codeType;
		CodeID = codeID;
		GroupCode = groupCode;
		CodeTypeName = codeTypeName;
		CodeName = codeName;
		Remark = remark;
	}
	public String getCodeType() {
		return CodeType;
	}
	public void setCodeType(String codeType) {
		CodeType = codeType;
	}
	public String getCodeID() {
		return CodeID;
	}
	public void setCodeID(String codeID) {
		CodeID = codeID;
	}
	public String getGroupCode() {
		return GroupCode;
	}
	public void setGroupCode(String groupCode) {
		GroupCode = groupCode;
	}
	public String getCodeTypeName() {
		return CodeTypeName;
	}
	public void setCodeTypeName(String codeTypeName) {
		CodeTypeName = codeTypeName;
	}
	public String getCodeName() {
		return CodeName;
	}
	public void setCodeName(String codeName) {
		CodeName = codeName;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}

	
	
	

	
}