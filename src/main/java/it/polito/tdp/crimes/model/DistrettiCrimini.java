package it.polito.tdp.crimes.model;

public class DistrettiCrimini {
	int districtID;
	int numeroCrimini;
	public DistrettiCrimini(int districtID, int numeroCrimini) {
		super();
		this.districtID = districtID;
		this.numeroCrimini = numeroCrimini;
	}
	public int getDistrictID() {
		return districtID;
	}
	public void setDistrictID(int districtID) {
		this.districtID = districtID;
	}
	public int getNumeroCrimini() {
		return numeroCrimini;
	}
	public void setNumeroCrimini(int numeroCrimini) {
		this.numeroCrimini = numeroCrimini;
	}
	
	
}
