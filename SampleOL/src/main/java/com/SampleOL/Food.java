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
	String currentQuantity;
	String unit;
	String foodSource;
	String foodSourceName;
	String qRcodeIdx;
	String remark;
	
	
	
	public Food(String regiment, String regimentName, String storehouse, String storehouseName, String foodCode,
			String expirationDate, String foodName, String storeDate, String currentQuantity, String unit,
			String foodSource, String foodSourceName, String qRcodeIdx, String remark) {
		super();
		this.regiment = regiment;
		this.regimentName = regimentName;
		this.storehouseName = storehouseName;
		this.storehouse = storehouse;
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
	public void setRegimentName(String regimentName) {
		this.regimentName = regimentName;
	}
	public String getStorehouseName() {
		return storehouseName;
	}
	public void setStorehouseName(String storehouseName) {
		this.storehouseName = storehouseName;
	}
	public String getStorehouse() {
		return storehouse;
	}
	public void setStorehouse(String storehouse) {
		this.storehouse = storehouse;
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
	public String getCurrentQuantity() {
		return currentQuantity;
	}
	public void setCurrentQuantity(String currentQuantity) {
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
	public String getqRcodeIdx() {
		return qRcodeIdx;
	}
	public void setqRcodeIdx(String qRcodeIdx) {
		this.qRcodeIdx = qRcodeIdx;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	


	 
	
}