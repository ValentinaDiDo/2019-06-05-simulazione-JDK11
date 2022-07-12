package it.polito.tdp.crimes.model;

import com.javadocmd.simplelatlng.LatLng;

public class District {
	private int districtId;
	private LatLng coordinate;
	public District(int districtId, LatLng coordinate) {
		super();
		this.districtId = districtId;
		this.coordinate = coordinate;
	}
	public int getDistrictId() {
		return districtId;
	}
	public LatLng getCoordinate() {
		return coordinate;
	}
	
	
}
