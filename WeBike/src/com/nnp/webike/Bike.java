package com.nnp.webike;

public class Bike {
	double lat;
	double lng;
	boolean avail;
	String description;
	String lock;
	int ID;

	public Bike(double lat, double lng, boolean avail, String description, int ID) {
		super();
		this.lat = lat;
		this.lng = lng;
		this.avail = avail;
		this.description = description;
		this.ID = ID;
	}
	
	public int getID(){
		return ID;
	}

	public String getLock(){
		return lock;
	}
	
	public void setLock(String lock){
		this.lock = lock;
	}
	
	public double getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(float lng) {
		this.lng = lng;
	}

	public boolean isAvail() {
		return avail;
	}

	public void setAvail(boolean avail) {
		this.avail = avail;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
