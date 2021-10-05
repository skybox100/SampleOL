package com.SampleOL;

public class Circle {
	String latitude;
	String longitude;
	String r;
	String regiment;
	public Circle(String latitude, String longitude, String r, String regiment) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.r = r;
		this.regiment = regiment;
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
	public String getR() {
		return r;
	}
	public void setR(String r) {
		this.r = r;
	}
	public String getRegiment() {
		return regiment;
	}
	public void setRegiment(String regiment) {
		this.regiment = regiment;
	}

	
	
}
