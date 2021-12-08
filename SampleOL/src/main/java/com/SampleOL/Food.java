package com.SampleOL;



public class Food {
	
	String regiment;
	String regimentName;
	String storehouse;
	String storehouseName;
	String foodCode;
	String expirationDate;
	String foodName;
	String storeDate;
	int currentQuantity;
	String unit;
	String foodSource;
	String foodSourceName;
	long qRcodeIdx;
	String remark;
	
	
	
	public Food(String regiment, String RegimentName, String Storehouse, String StorehouseName, String foodCode,
			String expirationDate, String foodName, String storeDate, int currentQuantity, String unit,
			String foodSource, String foodSourceName, long qRcodeIdx, String remark) {
		super();
		this.regiment = regiment;
		this.regimentName = RegimentName;
		this.storehouseName = StorehouseName;
		this.storehouse = Storehouse;
		this.foodCode = foodCode;
		this.expirationDate = expirationDate;
		this.foodName = foodName;
		this.storeDate = storeDate;
		this.currentQuantity = currentQuantity;
		this.unit = unit;
		this.foodSource = foodSource;
		this.foodSourceName = foodSourceName;
		this.qRcodeIdx = qRcodeIdx;
		this.remark = remark;
	}
	
	public String getRegiment() {
		return regiment;
	}
	public void setRegiment(String regiment) {
		this.regiment = regiment;
	}
	public String getRegimentName() {
		return regimentName;
	}
	public void setRegimentName(String RegimentName) {
		this.regimentName = RegimentName;
	}
	public String getStorehouseName() {
		return storehouseName;
	}
	public void setStorehouseName(String StorehouseName) {
		this.storehouseName = StorehouseName;
	}
	public String getStorehouse() {
		return storehouse;
	}
	public void setStorehouse(String Storehouse) {
		this.storehouse = Storehouse;
	}
	public String getFoodCode() {
		return foodCode;
	}
	public void setFoodCode(String foodCode) {
		this.foodCode = foodCode;
	}
	public String getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getFoodName() {
		return foodName;
	}
	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}
	public String getStoreDate() {
		return storeDate;
	}
	public void setStoreDate(String storeDate) {
		this.storeDate = storeDate;
	}
	public int getCurrentQuantity() {
		return currentQuantity;
	}
	public void setCurrentQuantity(int currentQuantity) {
		this.currentQuantity = currentQuantity;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getFoodSource() {
		return foodSource;
	}
	public void setFoodSource(String foodSource) {
		this.foodSource = foodSource;
	}
	
	public String getFoodSourceName() {
		return foodSourceName;
	}
	public void setFoodSourceName(String foodSourceName) {
		this.foodSourceName = foodSourceName;
	}
	public long getqRcodeIdx() {
		return qRcodeIdx;
	}
	public void setqRcodeIdx(long qRcodeIdx) {
		this.qRcodeIdx = qRcodeIdx;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	


	 
	
}