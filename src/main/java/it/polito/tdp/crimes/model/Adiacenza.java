package it.polito.tdp.crimes.model;

public class Adiacenza {
	private District d1;
	private District d2;
	private double distanza ;
	public Adiacenza(District d1, District d2, double distanza) {
		super();
		this.d1 = d1;
		this.d2 = d2;
		this.distanza = distanza;
	}
	public District getD1() {
		return d1;
	}
	public District getD2() {
		return d2;
	}
	public double getDistanza() {
		return distanza;
	}
	
	
}


